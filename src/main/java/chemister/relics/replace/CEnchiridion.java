package chemister.relics.replace;

import basemod.helpers.CardModifierManager;
import chemister.cardmods.TemporaryCardmod;
import chemister.character.Chemister;
import chemister.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.Enchiridion;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static chemister.ChemisterMod.makeID;
import static chemister.ChemisterMod.reagents;

public class CEnchiridion extends BaseRelic {
    private static final String NAME = CEnchiridion.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private static final String copyID = Enchiridion.ID;
    static {
        RelicStrings base = CardCrawlGame.languagePack.getRelicStrings(copyID);
        RelicStrings copy = CardCrawlGame.languagePack.getRelicStrings(ID);

        copy.NAME = base.NAME;
        copy.FLAVOR = base.FLAVOR;
    }

    public CEnchiridion() {
        super(ID, "enchiridion.png", Chemister.Meta.CARD_COLOR, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public void atBattleStartPreDraw() {
        flash();
        addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));

        AbstractCard tempReagent = reagents.get(AbstractDungeon.cardRandomRng.random(reagents.size() - 1));

        tempReagent = tempReagent.makeStatEquivalentCopy();

        AbstractCard finalReagent = tempReagent;
        boolean hasInherentTemporary = CardModifierManager.getModifiers(tempReagent, TemporaryCardmod.ID).stream().anyMatch((modifier)->modifier.isInherent(finalReagent));

        CardModifierManager.removeModifiersById(tempReagent, TemporaryCardmod.ID, true);
        CardModifierManager.addModifier(tempReagent, new TemporaryCardmod(1, hasInherentTemporary));

        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(tempReagent, Settings.WIDTH / 2f, Settings.HEIGHT / 2f));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
