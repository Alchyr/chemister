package chemister.patches;

import chemister.powers.SaltationPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

@SpirePatch(
        clz = GameActionManager.class,
        method = "getNextAction"
)
public class SaltationKeepGoingPatch {
    public static boolean salt = false;
    @SpirePrefixPatch
    public static void keepTheSalt(GameActionManager __instance) {
        if (salt && __instance.actions.isEmpty() && __instance.cardQueue.isEmpty()) {
            AbstractPower p = AbstractDungeon.player.getPower(SaltationPower.POWER_ID);
            if (p instanceof SaltationPower) {
                ((SaltationPower) p).restart();
            }
        }
    }
}
