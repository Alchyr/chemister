package chemister.actions;

import chemister.patches.TrackCardsDrawnDuringTurnPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DamageFromDrawnCardsAction extends AbstractGameAction {
    private final AbstractMonster mTarget;
    private final AbstractCard card;
    private final AttackEffect effect;

    public DamageFromDrawnCardsAction(AbstractCard card, int mult, AbstractMonster target, AttackEffect effect) {
        this.card = card;
        this.amount = mult;
        this.effect = effect;
        this.mTarget = target;
    }

    public void update() {
        card.baseDamage = amount * TrackCardsDrawnDuringTurnPatch.DRAWN_THIS_TURN;
        card.calculateCardDamage(mTarget);

        this.addToTop(new DamageAction(mTarget, new DamageInfo(AbstractDungeon.player, this.card.damage, this.card.damageTypeForTurn), this.effect));

        this.isDone = true;
    }
}
