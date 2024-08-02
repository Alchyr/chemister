package chemister.cardmods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class EndTurnIfAllPlayedModifier extends AbstractCardModifier {
    private final ArrayList<AbstractCard> cards;

    public EndTurnIfAllPlayedModifier(ArrayList<AbstractCard> cards) {
        this.cards = new ArrayList<>(cards);
    }

    @Override
    public boolean removeOnCardPlayed(AbstractCard card) {
        return true;
    }

    @Override
    public boolean removeAtEndOfTurn(AbstractCard card) {
        return true;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        int size = cards.size();
        for (CardQueueItem queuedCard : AbstractDungeon.actionManager.cardQueue) {
            if (!card.equals(queuedCard.card))
                cards.remove(queuedCard.card);
        }

        if (cards.size() == size) {
            addToBot(new PressEndTurnButtonAction());
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new EndTurnIfAllPlayedModifier(cards);
    }
}
