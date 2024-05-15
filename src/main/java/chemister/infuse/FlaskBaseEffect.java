package chemister.infuse;

import chemister.character.Chemister;
import chemister.relics.starter.FlaskRelic;

import java.util.List;

public abstract class FlaskBaseEffect extends InfuseEffect {
    private final FlaskRelic source;

    public FlaskBaseEffect(FlaskRelic r, String ID) {
        super(ID, 0);
        this.source = r;
    }

    @Override
    protected int getAmount(int infusedCount, List<Chemister.Flasks> infusedThisTurn, List<List<Chemister.Flasks>> infusedThisCombat) {
        return source.counter + (child != null ? child.getAmount(infusedCount, infusedThisTurn, infusedThisCombat) : 0);
    }
}
