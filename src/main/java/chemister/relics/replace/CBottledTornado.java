package chemister.relics.replace;

import basemod.abstracts.CustomBottleRelic;
import basemod.abstracts.CustomSavable;
import chemister.actions.infuse.InfuseAction;
import chemister.character.Chemister;
import chemister.patches.CBottledTornadoField;
import chemister.relics.BaseRelic;
import chemister.relics.starter.FlaskRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.BottledTornado;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.function.Predicate;

import static chemister.ChemisterMod.makeID;

public class CBottledTornado extends BaseRelic implements CustomBottleRelic, CustomSavable<Integer>, InfuseAction.PostInfuseTrigger {
    private static final String NAME = CBottledTornado.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private static final String copyID = BottledTornado.ID;
    static {
        RelicStrings base = CardCrawlGame.languagePack.getRelicStrings(copyID);
        RelicStrings copy = CardCrawlGame.languagePack.getRelicStrings(ID);

        copy.NAME = base.NAME;
        copy.FLAVOR = base.FLAVOR;
    }


    private boolean cardSelected = true;
    private AbstractCard card = null;

    private boolean triggered = false;

    public CBottledTornado() {
        super(ID, "bottledTornado.png", Chemister.Meta.CARD_COLOR, RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public void atPreBattle() {
        triggered = false;
        beginLongPulse();
    }

    @Override
    public void postInfusion(FlaskRelic flask) {
        if (!triggered && flask.flaskType() == Chemister.Flasks.AER) {
            triggered = true;
            stopPulse();
            flash();

            addToTop(new AbstractGameAction() {
                @Override
                public void update() {
                    AbstractCard inBottle = null;
                    for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
                        if (CBottledTornadoField.inCBottledTornado.get(c)) {
                            inBottle = c;
                            break;
                        }
                    }

                    if (inBottle != null) {
                        AbstractDungeon.player.drawPile.group.remove(inBottle);
                        AbstractDungeon.player.drawPile.group.add(inBottle);
                        addToBot(new DrawCardAction(1));
                    }
                    isDone = true;
                }
            });
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public Predicate<AbstractCard> isOnCard()
    {
        return CBottledTornadoField.inCBottledTornado::get;
    }

    public AbstractCard getCard()
    {
        return card.makeCopy();
    }

    @Override
    public Integer onSave()
    {
        return AbstractDungeon.player.masterDeck.group.indexOf(card);
    }

    @Override
    public void onLoad(Integer cardIndex)
    {
        if (cardIndex == null) {
            return;
        }
        if (cardIndex >= 0 && cardIndex < AbstractDungeon.player.masterDeck.group.size()) {
            card = AbstractDungeon.player.masterDeck.group.get(cardIndex);
            if (card != null) {
                CBottledTornadoField.inCBottledTornado.set(card, true);
                setDescriptionAfterLoading();
            }
        }
    }

    @Override
    public void onEquip()
    {
        cardSelected = false;
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
        AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck),
                1, DESCRIPTIONS[1] + name + ".",
                false, false, false, false);
    }

    @Override
    public void onUnequip()
    {
        if (card != null) {
            AbstractCard cardInDeck = AbstractDungeon.player.masterDeck.getSpecificCard(card);
            if (cardInDeck != null) {
                CBottledTornadoField.inCBottledTornado.set(cardInDeck, false);
            }
        }
    }

    @Override
    public void update()
    {
        super.update();

        if (!cardSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            cardSelected = true;
            card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            CBottledTornadoField.inCBottledTornado.set(card, true);
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            setDescriptionAfterLoading();
        }
    }

    private void setDescriptionAfterLoading()
    {
        description = FontHelper.colorString(card.name, "y") + DESCRIPTIONS[2];
        tips.clear();
        tips.add(new PowerTip(name, description));
        initializeTips();
    }
}
