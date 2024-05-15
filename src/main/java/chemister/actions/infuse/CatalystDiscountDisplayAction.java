package chemister.actions.infuse;

import chemister.cards.CatalystCard;
import chemister.util.GeneralUtils;
import chemister.util.TextureLoader;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static chemister.ChemisterMod.imagePath;

public class CatalystDiscountDisplayAction extends AbstractGameAction implements DisplayableAction {
    private static final TextureRegion img =
            new TextureRegion(TextureLoader.getTexture(imagePath("missing.png")));

    public CatalystDiscountDisplayAction(int costReduction) {
        this.amount = costReduction;
    }

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
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card instanceof CatalystCard) {
                card.modifyCostForCombat(-amount);
            }
        }
        isDone = true;
    }
}
