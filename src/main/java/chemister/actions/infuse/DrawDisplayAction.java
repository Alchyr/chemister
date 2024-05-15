package chemister.actions.infuse;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DrawDisplayAction extends DrawCardAction implements DisplayableAction {
    private static TextureRegion region = AbstractPower.atlas.findRegion("128/carddraw");

    public DrawDisplayAction(AbstractCreature source, int amount, boolean endTurnDraw) {
        super(source, amount, endTurnDraw);
    }

    public DrawDisplayAction(AbstractCreature source, int amount) {
        super(source, amount);
    }

    public DrawDisplayAction(int amount, boolean clearDrawHistory) {
        super(amount, clearDrawHistory);
    }

    public DrawDisplayAction(int amount) {
        super(amount);
    }

    public DrawDisplayAction(int amount, AbstractGameAction action) {
        super(amount, action);
    }

    public DrawDisplayAction(int amount, AbstractGameAction action, boolean clearDrawHistory) {
        super(amount, action, clearDrawHistory);
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
        return 0.55f;
    }
}
