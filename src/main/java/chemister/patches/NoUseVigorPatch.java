package chemister.patches;

import chemister.relics.EternalFlame;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class NoUseVigorPatch {
    @SpireEnum
    public static AbstractCard.CardTags CHEMISTER_NO_VIGOR_USE;

    @SpirePatch(
            clz = VigorPower.class,
            method = "onUseCard"
    )
    public static class CheckTag {
        @SpirePrefixPatch
        public static SpireReturn<?> nope(VigorPower __instance, AbstractCard c, UseCardAction action) {
            if (c.hasTag(CHEMISTER_NO_VIGOR_USE)) {
                __instance.flash();
                return SpireReturn.Return();
            }
            else if (c.type == AbstractCard.CardType.ATTACK) {
                AbstractRelic r = AbstractDungeon.player.getRelic(EternalFlame.ID);
                if (r != null) {
                    r.flash();
                    __instance.flash();
                    AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(__instance.owner, __instance.owner, __instance.ID, __instance.amount / 2));
                    return SpireReturn.Return();
                }
            }
            return SpireReturn.Continue();
        }
    }
}
