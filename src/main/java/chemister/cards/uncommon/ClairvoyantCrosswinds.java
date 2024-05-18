package chemister.cards.uncommon;

import chemister.actions.infuse.DisplayableAction;
import chemister.actions.infuse.ScryDisplayAction;
import chemister.cards.ReagentCard;
import chemister.character.Chemister;
import chemister.relics.starter.FlaskRelic;
import chemister.util.CardStats;
import chemister.util.GeneralUtils;
import com.megacrit.cardcrawl.helpers.PowerTip;

import java.util.List;

public class ClairvoyantCrosswinds extends ReagentCard {
    public static final String ID = makeID(ClairvoyantCrosswinds.class.getSimpleName());
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

    public ClairvoyantCrosswinds() {
        super(ID, info);
    }

    @Override
    public Chemister.Flasks[] getFlasks() {
        return flasks;
    }

    @Override
    public ReagentEffect getEffect(FlaskRelic flask) {
        if (flask.flaskType() == Chemister.Flasks.AER) {
            return new ReagentEffect(ID, -5, 1) {
                @Override
                protected int getAmount(int infusedCount, List<Chemister.Flasks> infusedThisTurn, List<List<Chemister.Flasks>> infusedThisCombat) {
                    return maxAmount - GeneralUtils.countLayered(infusedThisCombat, Chemister.Flasks.AER);
                }

                @Override
                public DisplayableAction createAction(int amt) {
                    return new ScryDisplayAction(2);
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
