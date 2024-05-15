package chemister.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.List;

import static chemister.ChemisterMod.makeID;

public class SneakySipPower extends BasePower {
    public static final String POWER_ID = makeID("SneakySipPower");
    private static final boolean TURN_BASED = true;

    public SneakySipPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, TURN_BASED, owner, amount);
    }

    public boolean preventInfuseCount() {
        if (amount > 0) {
            --amount;
            updateDescription();
            this.flash();
            if (this.amount <= 0) {
                addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            }
            return true;
        }
        return false;
    }

    @Override
    public void updateDescription() {
        this.description = amount == 1 ? DESCRIPTIONS[0] : String.format(DESCRIPTIONS[1], amount);
    }
}
