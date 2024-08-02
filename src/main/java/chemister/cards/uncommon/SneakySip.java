package chemister.cards.uncommon;

import chemister.actions.DrawPileToHandPredicateAction;
import chemister.cards.BaseCard;
import chemister.cards.WithdrawalCard;
import chemister.character.Chemister;
import chemister.powers.SneakySipPower;
import chemister.util.CardStats;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SneakySip extends BaseCard {
    public static final String ID = makeID(SneakySip.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            0
    );

    public SneakySip() {
        super(ID, info);

        //setMagic(2, 1);
        setMagic(0, 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawPileToHandPredicateAction(1, (card)->card instanceof WithdrawalCard));
        if (magicNumber > 0) {
            applySelf(new SneakySipPower(p, magicNumber));
        }
    }
}
