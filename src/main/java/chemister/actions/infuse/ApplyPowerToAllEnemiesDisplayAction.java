package chemister.actions.infuse;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_S;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.List;
import java.util.function.Function;

public class ApplyPowerToAllEnemiesDisplayAction extends AbstractGameAction implements DisplayableAction {
    private static final AcidSlime_S tempMonster = new AcidSlime_S(0, 0, 0);

    private final Function<AbstractMonster, AbstractPower> power;
    private final AbstractPower displayPower;
    private final boolean isFast;
    private final AttackEffect effect;

    public ApplyPowerToAllEnemiesDisplayAction(Function<AbstractMonster, AbstractPower> power, boolean isFast, AbstractGameAction.AttackEffect effect)
    {
        this.power = power;
        displayPower = power.apply(tempMonster);
        this.amount = displayPower.amount;
        this.isFast = isFast;
        this.effect = effect;
    }

    @Override
    public AbstractGameAction getAction() {
        return this;
    }

    @Override
    public TextureRegion getIcon() {
        return displayPower.region128;
    }

    @Override
    public float getIconScale() {
        return 50f / displayPower.region128.getRegionWidth();
    }

    @Override
    public void update() {
        List<AbstractMonster> monsters = AbstractDungeon.getMonsters().monsters;
        for (int i = monsters.size() - 1; i >= 0; --i) {
            AbstractMonster m = monsters.get(i);
            AbstractPower toApply = power.apply(m);
            addToTop(new ApplyPowerAction(m, AbstractDungeon.player, toApply, toApply.amount, isFast, effect));
        }
        isDone = true;
    }
}
