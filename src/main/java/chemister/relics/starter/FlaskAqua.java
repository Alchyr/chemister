package chemister.relics.starter;

import chemister.actions.infuse.DisplayableAction;
import chemister.actions.infuse.DrawDisplayAction;
import chemister.character.Chemister;
import chemister.infuse.FlaskBaseEffect;
import chemister.infuse.InfuseEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static chemister.ChemisterMod.makeID;

public class FlaskAqua extends FlaskRelic {
    private static final String NAME = "FlaskAqua";
    public static final String ID = makeID(NAME);

    private final List<Consumer<List<AbstractCard>>> drawFollowup = new ArrayList<>();
    public void addDrawFollowup(Consumer<List<AbstractCard>> followup) {
        drawFollowup.add(followup);
    }

    private AbstractGameAction getDrawFollowup() {
        List<Consumer<List<AbstractCard>>> finalConsumers = new ArrayList<>(drawFollowup);
        drawFollowup.clear();

        for (InfuseEffect effect : infuseEffects) {
            if (effect instanceof AquaFollowup) {
                Consumer<List<AbstractCard>> followup = ((AquaFollowup) effect).getFollowup();
                if (followup != null)
                    finalConsumers.add(followup);
            }
        }

        return new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                for (Consumer<List<AbstractCard>> followup : finalConsumers) {
                    followup.accept(DrawCardAction.drawnCards);
                }
            }
        };
    }

    public interface AquaFollowup {
        Consumer<List<AbstractCard>> getFollowup();
    }


    private final FlaskBaseEffect effect = new FlaskBaseEffect(this, "DRAW") {
        @Override
        public DisplayableAction createAction(int amt) {
            return new DrawDisplayAction(amt, getDrawFollowup());
        }
    };





    public FlaskAqua() {
        super(ID, NAME, Chemister.Meta.CARD_COLOR, RelicTier.STARTER, LandingSound.CLINK);

        counter = 1;
        refreshDescriptionAndTips();
    }

    @Override
    public void atPreBattle() {
        super.atPreBattle();
        drawFollowup.clear();
    }

    @Override
    public void infuse(boolean canChain) {
        super.infuse(canChain);
        drawFollowup.clear();
    }

    @Override
    public void resetCounter() {
        counter = 1;
    }

    @Override
    public Chemister.Flasks flaskType() {
        return Chemister.Flasks.AQUA;
    }

    @Override
    public InfuseEffect getBaseEffect() {
        return effect;
    }

    @Override
    public String getUpdatedDescription() {
        if (counter == 1) {
            return DESCRIPTIONS[0] + counter + DESCRIPTIONS[1];
        }
        else {
            return DESCRIPTIONS[0] + counter + DESCRIPTIONS[2];
        }
    }
}
