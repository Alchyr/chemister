package chemister.cards.rare;

import chemister.actions.ConsumptionAction;
import chemister.cards.BaseCard;
import chemister.cards.WithdrawalCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Consumption extends BaseCard {
    public static final String ID = makeID(Consumption.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ENEMY,
            1
    );


    public Consumption() {
        super(ID, info);

        setDamage(10, 4);
        setMagic(1);

        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ConsumptionAction(m, new DamageInfo(p, damage, damageTypeForTurn), magicNumber));
    }
}
