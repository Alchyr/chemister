package chemister.actions;

import chemister.cards.special.Ashes;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.unique.AddCardToDeckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;

public class ConsumptionAction extends AbstractGameAction {
    private final int increaseIgnisAmt;
    private final DamageInfo info;
    private static final float DURATION = 0.1F;

    public ConsumptionAction(AbstractCreature target, DamageInfo info, int amt) {
        this.info = info;
        this.setValues(target, info);
        this.increaseIgnisAmt = amt;
        this.actionType = ActionType.DAMAGE;
        this.duration = DURATION;
    }

    public void update() {
        if (this.duration == 0.1F && this.target != null) {
            AbstractDungeon.effectList.add(new InflameEffect(target));
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.FIRE));
            this.target.damage(this.info);

            if ((this.target.isDying || this.target.currentHealth <= 0) && !this.target.halfDead && !this.target.hasPower(MinionPower.POWER_ID)) {
                AbstractCard ashes = null;
                for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                    if (c instanceof Ashes) {
                        ashes = c;
                        break;
                    }
                }

                if (ashes != null) {
                    ashes.misc += increaseIgnisAmt;
                    ashes.baseMagicNumber = ashes.magicNumber = ashes.misc;

                    AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(ashes.makeSameInstanceOf()));
                }
                else {
                    ashes = new Ashes();
                    ashes.misc = increaseIgnisAmt;
                    ashes.baseMagicNumber = ashes.magicNumber = ashes.misc;

                    AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(ashes, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                }
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }

        this.tickDuration();
    }
}
