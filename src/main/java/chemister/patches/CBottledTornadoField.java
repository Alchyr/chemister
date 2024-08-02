package chemister.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpirePatch(
        clz = AbstractCard.class,
        method = SpirePatch.CLASS
)
public class CBottledTornadoField {
    public static SpireField<Boolean> inCBottledTornado = new SpireField<>(()->false);

    @SpirePatch(
            clz = AbstractCard.class,
            method = "makeStatEquivalentCopy"
    )
    public static class MakeStatEquivalentCopy
    {
        @SpirePostfixPatch
        public static AbstractCard copyBottled(AbstractCard __result, AbstractCard __instance)
        {
            inCBottledTornado.set(__result, inCBottledTornado.get(__instance));
            return __result;
        }
    }
}
