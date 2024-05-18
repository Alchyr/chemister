package chemister.patches;

import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.RenderCardDescriptors;
import basemod.patches.com.megacrit.cardcrawl.screens.SingleCardViewPopup.RenderCardDescriptorsSCV;
import chemister.cards.ReagentCard;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

import static chemister.ChemisterMod.makeID;

public class RenderReagentTypeName {
    private static final String TEXT = CardCrawlGame.languagePack.getUIString(makeID("ReagentType")).TEXT[0];
    private static final GlyphLayout gl = new GlyphLayout();

    @SpirePatch(
        clz = AbstractCard.class,
        method = "renderType"
    )
    public static class ChangeText {
        @SpireInsertPatch(
                rloc = 9, //right after text is set to Power. Might need to be 10. Test.
                localvars = { "text" }
        )
        public static void changeText(AbstractCard __instance, SpriteBatch sb, @ByRef String[] text) {
            if (__instance instanceof ReagentCard) {
                text[0] = TEXT;
            }
        }
    }

    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = "renderCardTypeText"
    )
    public static class ChangeSCVText {
        @SpireInsertPatch(
                rloc = 10,
                localvars = { "label" }
        )
        public static void changeText(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard ___card, @ByRef String[] label) {
            if (___card instanceof ReagentCard) {
                label[0] = TEXT;
            }
        }
    }

    @SpirePatch(
            clz = RenderCardDescriptors.Frame.class,
            method = "Insert"
    )
    public static class FixFrame {
        @SpireInsertPatch(
                rloc = 20,
                localvars = { "typeText" }
        )
        public static void changeText(AbstractCard __instance, SpriteBatch sb, float x, float y, float[] tOffset, float[] tWidth, @ByRef String[] typeText) {
            if (__instance instanceof ReagentCard) {
                typeText[0] = TEXT;

                gl.reset();
                FontHelper.cardTypeFont.getData().setScale(1.0F);
                gl.setText(FontHelper.cardTypeFont, typeText[0]);
                tOffset[0] = (gl.width - 38.0F * Settings.scale) / 2.0F;
                tWidth[0] = (gl.width - 0.0F) / (32.0F * Settings.scale);
            }
        }
    }

    @SpirePatch(
            clz = RenderCardDescriptorsSCV.Frame.class,
            method = "Insert"
    )
    public static class FixSCVFrame {
        @SpireInsertPatch(
                rloc = 20,
                localvars = { "typeText" }
        )
        public static void changeText(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard ___card, float[] tOffset, float[] tWidth, @ByRef String[] typeText) {
            if (___card instanceof ReagentCard) {
                typeText[0] = TEXT;

                gl.reset();
                FontHelper.panelNameFont.getData().setScale(1.0F);
                gl.setText(FontHelper.panelNameFont, typeText[0]);
                tOffset[0] = (gl.width - 70.0F * Settings.scale) / 2.0F;
                tWidth[0] = (gl.width - 0.0F) / (62.0F * Settings.scale);
            }
        }
    }
}
