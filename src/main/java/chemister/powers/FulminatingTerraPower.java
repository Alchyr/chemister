package chemister.powers;

import chemister.actions.infuse.InfuseAction;
import chemister.character.Chemister;
import chemister.relics.starter.FlaskRelic;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static chemister.ChemisterMod.makeID;

public class FulminatingTerraPower extends BasePower implements FlaskRelic.FlaskValuePower, InfuseAction.PostInfuseTrigger {
    public static final String POWER_ID = makeID("FulminatingTerraPower");
    private static final boolean TURN_BASED = true;

    public FulminatingTerraPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, TURN_BASED, owner, amount);
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], amount);
    }

    @Override
    public void modifyFlaskValue(FlaskRelic flask) {
        if (flask.flaskType() == Chemister.Flasks.TERRA) {
            flask.counter += this.amount;
        }
    }

    @Override
    public void postInfusion(FlaskRelic flask) {
        if (flask.flaskType() == Chemister.Flasks.TERRA) {
            this.flash();
            addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }
}
