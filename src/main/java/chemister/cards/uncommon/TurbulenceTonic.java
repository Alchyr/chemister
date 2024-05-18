package chemister.cards.uncommon;

import basemod.helpers.CardModifierManager;
import chemister.actions.infuse.DisplayableAction;
import chemister.cardmods.TemporaryCardmod;
import chemister.cards.ReagentCard;
import chemister.character.Chemister;
import chemister.relics.starter.FlaskRelic;
import chemister.util.CardStats;
import com.megacrit.cardcrawl.helpers.PowerTip;

public class TurbulenceTonic extends ReagentCard {
    public static final String ID = makeID(TurbulenceTonic.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            -2
    );

    private static final Chemister.Flasks[] flasks = new Chemister.Flasks[] {
            Chemister.Flasks.AER
    };

    public TurbulenceTonic() {
        super(ID, info);

        CardModifierManager.addModifier(this, new TemporaryCardmod(4, true));
    }

    @Override
    public Chemister.Flasks[] getFlasks() {
        return flasks;
    }

    @Override
    public ReagentEffect getEffect(FlaskRelic flask) {
        if (flask.flaskType() == Chemister.Flasks.AER) {
            return new ReagentEffect(ID, -1, 1) {
                @Override
                public DisplayableAction createAction(int amt) {
                    return null;
                }

                @Override
                public PowerTip getTip() {
                    return new PowerTip(text[0], maxAmount == 1 ? text[1] : String.format(text[2], maxAmount));
                }
            };
        }
        return null;
    }
}
