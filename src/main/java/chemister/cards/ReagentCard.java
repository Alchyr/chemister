package chemister.cards;

import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import chemister.ChemisterMod;
import chemister.cardmods.TemporaryCardmod;
import chemister.character.Chemister;
import chemister.infuse.InfuseEffect;
import chemister.relics.starter.FlaskRelic;
import chemister.util.CardStats;
import chemister.util.KeywordInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.ArrayList;
import java.util.List;

public abstract class ReagentCard extends BaseCard {
    private static final KeywordInfo reagentKeyword = ChemisterMod.keywords.get("reagent");
    private static final List<TooltipInfo> reagentKeywordTip;
    static {
        reagentKeywordTip = new ArrayList<>();
        reagentKeywordTip.add(new TooltipInfo(reagentKeyword.PROPER_NAME, reagentKeyword.DESCRIPTION));
    }

    public ReagentCard(String ID, CardStats info) {
        super(ID, info);
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCard reagent = makeStatEquivalentCopy();
        boolean hasInherentTemporary = CardModifierManager.getModifiers(reagent, TemporaryCardmod.ID).stream().anyMatch((modifier)->modifier.isInherent(reagent));

        CardModifierManager.removeModifiersById(reagent, TemporaryCardmod.ID, true);
        CardModifierManager.addModifier(reagent, new TemporaryCardmod(1, hasInherentTemporary));

        ShowCardAndObtainEffect vfx = new ShowCardAndObtainEffect(reagent, Settings.WIDTH / 2f, Settings.HEIGHT / 2f);
        addToBot(new VFXAction(vfx));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = vfx.isDone;
            }
        });
    }

    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        return reagentKeywordTip;
    }

    public abstract Chemister.Flasks[] getFlasks();

    public abstract ReagentEffect getEffect(FlaskRelic flask);

    public static abstract class ReagentEffect extends InfuseEffect {
        public int maxAmount;

        /*
        Priorities:
        -5 - Clairvoyant Crosswinds
        -1 - Flat value buffs (no action)
        0 - Reagent base effects
        2 - Acidic Acetate
        4 - Seeping Solvent
        5 - Buffered
        6 - Echoes
        8 - Oxidizing
        10 - Bernoulli's
        15 - Double Dipper (Relic)
        16 - Volatile Mix
        99 - Fulminating
         */

        public final String[] text;

        public ReagentEffect(String ID, int priority, int amount) {
            super(ID, amount);
            this.priority = priority;
            text = CardCrawlGame.languagePack.getUIString(this.ID).TEXT;
            maxAmount = amount;
        }

        public ReagentEffect merge(ReagentEffect otherEffect) {
            if (otherEffect.ID.equals(ID)) {
                this.maxAmount += otherEffect.maxAmount;
            }
            else {
                ChemisterMod.logger.error("Attempted to merge ReagentEffects with different IDs; " + ID + " | " + otherEffect.ID);
            }
            return (ReagentEffect) super.merge(otherEffect);
        }

        public abstract PowerTip getTip();
    }
}
