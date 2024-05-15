package chemister.actions.infuse;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class BlockDisplayAction extends GainBlockAction implements DisplayableAction {
    private static TextureRegion blockRegion = new TextureRegion(ImageMaster.BLOCK_ICON, 0, 0, ImageMaster.BLOCK_ICON.getWidth(), ImageMaster.BLOCK_ICON.getHeight());

    public BlockDisplayAction(AbstractCreature target, int amount) {
        super(target, amount);
    }

    public BlockDisplayAction(AbstractCreature target, AbstractCreature source, int amount) {
        super(target, source, amount);
    }

    public BlockDisplayAction(AbstractCreature target, int amount, boolean superFast) {
        super(target, amount, superFast);
    }

    public BlockDisplayAction(AbstractCreature target, AbstractCreature source, int amount, boolean superFast) {
        super(target, source, amount, superFast);
    }


    @Override
    public AbstractGameAction getAction() {
        return this;
    }

    @Override
    public TextureRegion getIcon() {
        return blockRegion;
    }

    @Override
    public float getIconScale() {
        return 0.75f;
    }
}
