package chemister.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class EffervescenceAction extends AbstractGameAction {
    private final boolean tryReshuffle;

    public EffervescenceAction(int amount, boolean tryReshuffle) {
        this.amount = amount;
        this.tryReshuffle = tryReshuffle;
    }

    @Override
    public void update() {
        isDone = true;

        if (AbstractDungeon.player.drawPile.size() + AbstractDungeon.player.discardPile.size() == 0) {
            return;
        }

        if (AbstractDungeon.player.drawPile.isEmpty()) {
            if (tryReshuffle) {
                addToTop(new EffervescenceAction(amount, false));
                addToTop(new EmptyDeckShuffleAction());
            }
            return;
        }

        AbstractCard c = AbstractDungeon.player.drawPile.getTopCard();
        for (int i = 0; i < amount; ++i) {
            addToTop(new WaitAction(0.1f));
            addToTop(new PlayCardAction(c.makeSameInstanceOf(), null, true));
        }
    }
}
