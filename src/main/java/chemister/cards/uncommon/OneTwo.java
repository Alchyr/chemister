package chemister.cards.uncommon;

import chemister.cards.BaseCard;
import chemister.cards.WithdrawalCard;
import chemister.character.Chemister;
import chemister.patches.NoUseVigorPatch;
import chemister.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class OneTwo extends BaseCard {
    public static final String ID = makeID(OneTwo.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1
    );

    public OneTwo() {
        super(ID, info);

        setDamage(8, 4);
        tags.add(NoUseVigorPatch.CHEMISTER_NO_VIGOR_USE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        damageSingle(m, damage <= 12 ? AbstractGameAction.AttackEffect.BLUNT_LIGHT : AbstractGameAction.AttackEffect.BLUNT_HEAVY);
    }
}
