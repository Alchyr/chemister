package chemister.cards.rare;

import chemister.cards.BaseCard;
import chemister.character.Chemister;
import chemister.powers.DelayedInsolubilityPower;
import chemister.util.CardStats;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;

public class Insolubility extends BaseCard {
    public static final String ID = makeID(Insolubility.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            1
    );


    public Insolubility() {
        super(ID, info);

        setMagic(2, 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applySelf(new DelayedInsolubilityPower(p, 1));
        applySelf(new DrawCardNextTurnPower(p, magicNumber));
    }
}
