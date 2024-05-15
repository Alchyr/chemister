package chemister.patches;

import chemister.cards.CatalystCard;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.unique.RandomizeHandCostAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ConfusionPower;

import java.util.HashMap;
import java.util.Map;

public class NoRandomCostPatch {
    @SpirePatch(
            clz = ConfusionPower.class,
            method = "onCardDraw"
    )
    public static class NoConfusion {
        public static SpireReturn<?> Prefix(ConfusionPower __instance, AbstractCard drawnCard) {
            if (drawnCard instanceof CatalystCard) return SpireReturn.Return();
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = RandomizeHandCostAction.class,
            method = "update"
    )
    public static class NoOil {
        private static final Map<AbstractCard, Integer> origCosts = new HashMap<>();

        @SpirePrefixPatch
        public static void TempRemove(RandomizeHandCostAction __instance) {
            origCosts.clear();
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if (c instanceof CatalystCard) {
                    origCosts.put(c, c.cost);
                    c.cost = -1;
                }
            }
        }

        @SpirePostfixPatch
        public static void Reset(RandomizeHandCostAction __instance) {
            for (Map.Entry<AbstractCard, Integer> card : origCosts.entrySet()) {
                card.getKey().cost = card.getValue();
            }
            origCosts.clear();
        }
    }
}
