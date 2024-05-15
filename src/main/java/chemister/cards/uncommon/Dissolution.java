package chemister.cards.uncommon;

import chemister.cards.WithdrawalCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Dissolution extends WithdrawalCard {
    public static final String ID = makeID(Dissolution.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            3
    );

    public Dissolution() {
        super(ID, info);

        setCostUpgrade(2);
        setDamage(28);
    }

    @Override
    public void applyPowers() {
        super.applyPowers();

        shuffleBackIntoDrawPile = !infusedThisTurn();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        damageSingle(m, AbstractGameAction.AttackEffect.SMASH);
    }

    @Override
    public void withdrawalEffect(AbstractPlayer p, AbstractMonster m) {

    }
}
