package chemister.cards.uncommon;

import chemister.actions.infuse.DisplayableAction;
import chemister.cards.ReagentCard;
import chemister.character.Chemister;
import chemister.relics.starter.FlaskRelic;
import chemister.util.CardStats;
import com.megacrit.cardcrawl.helpers.PowerTip;

public class PowderedPebble extends ReagentCard {
    public static final String ID = makeID(PowderedPebble.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            -2
    );

    private static final Chemister.Flasks[] flasks = new Chemister.Flasks[] {
            Chemister.Flasks.IGNIS
    };

    public PowderedPebble() {
        super(ID, info);

        setMagic(1);
    }

    @Override
    public Chemister.Flasks[] getFlasks() {
        return flasks;
    }

    @Override
    public ReagentEffect getEffect(FlaskRelic flask) {
        if (flask.flaskType() == Chemister.Flasks.TERRA) {
            flask.counter += magicNumber;
            return new ReagentEffect(ID, -1, magicNumber) {
                @Override
                public DisplayableAction createAction(int amt) {
                    return null;
                }

                @Override
                public void applyValueChange(FlaskRelic flaskRelic) {
                    flaskRelic.counter += maxAmount;
                }

                @Override
                public PowerTip getTip() {
                    return new PowerTip(text[0], String.format(text[1], maxAmount));
                }
            };
        }
        return null;
    }
}
