package chemister.cards.uncommon;

import chemister.cards.WithdrawalCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class AlacritousArrangement extends WithdrawalCard {
    public static final String ID = makeID(AlacritousArrangement.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            2
    );

    public AlacritousArrangement() {
        super(ID, info);

        setMagic(4, 1);
    }

    @Override
    public float getTitleFontSize() {
        return 20f;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!infusedThisTurn()) {
            queueWithdrawalEffect(p, m);
        }
        else {
            drawCards(magicNumber);
        }
    }

    @Override
    public void withdrawalEffect(AbstractPlayer p, AbstractMonster m) {
        drawCards(magicNumber, new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                for (AbstractCard c : DrawCardAction.drawnCards) {
                    if (c instanceof WithdrawalCard) {
                        c.setCostForTurn(c.costForTurn - 1);
                    }
                }
            }
        });
    }
}
