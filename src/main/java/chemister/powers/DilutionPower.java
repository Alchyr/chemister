package chemister.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static chemister.ChemisterMod.makeID;

public class DilutionPower extends BasePower {
    public static final String POWER_ID = makeID("DilutionPower");
    private static final boolean TURN_BASED = true;

    public DilutionPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, TURN_BASED, owner, amount);
    }

    //see WithdrawalCard

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
    }

    @Override
    public void updateDescription() {
        this.description = amount == 1 ? DESCRIPTIONS[0] : String.format(DESCRIPTIONS[1], amount);
    }
}
