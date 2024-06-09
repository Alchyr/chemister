package chemister.relics;

import chemister.actions.infuse.InfuseAction;
import chemister.character.Chemister;
import chemister.relics.starter.FlaskRelic;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.HashSet;
import java.util.Set;

import static chemister.ChemisterMod.makeID;

public class PerfectedElixir extends BaseRelic implements InfuseAction.PostInfuseTrigger {
    private static final String NAME = PerfectedElixir.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private final Set<Chemister.Flasks> infused = new HashSet<>();

    public PerfectedElixir() {
        super(ID, NAME, Chemister.Meta.CARD_COLOR, RelicTier.BOSS, LandingSound.MAGICAL);
        counter = 0;
    }

    public void atTurnStart() {
        counter = 0;
        infused.clear();
    }

    @Override
    public void onVictory() {
        counter = 0;
        infused.clear();
    }

    @Override
    public void postInfusion(FlaskRelic flask) {
        if (infused.add(flask.flaskType())) {
            ++counter;
            if (counter == Chemister.Flasks.values().length) {
                flash();
                addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                addToBot(new GainEnergyAction(1));
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
