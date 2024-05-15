package chemister.actions.infuse;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public interface DisplayableAction {
    AbstractGameAction getAction();

    //Should result in 48x48 with getIcon texture size * getIconScale
    TextureRegion getIcon();
    default float getIconScale() {
        return 1;
    }

    default boolean merge(DisplayableAction action) {
        if (this.getClass().equals(action.getClass())) {
            this.getAction().amount += action.getAction().amount;
            return true;
        }
        return false;
    }
}