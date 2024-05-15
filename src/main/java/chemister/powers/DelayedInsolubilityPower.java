package chemister.powers;

import chemister.actions.infuse.InfuseAction;
import chemister.character.Chemister;
import chemister.relics.starter.FlaskRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.DoubleDamagePower;

import static chemister.ChemisterMod.makeID;

public class DelayedInsolubilityPower extends BasePower {
    public static final String POWER_ID = makeID("DelayedInsolubilityPower");
    private static final boolean TURN_BASED = true;

    public DelayedInsolubilityPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, TURN_BASED, owner, amount);
    }

    @Override
    public void atStartOfTurn() {
        this.flash();
        this.addToBot(new ApplyPowerAction(this.owner, this.owner, new InsolubilityPower(this.owner, this.amount), this.amount, true));
        this.addToBot(new ReducePowerAction(this.owner, this.owner, this, this.amount));
    }

    @Override
    public void updateDescription() {
        this.description =
                amount == 1 ? DESCRIPTIONS[0] : String.format(DESCRIPTIONS[1], amount);
    }
}
