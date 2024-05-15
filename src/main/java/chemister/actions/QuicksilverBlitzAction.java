package chemister.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;

public class QuicksilverBlitzAction extends AbstractGameAction {
    private final DamageInfo info;
    private final int perDiscount;

    public QuicksilverBlitzAction(AbstractCreature target, DamageInfo info, int perDiscount) {
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.startDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startDuration;

        this.perDiscount = perDiscount;
    }

    public void update() {
        if (this.shouldCancelAction()) {
            this.isDone = true;
        } else {
            this.tickDuration();
            if (this.isDone) {
                CardCrawlGame.sound.play("ATTACK_WHIRLWIND", 0.1f);
                AbstractDungeon.effectList.add(new WhirlwindEffect());
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.SLASH_HEAVY, false));

                this.target.damage(this.info);

                int discounts = target.lastDamageTaken / perDiscount;
                for (int i = 0; i < discounts; ++i) {
                    addToTop(new ReduceCostOfRandomCardForTurnAction(1));
                }

                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                } else {
                    this.addToTop(new WaitAction(0.1F));
                }
            }
        }
    }
}
