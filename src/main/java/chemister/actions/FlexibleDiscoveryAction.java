package chemister.actions;

import basemod.BaseMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import java.util.ArrayList;
import java.util.function.Consumer;

public class FlexibleDiscoveryAction extends AbstractGameAction {
    private boolean retrieveCard = false;
    private final ArrayList<AbstractCard> cards;
    private boolean costsZeroThisTurn;
    private Consumer<AbstractCard> callback;
    private AbstractCardModifier cardModifier;
    private boolean skippable;

    public FlexibleDiscoveryAction(ArrayList<AbstractCard> cards, boolean costsZeroThisTurn) {
        this(cards, 1, costsZeroThisTurn, false, null, null);
    }


    public FlexibleDiscoveryAction(ArrayList<AbstractCard> cards, int amount, boolean costsZeroThisTurn) {
        this(cards, amount, costsZeroThisTurn, false, null, null);
    }

    public FlexibleDiscoveryAction(ArrayList<AbstractCard> cards, boolean costsZeroThisTurn, AbstractCardModifier cardModifier) {
        this(cards, 1, costsZeroThisTurn, false, null, cardModifier);
    }

    public FlexibleDiscoveryAction(ArrayList<AbstractCard> cards, Consumer<AbstractCard> callback, boolean costsZeroThisTurn) {
        this(cards, 1, costsZeroThisTurn, false, callback, null);
    }
    public FlexibleDiscoveryAction(ArrayList<AbstractCard> cards, boolean costsZeroThisTurn, boolean skippable, AbstractCardModifier cardModifier)
    {
        this(cards, 1, costsZeroThisTurn, skippable,null,  cardModifier);
    }

    public FlexibleDiscoveryAction(ArrayList<AbstractCard> cards, int amount, boolean costsZeroThisTurn, boolean skippable,  Consumer<AbstractCard> callback, AbstractCardModifier cardModifier) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.cards = cards;
        this.amount = amount;
        this.costsZeroThisTurn = costsZeroThisTurn;
        this.cardModifier = cardModifier;
        this.callback = callback;
        this.skippable = skippable;
    }


    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.cardRewardScreen.customCombatOpen(cards, CardRewardScreen.TEXT[1], skippable);
            AbstractDungeon.cardRewardScreen.rewardGroup = cards;

            for (AbstractCard tmp : AbstractDungeon.cardRewardScreen.rewardGroup) {
                UnlockTracker.markCardAsSeen(tmp.cardID);
            }
            this.tickDuration();
        } else {
            if (!this.retrieveCard) {
                if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
                    AbstractCard disCard = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
                    if (callback != null)
                    {
                        callback.accept(disCard);
                    }
                    if (costsZeroThisTurn) {
                        disCard.setCostForTurn(0);
                    }
                    if (cardModifier!=null){
                        CardModifierManager.addModifier(disCard, cardModifier);
                    }
                    disCard.current_x = -1000.0F * Settings.scale;

                    if (amount == 1) {
                        if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
                            AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(disCard, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        } else {
                            AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(disCard, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        }
                    }
                    else {

                        if (AbstractDungeon.player.hand.size() + this.amount <= BaseMod.MAX_HAND_SIZE) {
                            AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(disCard, (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));

                            for (int i = 1; i < amount; ++i) {
                                AbstractCard disCard2 = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
                                AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(disCard2, (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                            }
                        } else if (AbstractDungeon.player.hand.size() == BaseMod.MAX_HAND_SIZE - 1) {
                            AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(disCard, (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                            for (int i = 1; i < amount; ++i) {
                                AbstractCard disCard2 = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
                                AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(disCard2, (float) Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                            }
                        } else {
                            AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(disCard, (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                            for (int i = 1; i < amount; ++i) {
                                disCard = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
                                AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(disCard, (float)Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                            }
                        }
                    }

                    AbstractDungeon.cardRewardScreen.discoveryCard = null;
                }

                this.retrieveCard = true;
            }

            this.tickDuration();
        }
    }
}