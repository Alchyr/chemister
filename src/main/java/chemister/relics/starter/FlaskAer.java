package chemister.relics.starter;

import chemister.actions.infuse.DisplayableAction;
import chemister.actions.infuse.PowerDisplayAction;
import chemister.cards.ReagentCard;
import chemister.cards.uncommon.TurbulenceTonic;
import chemister.character.Chemister;
import chemister.infuse.FlaskBaseEffect;
import chemister.infuse.InfuseEffect;
import chemister.powers.AerPower;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static chemister.ChemisterMod.makeID;

public class FlaskAer extends FlaskRelic {
    private static final String NAME = "FlaskAer";
    public static final String ID = makeID(NAME);

    private final FlaskBaseEffect effect = new FlaskBaseEffect(this, "DISCOUNT") {
        @Override
        public DisplayableAction createAction(int amt) {
            int times = 1;
            for (ReagentCard.ReagentEffect effect : FlaskAer.this.reagentEffects) {
                if (effect.ID.equals(TurbulenceTonic.ID)) {
                    times += effect.maxAmount;
                }
            }
            return new PowerDisplayAction(AbstractDungeon.player, AbstractDungeon.player, new AerPower(AbstractDungeon.player, amt, times));
        }
    };

    public FlaskAer() {
        super(ID, NAME, Chemister.Meta.CARD_COLOR, RelicTier.STARTER, LandingSound.CLINK);

        counter = 1;
        refreshDescriptionAndTips();
    }

    @Override
    public void resetCounter() {
        counter = 1;
    }

    @Override
    public Chemister.Flasks flaskType() {
        return Chemister.Flasks.AER;
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
