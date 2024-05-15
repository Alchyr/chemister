package chemister.cards.common;

import chemister.cards.WithdrawalCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DoubleDisplacement extends WithdrawalCard {
    public static final String ID = makeID(DoubleDisplacement.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1
    );

    public DoubleDisplacement() {
        super(ID, info);

        setDamage(6, 3);
    }

    @Override
    public float getTitleFontSize() {
        return 20f;
    }

    @Override
    public void applyPowers() {
        if (infusedThisTurn()) {
            this.name = cardStrings.EXTENDED_DESCRIPTION[0];
        }
        else {
            this.name = cardStrings.NAME;
        }
        super.applyPowers();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        damageSingle(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        if (!infusedThisTurn()) {
            queueWithdrawalEffect(p, m);
        }
    }

    @Override
    public void withdrawalEffect(AbstractPlayer p, AbstractMonster m) {
        damageSingle(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
    }
}
