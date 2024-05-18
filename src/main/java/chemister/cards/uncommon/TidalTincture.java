package chemister.cards.uncommon;

import chemister.ChemisterMod;
import chemister.actions.infuse.DisplayableAction;
import chemister.cards.ReagentCard;
import chemister.character.Chemister;
import chemister.relics.starter.FlaskRelic;
import chemister.util.CardStats;
import chemister.util.GeneralUtils;
import com.megacrit.cardcrawl.helpers.PowerTip;

import java.util.List;

public class TidalTincture extends ReagentCard {
    public static final String ID = makeID(TidalTincture.class.getSimpleName());
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

    public TidalTincture() {
        super(ID, info);
    }

    @Override
    public Chemister.Flasks[] getFlasks() {
        return flasks;
    }

    @Override
    public ReagentEffect getEffect(FlaskRelic flask) {
        if (flask.flaskType() == Chemister.Flasks.AQUA) {
            return new ReagentEffect(ID, -1, 1) {
                @Override
                protected int getAmount(int infusedCount, List<Chemister.Flasks> infusedThisTurn, List<List<Chemister.Flasks>> infusedThisCombat) {
                    return maxAmount - GeneralUtils.countLayered(infusedThisCombat, Chemister.Flasks.AQUA);
                }

                @Override
                public DisplayableAction createAction(int amt) {
                    return null;
                }

                @Override
                public void applyValueChange(FlaskRelic flaskRelic) {
                    if (getAmount(ChemisterMod.infusedCountThisTurn, ChemisterMod.infusedTypesThisTurn, ChemisterMod.infusedTurnHistory) > 0) {
                        flaskRelic.counter += 1;
                    }
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
