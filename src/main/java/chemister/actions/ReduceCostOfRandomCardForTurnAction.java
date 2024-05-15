package chemister.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.List;

public class ReduceCostOfRandomCardForTurnAction extends AbstractGameAction {
    public ReduceCostOfRandomCardForTurnAction(int amt) {
        this.amount = amt;
        this.startDuration = Settings.ACTION_DUR_FASTER;
        this.duration = this.startDuration;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (AbstractDungeon.player.hand.isEmpty()) {
                isDone = true;
                return;
            }

            List<AbstractCard> validOptions = new ArrayList<>();
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if (c.costForTurn > 0 && c.cost > 0) validOptions.add(c);
            }

            if (validOptions.isEmpty()) {
                for (AbstractCard c : AbstractDungeon.player.hand.group) {
                    if (c.costForTurn > 0) validOptions.add(c);
                }

                if (validOptions.isEmpty()) {
                    isDone = true;
                    return;
                }
            }

            AbstractCard targetCard = validOptions.get(AbstractDungeon.cardRng.random(validOptions.size() - 1));
            targetCard.setCostForTurn(targetCard.costForTurn - this.amount);

        }

        tickDuration();
        if (Settings.FAST_MODE) {
            this.isDone = true;
        }
    }
}