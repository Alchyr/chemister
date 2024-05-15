package chemister.powers;

import chemister.actions.infuse.InfuseAction;
import chemister.character.Chemister;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static chemister.ChemisterMod.makeID;

public class ArsonArmorPower extends BasePower {
    public static final String POWER_ID = makeID("ArsonArmorPower");
    private static final boolean TURN_BASED = true;

    public ArsonArmorPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, TURN_BASED, owner, amount);
    }

    public int onAttacked(DamageInfo info, int damageAmount) {
        this.flash();
        addToTop(new InfuseAction(Chemister.Flasks.IGNIS));

        return damageAmount;
    }

    public void atStartOfTurn() {
        addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
    }

    @Override
    public void updateDescription() {
        this.description =
                amount == 1 ? DESCRIPTIONS[0] :
                        String.format(DESCRIPTIONS[1], amount);
    }
}
