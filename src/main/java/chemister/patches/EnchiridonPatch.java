package chemister.patches;

import chemister.character.Chemister;
import chemister.relics.replace.CEnchiridion;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Enchiridion;

@SpirePatch(
        clz = Enchiridion.class,
        method = "makeCopy"
)
public class EnchiridonPatch {
    @SpirePrefixPatch
    public static SpireReturn<AbstractRelic> altRelic(Enchiridion __instance) {
        if (CardCrawlGame.isInARun() && AbstractDungeon.player instanceof Chemister) {
            return SpireReturn.Return(new CEnchiridion());
        }
        return SpireReturn.Continue();
    }
}
