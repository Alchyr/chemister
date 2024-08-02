package chemister.cards.uncommon;

import chemister.cards.InfuseCard;
import chemister.cards.WithdrawalCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ThinkFast extends WithdrawalCard implements InfuseCard {
    public static final String ID = makeID(ThinkFast.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            0
    );

    private static final Chemister.Flasks[] withdrawalFlask = new Chemister.Flasks[] {
            Chemister.Flasks.AQUA,
            Chemister.Flasks.IGNIS
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
        if (flasks.length != amt * withdrawalFlask.length) {
            if (amt == 1) {
                flasks = withdrawalFlask;
            }
            else {
                flasks = new Chemister.Flasks[amt * withdrawalFlask.length];
                for (int i = 0; i < flasks.length; ++i) {
                    flasks[i] = withdrawalFlask[i % withdrawalFlask.length];
                }
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
        for (Chemister.Flasks flask : withdrawalFlask) {
            infuse(flask);
        }
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
