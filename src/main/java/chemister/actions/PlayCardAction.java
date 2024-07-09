package chemister.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class PlayCardAction extends AbstractGameAction {
    private boolean exhaustCards;
    private AbstractCard card;
    private CardGroup sourceGroup;

    public PlayCardAction(AbstractCard cardToUse, AbstractCard originalCard, boolean randomPosition, CardGroup source, boolean exhausts) {
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.WAIT;
        this.source = AbstractDungeon.player;
        this.card = cardToUse;

        this.sourceGroup = source;
        this.exhaustCards = exhausts;

        if (randomPosition) {
            card.current_x = card.target_x = MathUtils.random(0.1F, 0.9F) * (float)Settings.WIDTH;
            card.target_y = MathUtils.random(0.2F, 0.8F) * (float)Settings.HEIGHT;
            card.current_y = -500f;
            if (sourceGroup == null) {
                AbstractDungeon.player.limbo.addToBottom(card);
            }
        }
        else if (originalCard != null)
        {
            card.current_x = originalCard.current_x;
            card.current_y = originalCard.current_y;
        }
        else
        {
            card.current_x = card.target_x = (float)Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
            card.target_y = (float)Settings.HEIGHT / 2.0F;
            card.current_y = -500f;
            if (sourceGroup == null) {
                AbstractDungeon.player.limbo.addToBottom(card);
            }
        }
    }
    public PlayCardAction(AbstractCard cardToUse, CardGroup source, boolean randomPosition, boolean exhausts) {
        this(cardToUse, null, randomPosition, source, exhausts && (source != null));
    }

    public void update() {
        if (card == null || (sourceGroup != null && !sourceGroup.contains(card)))
        {
            this.isDone = true;
            return;
        }

        //card.freeToPlayOnce = true;
        card.exhaustOnUseOnce = this.exhaustCards && !(card.type == AbstractCard.CardType.POWER);

        AbstractDungeon.actionManager.addToTop(new UpdateHandAction());

        if (sourceGroup == null)
        {
            if (!AbstractDungeon.player.limbo.contains(card)) {
                card.target_x = (float)Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
                card.target_y = (float)Settings.HEIGHT / 2.0F;
                AbstractDungeon.player.limbo.addToTop(card);
            }

            card.purgeOnUse = true;
            AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(card, true, EnergyPanel.getCurrentEnergy(),true,true));
        } else {
            if (sourceGroup.type != CardGroup.CardGroupType.HAND)
            {
                sourceGroup.removeCard(card);
                AbstractDungeon.getCurrRoom().souls.remove(card);

                if (!AbstractDungeon.player.limbo.contains(card)) {
                    AbstractDungeon.player.limbo.addToTop(card);
                }

                card.target_x = (float)Settings.WIDTH / 2.0F + 200.0F * Settings.scale;
                card.target_y = (float)Settings.HEIGHT / 2.0F;
                card.targetAngle = 0.0F;
                card.lighten(false);
                card.targetDrawScale = 0.75F;


                card.applyPowers();
                this.addToTop(new NewQueueCardAction(card, true, false, true));
                this.addToTop(new CompletelyUnlimboAction(card));
                if (!Settings.FAST_MODE) {
                    this.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
                } else {
                    this.addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
                }
            }
            else
            {
                card.applyPowers();
                AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(card, true, EnergyPanel.getCurrentEnergy(), true, true));
                AbstractDungeon.actionManager.addToTop(new WaitAction(0.1F));
            }
        }


        this.isDone = true;
    }
}