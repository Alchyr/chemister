package chemister.actions.infuse;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ScryDisplayAction extends ScryAction implements DisplayableAction {
    private static TextureRegion region = AbstractPower.atlas.findRegion("128/master_reality");

    public ScryDisplayAction(int numCards) {
        super(numCards);
    }

    @Override
    public AbstractGameAction getAction() {
        return this;
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
