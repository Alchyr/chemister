package chemister.infuse;

import chemister.character.Chemister;
import chemister.actions.infuse.DisplayableAction;
import chemister.relics.starter.FlaskRelic;

import java.util.List;

public abstract class InfuseEffect {
    public final String ID;
    private final int amount;

    public int priority = 0; //default, reagent order

    protected InfuseEffect child;

    public InfuseEffect(String ID, int amount) {
        this.ID = ID;
        this.amount = amount;
    }

    public abstract DisplayableAction createAction(int amt);

    public DisplayableAction getAction(List<DisplayableAction> actions, int infusedCount, List<Chemister.Flasks> infusedThisTurn, List<List<Chemister.Flasks>> infusedThisCombat) {
        int amt = getAmount(infusedCount, infusedThisTurn, infusedThisCombat);
        if (amt <= 0)
            return null;
        return createAction(amt);
    }

    protected int getAmount(int infusedCount, List<Chemister.Flasks> infusedThisTurn, List<List<Chemister.Flasks>> infusedThisCombat) {
        return amount + (child != null ? child.getAmount(infusedCount, infusedThisTurn, infusedThisCombat) : 0);
    }

    public InfuseEffect merge(InfuseEffect otherEffect) {
        if (child == null) {
            child = otherEffect;
        }
        else {
            child.merge(otherEffect);
        }
        return this;
    }

    //Modify relic's counter, if applicable
    public void applyValueChange(FlaskRelic flaskRelic) {
    }
}
