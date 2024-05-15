package chemister.cards.uncommon;

import chemister.cards.BaseCard;
import chemister.cards.WithdrawalCard;
import chemister.character.Chemister;
import chemister.powers.SneakySipPower;
import chemister.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
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

        setMagic(2, 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applySelf(new SneakySipPower(p, magicNumber));
    }
}
