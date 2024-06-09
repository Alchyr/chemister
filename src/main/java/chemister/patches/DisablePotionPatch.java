package chemister.patches;


import chemister.character.Chemister;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.potions.PowerPotion;

import java.util.ArrayList;

@SpirePatch(
        clz = PotionHelper.class,
        method = "getPotions"
)
public class DisablePotionPatch {
    @SpirePostfixPatch
    public static ArrayList<String> removePotions(ArrayList<String> __result, AbstractPlayer.PlayerClass c, boolean getAll) {
        if (c == Chemister.Meta.CHEMISTER) {
            __result.remove(PowerPotion.POTION_ID);
        }
        return __result;
    }
}
