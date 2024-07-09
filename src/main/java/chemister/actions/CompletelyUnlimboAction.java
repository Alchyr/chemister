package chemister.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class CompletelyUnlimboAction extends AbstractGameAction {
    private final AbstractCard card;

    public CompletelyUnlimboAction(AbstractCard c) {
        this.card = c;
    }

    @Override
    public void update() {
        isDone = true;
        while (AbstractDungeon.player.limbo.group.remove(card));
    }
}
