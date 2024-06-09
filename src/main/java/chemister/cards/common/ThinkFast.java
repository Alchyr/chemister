package chemister.cards.common;

import chemister.cards.InfuseCard;
import chemister.cards.WithdrawalCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Arrays;

public class ThinkFast extends WithdrawalCard implements InfuseCard {
    public static final String ID = makeID(ThinkFast.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.NONE,
            0
    );

    private static final Chemister.Flasks[] withdrawalFlask = new Chemister.Flasks[] {
            Chemister.Flasks.AQUA
    };
    private Chemister.Flasks[] flasks = withdrawalFlask;

    public ThinkFast() {
        super(ID, info);

        setExhaust(true, false);
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
                Arrays.fill(flasks, Chemister.Flasks.AQUA);
            }
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!infusedThisTurn()) {
            queueWithdrawalEffect(p, m);
        }
        else {
            infuse();
        }
    }

    @Override
    public void withdrawalEffect(AbstractPlayer p, AbstractMonster m) {
        infuse(Chemister.Flasks.AQUA);
    }

    @Override
    public boolean specialRender(SpriteBatch sb) {
        return renderInfuseEffects(this, false, sb);
    }

    @Override
    public Chemister.Flasks[] getFlasks() {
        if (!infusedThisTurn()) {
            return flasks;
        }
        return Chemister.Flasks.values();
    }
}
