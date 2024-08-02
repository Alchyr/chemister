package chemister.relics.replace;

import chemister.cards.ReagentCard;
import chemister.character.Chemister;
import chemister.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.FrozenEgg2;

import static chemister.ChemisterMod.makeID;

public class CFrozenEgg extends BaseRelic {
    private static final String NAME = CFrozenEgg.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private static final String copyID = FrozenEgg2.ID;
    static {
        RelicStrings base = CardCrawlGame.languagePack.getRelicStrings(copyID);
        RelicStrings copy = CardCrawlGame.languagePack.getRelicStrings(ID);

        copy.NAME = base.NAME;
        copy.FLAVOR = base.FLAVOR;
    }

    private boolean firstTurn = false;

    public CFrozenEgg() {
        super(ID, "frozenEgg.png", Chemister.Meta.CARD_COLOR, RelicTier.UNCOMMON, LandingSound.CLINK);

        counter = 0;
    }

    @Override
    public void onObtainCard(AbstractCard c) {
        if (c instanceof ReagentCard) {
            flash();
            pulse = true;
            ++counter;
        }
    }

    public void atPreBattle() {
        this.firstTurn = true;
    }

    public void atTurnStart() {
        if (this.firstTurn) {
            if (this.counter > 0) {
                this.addToTop(new GainEnergyAction(counter));
                this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));

                this.pulse = false;
                this.counter = 0;
                this.flash();
            }

            this.firstTurn = false;
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
