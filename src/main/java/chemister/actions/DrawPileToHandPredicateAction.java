package chemister.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.function.Predicate;

public class DrawPileToHandPredicateAction extends AbstractGameAction {
    private final Predicate<AbstractCard> predicate;

    public DrawPileToHandPredicateAction(int amount, Predicate<AbstractCard> predicate) {
        this.amount = amount;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.FAST_MODE ? Settings.ACTION_DUR_FASTER : Settings.ACTION_DUR_FAST;
        this.predicate = predicate;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            AbstractPlayer p = AbstractDungeon.player;

            if (p.drawPile.isEmpty()) {
                this.isDone = true;
                return;
            }

            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

            for (AbstractCard card : p.drawPile.group) {
                if (predicate.test(card)) {
                    tmp.addToRandomSpot(card);
                }
            }

            if (tmp.isEmpty()) {
                this.isDone = true;
                return;
            }

            AbstractCard card;
            for(int i = 0; i < this.amount; ++i) {
                if (!tmp.isEmpty()) {
                    tmp.shuffle();
                    card = tmp.getBottomCard();
                    tmp.removeCard(card);
                    if (p.hand.size() >= BaseMod.MAX_HAND_SIZE) {
                        p.drawPile.moveToDiscardPile(card);
                        p.createHandIsFullDialog();
                    } else {
                        card.unhover();
                        card.lighten(true);
                        card.setAngle(0.0F);
                        card.drawScale = 0.12F;
                        card.targetDrawScale = 0.75F;
                        card.current_x = CardGroup.DRAW_PILE_X;
                        card.current_y = CardGroup.DRAW_PILE_Y;
                        p.drawPile.removeCard(card);
                        p.hand.addToTop(card);
                        p.hand.refreshHandLayout();
                        p.hand.applyPowers();
                    }
                }
            }

            this.isDone = true;
        }

        this.tickDuration();
    }
}
