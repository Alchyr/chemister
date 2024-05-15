package chemister.actions;

import chemister.patches.OverkillDamagePatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class CatalyticCombustionAction extends AbstractGameAction {
    public CatalyticCombustionAction(AbstractCreature source, int damage, DamageInfo.DamageType damageTypeForTurn) {
        this.source = source;
        this.amount = damage;
        this.damageType = damageTypeForTurn;

        this.actionType = ActionType.DAMAGE;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        tickDuration();

        if (isDone) {
            if (amount <= 0) {
                return;
            }
            target = getClosestTarget();
            if (target == null) {
                return;
            }


            AbstractDungeon.effectList.add(new ExplosionSmallEffect(target.hb.cX + MathUtils.random(-35.0f * Settings.scale, 35.0f * Settings.scale),
                    target.hb.cY + MathUtils.random(0, 20.0f * Settings.scale)));
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, AttackEffect.FIRE, false));
            this.target.damage(new DamageInfo(source, amount, damageType));

            if (target.equals(OverkillDamagePatch.overkilled)) {
                this.amount = OverkillDamagePatch.overkillAmt;
                this.amount *= 2;
            }
            else {
                this.amount = 0;
            }


            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            } else {
                if (this.amount > 0 && (this.target.isDying || this.target.currentHealth <= 0) && !this.target.halfDead) {
                    addToTop(new CatalyticCombustionAction(source, amount, damageType));
                }
                this.addToTop(new WaitAction(0.1F));
            }
        }
    }

    public static AbstractMonster getClosestTarget() {
        AbstractMonster closest = null;
        float dist = 0;

        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped()) {
                if (closest == null) {
                    closest = m;
                    dist = getDist(m);
                }
                else {
                    float mDist = getDist(m);
                    if (mDist < dist) {
                        closest = m;
                        dist = mDist;
                    }
                }
            }
        }

        return closest;
    }

    private static float getDist(AbstractMonster m) {
        float x = AbstractDungeon.player.hb.cX, y = AbstractDungeon.player.hb.cY;

        x = m.hb.cX - x;
        x *= x;
        y = m.hb.cY - y;
        y *= y;

        float hbOffset = ((m.hb.width + m.hb.height) / 4f);
        hbOffset *= hbOffset;

        return x + y - hbOffset;
    }
}
