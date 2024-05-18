package chemister.cards.rare;

import chemister.cards.WithdrawalCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class TeetotalRejection extends WithdrawalCard {
    public static final String ID = makeID(TeetotalRejection.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ENEMY,
            1
    );


    public TeetotalRejection() {
        super(ID, info);

        setDamage(12);
        setMagic(2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        damageSingle(m, AbstractGameAction.AttackEffect.FIRE);

        if (upgraded || infusedThisTurn()) {
            applySingle(m, getWeak(m, magicNumber));
        }

        if (!infusedThisTurn()) {
            queueWithdrawalEffect(p, m);
        }
    }

    @Override
    public void withdrawalEffect(AbstractPlayer p, AbstractMonster m) {
        applySingle(m, getVuln(m, magicNumber));
    }
}
