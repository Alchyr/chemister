package chemister.actions.infuse;

import chemister.util.GeneralUtils;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DisplaySkillDuplicateAction extends AbstractGameAction implements DisplayableAction {
    private static TextureRegion region = AbstractPower.atlas.findRegion("128/master_reality"); //TODO - CHANGE

    public DisplaySkillDuplicateAction() {
        this.amount = 1;
    }

    @Override
    public AbstractGameAction getAction() {
        return this;
    }

    @Override
    public void update() {
        isDone = true;
        AbstractCard cheapest = null;
        int cheapestCost = 0, cost = 0;
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c.type == AbstractCard.CardType.SKILL) {
                cost = GeneralUtils.getLogicalCardCost(c);
                if (cheapest == null || cost < cheapestCost) {
                    cheapest = c;
                    cheapestCost = cost;
                }
            }
        }

        if (cheapest != null) {
            addToTop(new MakeTempCardInHandAction(cheapest, 1));
        }
    }

    @Override
    public TextureRegion getIcon() {
        return region;
    }

    @Override
    public float getIconScale() {
        return 49f / region.getRegionWidth();
    }
}
