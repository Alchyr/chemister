package chemister.powers;

import chemister.ChemisterMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static chemister.ChemisterMod.makeID;

public class CavitationPower extends BasePower {
    public static final String POWER_ID = makeID(CavitationPower.class.getSimpleName());
    private static final boolean TURN_BASED = true;

    public CavitationPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, TURN_BASED, owner, amount);
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        int totalBase = 0;
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            totalBase += ChemisterMod.getCardBase(c);
        }
        if (totalBase > 0) {
            this.flash();
            addToBot(new DamageRandomEnemyAction(new DamageInfo(this.owner, totalBase, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.POISON));
        }
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
