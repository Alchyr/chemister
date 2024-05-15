package chemister.patches;

import chemister.relics.starter.FlaskRelic;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

@SpirePatch(
        clz = CardGroup.class,
        method = "applyPowers"
)
public class ApplyPowersUpdateFlaskState {
    @SpirePrefixPatch
    public static void apply(CardGroup __instance) {
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof FlaskRelic) {
                ((FlaskRelic) r).applyReagents();
            }
        }
    }
}
