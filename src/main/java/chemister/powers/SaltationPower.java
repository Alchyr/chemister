package chemister.powers;

import basemod.BaseMod;
import chemister.patches.OnRefreshHandPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.unique.ApplyBulletTimeAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.NoDrawPower;

import static chemister.ChemisterMod.makeID;

public class SaltationPower extends BasePower implements OnRefreshHandPatch.OnRefreshHandPower {
    public static final String POWER_ID = makeID(SaltationPower.class.getSimpleName());
    private static final boolean TURN_BASED = true;

    private boolean triggeredThisTurn = false;

    public SaltationPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, TURN_BASED, owner, amount);
    }
    @Override
    public void onGainedBlock(float blockAmount) {
        if (blockAmount > 0) {
            addToBot(new DrawCardAction(1));
        }
    }


    @Override
    public void onRefreshHand() {
        if (!triggeredThisTurn && AbstractDungeon.player.hand.size() >= BaseMod.MAX_HAND_SIZE) {
            triggeredThisTurn = true;

            this.flash();
            addToTop(new ApplyBulletTimeAction());
            addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new NoDrawPower(AbstractDungeon.player), 1));
        }
    }

    @Override
    public void atEndOfRound() {
        addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                SaltationPower.this.triggeredThisTurn = false;
                isDone = true;
            }
        });
    }

    @Override
    public void updateDescription() {
        this.description =
                amount == 1 ? DESCRIPTIONS[0] :
                        String.format(DESCRIPTIONS[1], amount);
    }
}
