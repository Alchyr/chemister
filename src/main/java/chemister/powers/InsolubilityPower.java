package chemister.powers;

import chemister.cards.WithdrawalCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static chemister.ChemisterMod.makeID;

public class InsolubilityPower extends BasePower implements WithdrawalCard.IncreaseWithdrawalPower {
    public static final String POWER_ID = makeID("InsolubilityPower");
    private static final boolean TURN_BASED = true;

    public InsolubilityPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, TURN_BASED, owner, amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    @Override
    public void updateDescription() {
        this.description =
                amount == 1 ? DESCRIPTIONS[0] : String.format(DESCRIPTIONS[1], amount);
    }

    @Override
    public int increaseWithdrawalAmount() {
        this.flash();
        return this.amount;
    }
}
