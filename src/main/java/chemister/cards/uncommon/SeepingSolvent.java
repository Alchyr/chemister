package chemister.cards.uncommon;

import basemod.helpers.CardModifierManager;
import chemister.actions.infuse.CatalystDiscountDisplayAction;
import chemister.actions.infuse.DisplayableAction;
import chemister.cardmods.TemporaryCardmod;
import chemister.cards.ReagentCard;
import chemister.character.Chemister;
import chemister.relics.starter.FlaskRelic;
import chemister.util.CardStats;
import chemister.util.GeneralUtils;
import com.megacrit.cardcrawl.helpers.PowerTip;

import java.util.List;

public class SeepingSolvent extends ReagentCard {
    public static final String ID = makeID(SeepingSolvent.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            -2
    );

    private static final Chemister.Flasks[] flasks = new Chemister.Flasks[] {
            Chemister.Flasks.AQUA
    };

    public SeepingSolvent() {
        super(ID, info);

        CardModifierManager.addModifier(this, new TemporaryCardmod(4, true));
    }

    @Override
    public Chemister.Flasks[] getFlasks() {
        return flasks;
    }

    @Override
    public ReagentEffect getEffect(FlaskRelic flask) {
        if (flask.flaskType() == Chemister.Flasks.AQUA) {
            return new ReagentEffect(ID, 4, 1) {
                @Override
                protected int getAmount(int infusedCount, List<Chemister.Flasks> infusedThisTurn, List<List<Chemister.Flasks>> infusedThisCombat) {
                    return maxAmount - GeneralUtils.count(infusedThisTurn, Chemister.Flasks.AQUA);
                }

                @Override
                public DisplayableAction createAction(int amt) {
                    return new CatalystDiscountDisplayAction(2);
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
