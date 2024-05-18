package chemister.patches;

import basemod.ReflectionHacks;
import chemister.cards.ReagentCard;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

@SpirePatch(
        clz = SingleCardViewPopup.class,
        method = "allowUpgradePreview"
)
public class NoUpgradePreview {
    @SpirePrefixPatch
    public static SpireReturn<Boolean> noPreviewReagent(SingleCardViewPopup __instance) {
        if (ReflectionHacks.getPrivate(__instance, SingleCardViewPopup.class, "card") instanceof ReagentCard) {
            return SpireReturn.Return(false);
        }
        return SpireReturn.Continue();
    }
}
