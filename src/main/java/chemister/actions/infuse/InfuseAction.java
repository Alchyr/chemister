package chemister.actions.infuse;

import chemister.ChemisterMod;
import chemister.cards.CatalystCard;
import chemister.character.Chemister;
import chemister.powers.FlaskFracturePower;
import chemister.powers.SneakySipPower;
import chemister.relics.starter.FlaskRelic;
import chemister.util.GeneralUtils;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class InfuseAction extends AbstractGameAction implements DisplayableAction {
    public final Chemister.Flasks flask;

    private FlaskRelic flaskRelic = null;
    public final boolean canChain;

    public InfuseAction(Chemister.Flasks flask, boolean isChained) {
        this.flask = flask;
        this.canChain = !isChained;

        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof FlaskRelic) {
                if (((FlaskRelic) r).flaskType() == flask) {
                    flaskRelic = (FlaskRelic) r;
                    break;
                }
            }
        }

        amount = 0;
    }

    public InfuseAction(Chemister.Flasks flask) {
        this(flask, false);
    }

    @Override
    public void update() {
        AbstractPower p = AbstractDungeon.player.getPower(SneakySipPower.POWER_ID);
        SneakySipPower sneaky = null;
        FlaskFracturePower fracture = null;

        if (p instanceof SneakySipPower)
            sneaky = (SneakySipPower) p;

        p = AbstractDungeon.player.getPower(FlaskFracturePower.POWER_ID);
        if (p instanceof FlaskFracturePower) {
            fracture = (FlaskFracturePower) p;
        }

        if (fracture != null && fracture.amount > 0) {
            fracture.onSpecificTrigger();
            isDone = true;
            return;
        }

        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof FlaskRelic) {
                if (((FlaskRelic) r).flaskType() == flask) {
                    r.flash();
                    ((FlaskRelic) r).infuse(canChain);

                    if (sneaky == null || !sneaky.preventInfuseCount()) {
                        ++ChemisterMod.infusedCountThisTurn;
                    }

                    ChemisterMod.infusedTypesThisTurn.add(flask);
                    /*if (ChemisterMod.infusedTurnHistory.isEmpty() ||
                            !ChemisterMod.infusedTurnHistory.get(ChemisterMod.infusedTurnHistory.size() - 1).equals(ChemisterMod.infusedTypesThisTurn)) {
                        ChemisterMod.infusedTurnHistory.add(ChemisterMod.infusedTypesThisTurn);
                    }*/

                    GeneralUtils.forAllInCombatCards(
                            (card)->{
                                if (card instanceof CatalystCard) {
                                    if (GeneralUtils.arrContains(((CatalystCard) card).getFlasks(), flask)) {
                                        card.modifyCostForCombat(CatalystCard.getDiscount());
                                    }
                                }
                            }, true
                    );

                    for (AbstractPower pow : AbstractDungeon.player.powers) {
                        if (pow instanceof PostInfusePower) {
                            ((PostInfusePower) pow).postInfusion((FlaskRelic) r);
                        }
                    }
                    break;
                }
            }
        }

        isDone = true;
    }

    @Override
    public AbstractGameAction getAction() {
        return this;
    }

    @Override
    public TextureRegion getIcon() {
        return flaskRelic.imgRegion;
    }

    @Override
    public float getIconScale() {
        return 0.75f;
    }

    @Override
    public boolean merge(DisplayableAction action) {
        return false;
    }

    public interface PostInfusePower {
        void postInfusion(FlaskRelic flask);
    }
}
