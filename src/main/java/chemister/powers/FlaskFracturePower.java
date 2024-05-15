package chemister.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static chemister.ChemisterMod.makeID;

public class FlaskFracturePower extends BasePower {
    public static final String POWER_ID = makeID("FlaskFracturePower");
    private static final boolean TURN_BASED = true;

    public FlaskFracturePower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, TURN_BASED, owner, amount);
    }

    @Override
    public void onSpecificTrigger() {
        flash();
        addToTop(new ReducePowerAction(this.owner, this.owner, this, 1));
    }

    @Override
    public void atEndOfRound() {
        addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    @Override
    public void updateDescription() {
        this.description =
                amount == 1 ? DESCRIPTIONS[0] :
                        String.format(DESCRIPTIONS[1], amount);
    }
}
