package chemister.powers;

import chemister.actions.infuse.InfuseAction;
import chemister.character.Chemister;
import chemister.relics.starter.FlaskRelic;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static chemister.ChemisterMod.makeID;

public class FulminatingIgnisPower extends BasePower implements FlaskRelic.FlaskValuePower, InfuseAction.PostInfuseTrigger {
    public static final String POWER_ID = makeID("FulminatingIgnisPower");
    private static final boolean TURN_BASED = true;

    public FulminatingIgnisPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, TURN_BASED, owner, amount);
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], amount);
    }

    @Override
    public void modifyFlaskValue(FlaskRelic flask) {
        if (flask.flaskType() == Chemister.Flasks.IGNIS) {
            flask.counter += this.amount;
        }
    }

    @Override
    public void postInfusion(FlaskRelic flask) {
        if (flask.flaskType() == Chemister.Flasks.IGNIS) {
            this.flash();
            addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }
}
