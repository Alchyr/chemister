package chemister.cards.uncommon;

import chemister.cards.WithdrawalCard;
import chemister.character.Chemister;
import chemister.powers.BefuddlingBrewPower;
import chemister.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BefuddlingBrew extends WithdrawalCard {
    public static final String ID = makeID(BefuddlingBrew.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.ALL,
            1
    );

    public BefuddlingBrew() {
        super(ID, info);

        setBlock(6, 2);
        setMagic(1, 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        block();
        if (!infusedThisTurn()) {
            queueWithdrawalEffect(p, m);
        }
        else {
            applySelf(new BefuddlingBrewPower(p, this.magicNumber));
        }
    }

    @Override
    public void withdrawalEffect(AbstractPlayer p, AbstractMonster m) {
        applyAll((mo)->getWeak(mo, magicNumber), true, AbstractGameAction.AttackEffect.NONE);
    }
}
