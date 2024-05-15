package chemister.patches;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import chemister.cardmods.TemporaryCardmod;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import java.util.ArrayList;
import java.util.List;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "onVictory"
)
public class OnVictoryPatch {
    private static final List<AbstractCard> toRemove = new ArrayList<>();

    @SpirePostfixPatch
    public static void removeTempCardsOnVictory(AbstractPlayer __instance) {
        if (!__instance.isDying) {
            toRemove.clear();
            for (AbstractCard card : __instance.masterDeck.group) {
                for (AbstractCardModifier modifier : CardModifierManager.getModifiers(card, TemporaryCardmod.ID)) {
                    if (modifier instanceof TemporaryCardmod && ((TemporaryCardmod)modifier).age(card)) {
                        toRemove.add(card);
                    }
                }
            }

            if (toRemove.size() > 1) {
                for (AbstractCard c : toRemove) {
                    AbstractDungeon.effectList.add(new PurgeCardEffect(c, MathUtils.random(0.1F, 0.9F) * Settings.WIDTH, MathUtils.random(0.2F, 0.8F) * Settings.HEIGHT));
                    AbstractDungeon.player.masterDeck.removeCard(c);
                }
            }
            else if (toRemove.size() == 1) {
                AbstractDungeon.effectList.add(new PurgeCardEffect(toRemove.get(0)));
                AbstractDungeon.player.masterDeck.removeCard(toRemove.get(0));
            }
            toRemove.clear();
        }
    }
}
