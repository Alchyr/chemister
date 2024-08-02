package chemister.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DamageEqualToBlockAction extends AbstractGameAction {
    private final AbstractMonster mTarget;
    private final AbstractCard card;
    private final AbstractGameAction.AttackEffect effect;

    public DamageEqualToBlockAction(AbstractCard card, AbstractMonster target, AbstractGameAction.AttackEffect effect) {
        this.card = card;
        this.effect = effect;
        this.mTarget = target;
    }

    public void update() {
        card.baseDamage = AbstractDungeon.player.currentBlock;
        card.calculateCardDamage(mTarget);

        this.addToTop(new DamageAction(mTarget, new DamageInfo(AbstractDungeon.player, this.card.damage, this.card.damageTypeForTurn), this.effect));

        this.isDone = true;
    }
}
