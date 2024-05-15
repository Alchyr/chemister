package chemister.cards.common;

import chemister.cards.BaseCard;
import chemister.cards.InfuseCard;
import chemister.cards.WithdrawalCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Arrays;

public class TripleThreat extends WithdrawalCard implements InfuseCard {
    public static final String ID = makeID(TripleThreat.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1
    );

    private static final Chemister.Flasks[] withdrawalFlask = new Chemister.Flasks[] {
            Chemister.Flasks.IGNIS
    };
    private static final Chemister.Flasks[] none = new Chemister.Flasks[] {};
    private Chemister.Flasks[] flasks = withdrawalFlask;

    public TripleThreat() {
        super(ID, info);

        setBlock(4, 2);
        setDamage(4, 2);
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        int amt = withdrawalEffectCount();
        if (flasks.length != amt) {
            if (amt == withdrawalFlask.length) {
                flasks = withdrawalFlask;
            }
            else {
                flasks = new Chemister.Flasks[withdrawalEffectCount()];
                Arrays.fill(flasks, Chemister.Flasks.IGNIS);
            }
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        block();
        damageSingle(m, AbstractGameAction.AttackEffect.FIRE);
        if (!infusedThisTurn()) {
            queueWithdrawalEffect(p, m);
        }
    }

    @Override
    public void withdrawalEffect(AbstractPlayer p, AbstractMonster m) {
        infuse(Chemister.Flasks.IGNIS);
    }

    @Override
    public boolean specialRender(SpriteBatch sb) {
        if (infusedThisTurn()) return false;
        return renderInfuseEffects(this, true, sb);
    }

    @Override
    public Chemister.Flasks[] getFlasks() {
        return !infusedThisTurn() ? flasks : none;
    }
}
