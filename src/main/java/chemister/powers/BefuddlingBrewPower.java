package chemister.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.Iterator;
import java.util.List;

import static chemister.ChemisterMod.makeID;

public class BefuddlingBrewPower extends BasePower {
    public static final String POWER_ID = makeID("BefuddlingBrewPower");
    private static final boolean TURN_BASED = true;

    public BefuddlingBrewPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, TURN_BASED, owner, amount);
    }

    @Override
    public void atStartOfTurn() {
        addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));

        flash();

        List<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters;

        for (int i = monsters.size() - 1; i >= 0; --i) {
            AbstractMonster m = monsters.get(i);
            addToTop(new ApplyPowerAction(m, owner, new WeakPower(m, amount, false), amount, true, AbstractGameAction.AttackEffect.NONE));
        }
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], amount);
    }
}
