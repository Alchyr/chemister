package chemister.relics;

import chemister.cards.ReagentCard;
import chemister.character.Chemister;
import chemister.util.GeneralUtils;
import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;
import java.util.List;

import static chemister.ChemisterMod.makeID;

public class ChemisterGuide extends BaseRelic {
    private static final String NAME = ChemisterGuide.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private static final int AMOUNT = 4;

    public ChemisterGuide() {
        super(ID, NAME, Chemister.Meta.CARD_COLOR, RelicTier.BOSS, LandingSound.FLAT);
    }


    public void onEquip() {
        AbstractDungeon.getCurrRoom().rewards.clear();

        CardGroup cards = GeneralUtils.filterCardsForDiscovery(c -> c instanceof ReagentCard && !c.hasTag(AbstractCard.CardTags.HEALING) && c.rarity != AbstractCard.CardRarity.SPECIAL && c.rarity != AbstractCard.CardRarity.BASIC);

        int numCards = 3;
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            numCards = r.changeNumberOfCardsInReward(numCards);
        }
        if (ModHelper.isModEnabled("Binary")) {
            --numCards;
        }
        if (numCards < 1) numCards = 1;

        List<RewardItem> intendedRewards = new ArrayList<>();
        for (int i = 0; i < AMOUNT; ++i) {
            RewardItem item = StSLib.generateCardReward(GeneralUtils.createCardsForDiscovery(cards, numCards), false);
            intendedRewards.add(item);
            AbstractDungeon.getCurrRoom().rewards.add(item);
        }

        AbstractDungeon.combatRewardScreen.open(this.DESCRIPTIONS[1]);
        AbstractDungeon.combatRewardScreen.rewards.removeIf((item)->!intendedRewards.contains(item));
        AbstractDungeon.combatRewardScreen.positionRewards();
        AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.0F;
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(DESCRIPTIONS[0], AMOUNT);
    }
}
