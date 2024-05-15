package chemister.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.ReduceCostForTurnAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static chemister.ChemisterMod.makeID;

public class AerPower extends BasePower implements NonStackablePower {
    public static final String POWER_ID = makeID("AerPower");
    private static final boolean TURN_BASED = true;

    public boolean renderSecondAmount = false;
    public int times;

    public AerPower(AbstractCreature owner, int amount, int times) {
        super(POWER_ID, PowerType.BUFF, TURN_BASED, owner, amount);

        this.times = times;

        if (times > 1) renderSecondAmount = true;
        updateDescription();
    }

    @Override
    public boolean isStackable(AbstractPower power) {
        return (power instanceof AerPower && ((AerPower) power).times == this.times);
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if (this.owner.isPlayer && times > 0) {
            this.flash();
            times -= 1;
            updateDescription();

            addToTop(new ReduceCostForTurnAction(card, amount));
            if (times <= 0) {
                addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            }
        }
    }

    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        super.renderAmount(sb, x, y, c);
        if (renderSecondAmount) {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.times), x, y + 15.0F * Settings.scale, this.fontScale, c);
        }
    }

    @Override
    public void updateDescription() {
        if (renderSecondAmount && times > 1) {
            this.description = DESCRIPTIONS[1] + times + DESCRIPTIONS[2] + amount + DESCRIPTIONS[3];
        }
        else {
            this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[3];
        }
    }
}
