package chemister.actions.infuse;

import basemod.BaseMod;
import chemister.util.GeneralUtils;
import chemister.util.TextureLoader;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static chemister.ChemisterMod.imagePath;

public class AddAttackDisplayAction extends AbstractGameAction implements DisplayableAction {
    private static final TextureRegion img =
            new TextureRegion(TextureLoader.getTexture(imagePath("DrawAttack.png")));

    @Override
    public AbstractGameAction getAction() {
        return this;
    }

    @Override
    public TextureRegion getIcon() {
        return img;
    }

    @Override
    public float getIconScale() {
        return 50f / img.getRegionWidth();
    }

    @Override
    public void update() {
        isDone = true;

        if (AbstractDungeon.player.drawPile.isEmpty()) return;

        AbstractCard cheapest = null;
        int cost = 0;
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            int cardCost = GeneralUtils.getLogicalCardCost(c);
            if (cheapest == null || cardCost < cost) {
                cheapest = c;
                cost = cardCost;
            }
        }

        if (cheapest != null) {
            if (AbstractDungeon.player.hand.size() >= BaseMod.MAX_HAND_SIZE) {
                AbstractDungeon.player.drawPile.moveToDiscardPile(cheapest);
                AbstractDungeon.player.createHandIsFullDialog();
            }
            else {
                cheapest.untip();
                cheapest.unhover();
                cheapest.lighten(true);
                cheapest.setAngle(0.0F);
                cheapest.drawScale = 0.12F;
                cheapest.targetDrawScale = 0.75F;
                cheapest.current_x = CardGroup.DRAW_PILE_X;
                cheapest.current_y = CardGroup.DRAW_PILE_Y;
                AbstractDungeon.player.drawPile.removeCard(cheapest);
                AbstractDungeon.player.hand.addToHand(cheapest);
                AbstractDungeon.player.hand.refreshHandLayout();
                AbstractDungeon.player.hand.applyPowers();
            }
        }
    }
}
