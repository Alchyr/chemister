package chemister.powers;

import chemister.cards.WithdrawalCard;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static chemister.ChemisterMod.makeID;

public class SneakySipPower extends BasePower {
    public static final String POWER_ID = makeID("SneakySipPower");
    private static final boolean TURN_BASED = true;

    public SneakySipPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, TURN_BASED, owner, amount);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card instanceof WithdrawalCard) {
            addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
        }
    }

    //see WithdrawalCard

    @Override
    public void updateDescription() {
        this.description = amount == 1 ? DESCRIPTIONS[0] : String.format(DESCRIPTIONS[1], amount);
    }
}
