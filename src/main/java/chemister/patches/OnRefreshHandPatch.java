package chemister.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

@SpirePatch(
        clz = CardGroup.class,
        method = "refreshHandLayout"
)
public class OnRefreshHandPatch {
    @SpireInsertPatch(
            rloc = 43
    )
    public static void onRefresh(CardGroup __instance) {
        for (AbstractPower p : AbstractDungeon.player.powers) {
            if (p instanceof OnRefreshHandPower) {
                ((OnRefreshHandPower) p).onRefreshHand();
            }
        }
    }

    public interface OnRefreshHandPower {
        void onRefreshHand();
    }
}
