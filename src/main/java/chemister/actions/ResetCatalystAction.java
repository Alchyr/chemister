package chemister.actions;

import chemister.ChemisterMod;
import chemister.cards.CatalystCard;
import chemister.util.GeneralUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class ResetCatalystAction extends AbstractGameAction {
    private final CatalystCard c;

    public ResetCatalystAction(CatalystCard c) {
        this.c = c;
    }

    @Override
    public void update() {
        GeneralUtils.forAllInCombatCards(
                (card)->{
                    if (card instanceof CatalystCard && card.cardID.equals(c.cardID)) {
                        int newCost = ((CatalystCard) card).getBaseCost();
                        if (card.cost >= 0 && card.cost != newCost) { //Avoid messing with cards that somehow have their cost made negative.
                            card.cost = newCost;
                            card.costForTurn = card.cost;
                            card.isCostModified = false;
                        }
                        card.freeToPlayOnce = false;
                    }
                }, false
        );

        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;

                int newCost = c.getBaseCost();
                if (c.cost >= 0 && c.cost != newCost) {
                    c.cost = newCost;
                    c.costForTurn = c.cost;
                    c.isCostModified = false;
                }
                c.freeToPlayOnce = false;
            }
        });

        isDone = true;
    }
}
