package chemister.cards;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import chemister.character.Chemister;
import chemister.relics.starter.FlaskRelic;
import chemister.util.CardStats;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.patches.ExtraIconsPatch;
import com.evacipated.cardcrawl.mod.stslib.util.extraicons.ExtraIcons;
import com.evacipated.cardcrawl.mod.stslib.util.extraicons.IconPayload;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

public abstract class CatalystCard extends BaseCard {
    public interface CatalystDiscountRelic {
        int modifyCatalystDiscount(int discount);
    }
    public static int getDiscount() {
        int discount = 1;
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof CatalystDiscountRelic) {
                discount = ((CatalystDiscountRelic) r).modifyCatalystDiscount(discount);
            }
        }
        return -discount;
    }



    private int baseCost;
    private final Chemister.Flasks[] flasks;

    public CatalystCard(String ID, CardStats info, Chemister.Flasks[] flasks) {
        super(ID, info);

        this.flasks = flasks;
        CardModifierManager.addModifier(this, new CatalystIconsModifier(this.flasks));
        baseCost = this.cost;
    }

    public Chemister.Flasks[] getFlasks() {
        return flasks;
    }
    public int getBaseCost() {
        return baseCost;
    }

    @Override
    protected void upgradeBaseCost(int newBaseCost) {
        if (newBaseCost < 0) newBaseCost = 0;
        super.upgradeBaseCost(newBaseCost);
        baseCost = newBaseCost;
    }

    //Used for init as relics are registered after cards
    public void regenIcons() {
        CardModifierManager.removeModifiersById(this, "CHEMISTER_CATALYST_ICONS", true);
        CardModifierManager.addModifier(this, new CatalystIconsModifier(this.flasks));
    }

    @AbstractCardModifier.SaveIgnore
    public static class CatalystIconsModifier extends AbstractCardModifier {
        private transient IconPayload[] icons;

        public CatalystIconsModifier(Chemister.Flasks[] flasks) {
            priority = Integer.MIN_VALUE;

            icons = new IconPayload[flasks.length];
            float offset = 0.25f; //jank to have first icon be positioned correctly.
            //Icons placed after the catalyst icons will be positioned incorrectly regardless.
            for (int i = 0; i < flasks.length; ++i) {
                icons[i] = new IconPayload(ExtraIcons.icon(FlaskRelic.getFlaskTexture(flasks[i]))
                        .offsetY(FlaskRelic.getFlaskTexture(flasks[i]).getHeight() * offset));
                offset = 0.5f;
            }
        }

        public CatalystIconsModifier(IconPayload[] icons) {
            priority = Integer.MIN_VALUE;

            this.icons = icons;
        }

        @Override
        public void onRender(AbstractCard card, SpriteBatch sb) {
            ArrayList<IconPayload> icons = ExtraIconsPatch.ExtraIconsField.extraIcons.get(card);
            for (IconPayload payload : this.icons) {
                icons.add(payload);
            }
        }

        @Override
        public void onSingleCardViewRender(AbstractCard card, SpriteBatch sb) {
            ArrayList<IconPayload> icons = ExtraIconsPatch.ExtraIconsField.extraIcons.get(card);
            for (IconPayload payload : this.icons) {
                icons.add(payload);
            }
        }

        @Override
        public String identifier(AbstractCard card) {
            return "CHEMISTER_CATALYST_ICONS";
        }

        @Override
        public boolean isInherent(AbstractCard card) {
             return true;
        }

        @Override
        public AbstractCardModifier makeCopy() {
            return new CatalystIconsModifier(icons);
        }
    }
}
