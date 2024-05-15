package chemister.relics.starter;

import chemister.actions.ReduceCostOfRandomCardForTurnAction;
import chemister.character.Chemister;
import chemister.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static chemister.ChemisterMod.makeID;

public class ChemisterJournal extends BaseRelic {
    private static final String NAME = "ChemisterJournal";
    public static final String ID = makeID(NAME);

    private boolean activated = false;

    public ChemisterJournal() {
        super(ID, NAME, Chemister.Meta.CARD_COLOR, RelicTier.STARTER, LandingSound.FLAT);
    }

    public void atBattleStartPreDraw() {
        this.activated = false;
    }

    public void atTurnStartPostDraw() {
        if (!this.activated) {
            this.activated = true;
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new ReduceCostOfRandomCardForTurnAction(1));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
