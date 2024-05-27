package chemister.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnLoseBlockPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static chemister.ChemisterMod.makeID;

public class CrystallizationPower extends BasePower implements OnLoseBlockPower {
    public static final String POWER_ID = makeID(CrystallizationPower.class.getSimpleName());
    private static final boolean TURN_BASED = true;

    public CrystallizationPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, TURN_BASED, owner, amount);
    }

    @Override
    public int onLoseBlock(DamageInfo damageInfo, int amt) {
        int blockLost = amt;
        if (blockLost > owner.currentBlock) blockLost = owner.currentBlock;

        if (blockLost > 0) {
            blockLost *= 2;
            this.flash();
            addToBot(new DamageRandomEnemyAction(new DamageInfo(this.owner, blockLost, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }

        return amt;
    }

    @Override
    public void atEndOfRound() {
        addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
    }

    @Override
    public void updateDescription() {
        this.description =
                amount == 1 ? DESCRIPTIONS[0] :
                        String.format(DESCRIPTIONS[1], amount);
    }
}
