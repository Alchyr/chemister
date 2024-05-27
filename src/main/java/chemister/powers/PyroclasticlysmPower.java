package chemister.powers;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static chemister.ChemisterMod.makeID;

public class PyroclasticlysmPower extends BasePower {
    public static final String POWER_ID = makeID(PyroclasticlysmPower.class.getSimpleName());
    private static final boolean TURN_BASED = true;

    public PyroclasticlysmPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, TURN_BASED, owner, amount);
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.type == DamageInfo.DamageType.NORMAL) {
            addToTop(new AbstractGameAction() {
                @Override
                public void update() {
                    isDone = true;
                    if (target != null && target.lastDamageTaken > 0) {
                        PyroclasticlysmPower.this.flash();
                        addToTop(new GainBlockAction(PyroclasticlysmPower.this.owner, target.lastDamageTaken));
                    }
                }
            });
        }
    }

    @Override
    public void onGainedBlock(float blockAmount) {
        int amt = MathUtils.floor(blockAmount);
        if (amt > 0) {
            this.flash();
            addToBot(new DamageRandomEnemyAction(new DamageInfo(this.owner, amt, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        }
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
