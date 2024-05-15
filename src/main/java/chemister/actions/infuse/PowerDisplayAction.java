package chemister.actions.infuse;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class PowerDisplayAction extends ApplyPowerAction implements DisplayableAction {
    private final AbstractPower toApply;

    public PowerDisplayAction(AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount, boolean isFast, AttackEffect effect) {
        super(target, source, powerToApply, stackAmount, isFast, effect);
        toApply = powerToApply;
    }

    public PowerDisplayAction(AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount, boolean isFast) {
        super(target, source, powerToApply, stackAmount, isFast);
        toApply = powerToApply;
    }

    public PowerDisplayAction(AbstractCreature target, AbstractCreature source, AbstractPower powerToApply) {
        super(target, source, powerToApply);
        toApply = powerToApply;
    }

    public PowerDisplayAction(AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount) {
        super(target, source, powerToApply, stackAmount);
        toApply = powerToApply;
    }

    public PowerDisplayAction(AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount, AttackEffect effect) {
        super(target, source, powerToApply, stackAmount, effect);
        toApply = powerToApply;
    }

    @Override
    public boolean merge(DisplayableAction action) {
        if (action instanceof PowerDisplayAction) {
            if (((PowerDisplayAction) action).toApply.ID.equals(this.toApply.ID)) {
                this.getAction().amount += action.getAction().amount;
                return true;
            }
        }
        return false;
    }

    @Override
    public AbstractGameAction getAction() {
        return this;
    }

    @Override
    public TextureRegion getIcon() {
        return toApply.region128;
    }

    @Override
    public float getIconScale() {
        return 50f / toApply.region128.getRegionWidth();
    }
}
