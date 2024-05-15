package chemister.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static chemister.ChemisterMod.makeID;

public class SeismicShellPower extends BasePower {
    public static final String POWER_ID = makeID("SeismicShellPower");
    private static final boolean TURN_BASED = true;

    private int lastTurnBlock = 0;

    public SeismicShellPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, TURN_BASED, owner, amount);
    }

    @Override
    public void atEndOfRound() {
        lastTurnBlock = owner.currentBlock;
    }

    @Override
    public void atStartOfTurn() {
        addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        if (lastTurnBlock != 0) {
            flash();
            addToTop(new DamageAllEnemiesAction(AbstractDungeon.player, DamageInfo.createDamageMatrix(this.amount * lastTurnBlock, true, false),
                    DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
