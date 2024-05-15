package chemister.relics.starter;

import chemister.actions.infuse.DisplayableAction;
import chemister.character.Chemister;
import chemister.infuse.FlaskBaseEffect;
import chemister.infuse.InfuseEffect;
import chemister.actions.infuse.BlockDisplayAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static chemister.ChemisterMod.makeID;

public class FlaskTerra extends FlaskRelic {
    private static final String NAME = "FlaskTerra";
    public static final String ID = makeID(NAME);

    private final FlaskBaseEffect effect = new FlaskBaseEffect(this, "BLOCK") {
        @Override
        public DisplayableAction createAction(int amt) {
            return new BlockDisplayAction(AbstractDungeon.player, AbstractDungeon.player, amt);
        }
    };

    public FlaskTerra() {
        super(ID, NAME, Chemister.Meta.CARD_COLOR, RelicTier.STARTER, LandingSound.CLINK);

        counter = 4;
        refreshDescriptionAndTips();
    }

    @Override
    public void resetCounter() {
        counter = 4;
    }

    @Override
    public Chemister.Flasks flaskType() {
        return Chemister.Flasks.TERRA;
    }

    @Override
    public InfuseEffect getBaseEffect() {
        return effect;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + counter + DESCRIPTIONS[1];
    }
}
