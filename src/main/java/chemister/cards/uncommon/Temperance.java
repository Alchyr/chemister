package chemister.cards.uncommon;

import chemister.actions.PlayCardAction;
import chemister.cards.WithdrawalCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import chemister.util.GeneralUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.function.Predicate;

public class Temperance extends WithdrawalCard {
    public static final String ID = makeID(Temperance.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            1
    );

    public Temperance() {
        super(ID, info);

        setCostUpgrade(0);
        setMagic(2);

        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ConditionalPlayTopCardAction((card)->GeneralUtils.getLogicalCardCost(card) <= magicNumber));
        if (!infusedThisTurn()) {
            queueWithdrawalEffect(p, m);
        }
    }

    @Override
    public void withdrawalEffect(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ConditionalPlayTopCardAction((card)->GeneralUtils.getLogicalCardCost(card) <= magicNumber));
    }

    private static class ConditionalPlayTopCardAction extends AbstractGameAction {
        private final Predicate<AbstractCard> condition;
        public ConditionalPlayTopCardAction(Predicate<AbstractCard> condition) {
            this.condition = condition;
        }

        @Override
        public void update() {
            isDone = true;
            if (AbstractDungeon.player.drawPile.isEmpty()) return;
            if (!condition.test(AbstractDungeon.player.drawPile.getTopCard())) return;

            addToTop(new PlayCardAction(AbstractDungeon.player.drawPile.getTopCard(), AbstractDungeon.player.drawPile, false, false));
        }
    }
}
