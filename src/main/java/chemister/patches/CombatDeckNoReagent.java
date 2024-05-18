package chemister.patches;

import chemister.cards.ReagentCard;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.CardGroup;

@SpirePatch(
        clz = CardGroup.class,
        method = "initializeDeck"
)
public class CombatDeckNoReagent {
    @SpireInsertPatch(
            rloc = 2,
            localvars = { "copy" }
    )
    public static void removeReagents(CardGroup __instance, CardGroup masterDeck, CardGroup copy) {
        copy.group.removeIf((card)->card instanceof ReagentCard);
    }
}
