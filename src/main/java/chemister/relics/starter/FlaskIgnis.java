package chemister.relics.starter;

import chemister.actions.infuse.DisplayableAction;
import chemister.actions.infuse.PowerDisplayAction;
import chemister.character.Chemister;
import chemister.infuse.FlaskBaseEffect;
import chemister.infuse.InfuseEffect;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static chemister.ChemisterMod.makeID;

public class FlaskIgnis extends FlaskRelic {
    private static final String NAME = "FlaskIgnis";
    public static final String ID = makeID(NAME);

    private static final int BASE_STRENGTH = 3;

    private final FlaskBaseEffect effect = new FlaskBaseEffect(this, "VIGOR") {
        @Override
        public DisplayableAction createAction(int amt) {
            return new PowerDisplayAction(AbstractDungeon.player, AbstractDungeon.player, new VigorPower(AbstractDungeon.player, amt));
        }
    };

    public FlaskIgnis() {
        super(ID, NAME, Chemister.Meta.CARD_COLOR, RelicTier.STARTER, LandingSound.CLINK);

        counter = BASE_STRENGTH;
        refreshDescriptionAndTips();
    }

    @Override
    public void resetCounter() {
        counter = BASE_STRENGTH;
    }

    @Override
    public Chemister.Flasks flaskType() {
        return Chemister.Flasks.IGNIS;
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
