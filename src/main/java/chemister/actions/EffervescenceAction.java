package chemister.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class EffervescenceAction extends AbstractGameAction {
    private final boolean tryReshuffle;
    private final boolean realPlay;

    public EffervescenceAction(int amount, boolean realPlay, boolean tryReshuffle) {
        this.amount = amount;
        this.realPlay = realPlay;
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
                addToTop(new EffervescenceAction(amount, realPlay, false));
                addToTop(new EmptyDeckShuffleAction());
            }
            return;
        }

        AbstractCard c = AbstractDungeon.player.drawPile.getTopCard();

        for (int i = realPlay ? 1 : 0; i < amount; ++i) {
            addToTop(new PlayCardAction(c.makeSameInstanceOf(), null, amount > 1, true));
        }
        if (realPlay) {
            addToTop(new PlayCardAction(c, AbstractDungeon.player.drawPile, amount > 1, true));
        }
    }
}
