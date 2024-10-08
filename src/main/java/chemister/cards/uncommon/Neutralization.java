package chemister.cards.uncommon;

import chemister.cards.BaseCard;
import chemister.character.Chemister;
import chemister.powers.TripleThreatDebuff;
import chemister.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Neutralization extends BaseCard {
    public static final String ID = makeID(Neutralization.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1
    );

    public Neutralization() {
        super(ID, info);

        setDamage(7, 2);
        setMagic(2, 1);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        damageSingle(m, AbstractGameAction.AttackEffect.FIRE);
        applySingle(m, new TripleThreatDebuff(m, p, magicNumber));
    }
}
