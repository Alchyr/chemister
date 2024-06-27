package chemister.powers;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static chemister.ChemisterMod.makeID;

public class PyroclasticlysmPower extends BasePower implements NonStackablePower {
    public static final String POWER_ID = makeID(PyroclasticlysmPower.class.getSimpleName());
    private static final boolean TURN_BASED = true;

    private int blockForDamage;
    private int damageForBlock;

    public PyroclasticlysmPower(AbstractCreature owner, int amount, int blockForDamage, int damageForBlock) {
        super(POWER_ID, PowerType.BUFF, TURN_BASED, owner, null, amount, false);

        this.blockForDamage = blockForDamage;
        this.damageForBlock = damageForBlock;

        updateDescription();
    }

    @Override
    public boolean isStackable(AbstractPower power) {
        if (power instanceof PyroclasticlysmPower) {
            if (((PyroclasticlysmPower) power).blockForDamage > blockForDamage)
                blockForDamage = ((PyroclasticlysmPower) power).blockForDamage;
            if (((PyroclasticlysmPower) power).damageForBlock > damageForBlock)
                damageForBlock = ((PyroclasticlysmPower) power).damageForBlock;
            return true;
        }
        return false;
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.type == DamageInfo.DamageType.NORMAL) {
            if (target != null && info.output > 0) {
                flashWithoutSound();
                addToTop(new GainBlockAction(this.owner, blockForDamage));
            }

            /*AbstractCreature attacked = target;
            addToTop(new AbstractGameAction() {
                @Override
                public void update() {
                    isDone = true;
                    if (attacked != null && attacked.lastDamageTaken > 0) {
                        PyroclasticlysmPower.this.flash();
                        addToTop(new GainBlockAction(PyroclasticlysmPower.this.owner, attacked.lastDamageTaken));
                    }
                }
            });*/
        }
    }

    @Override
    public void onGainedBlock(float blockAmount) {
        int amt = MathUtils.floor(blockAmount);
        if (amt > 0) {
            this.flash();
            addToBot(new DamageRandomEnemyAction(new DamageInfo(this.owner, damageForBlock, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        }
    }

    @Override
    public void atEndOfRound() {
        addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
    }

    @Override
    public void updateDescription() {
        this.description =
                amount == 1 ? String.format(DESCRIPTIONS[0], blockForDamage, damageForBlock) :
                        String.format(DESCRIPTIONS[1], amount, blockForDamage, damageForBlock);
    }
}
