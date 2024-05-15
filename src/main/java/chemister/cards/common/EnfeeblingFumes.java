package chemister.cards.common;

import chemister.cards.BaseCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class EnfeeblingFumes extends BaseCard {
    public static final String ID = makeID(EnfeeblingFumes.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1
    );

    public EnfeeblingFumes() {
        super(ID, info);

        setMagic(1);
        setBlock(4, 3);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applySingle(m, getWeak(m, magicNumber));
        block();
    }
}
