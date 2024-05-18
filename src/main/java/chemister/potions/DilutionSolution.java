package chemister.potions;

import chemister.actions.FlexibleDiscoveryAction;
import chemister.cards.WithdrawalCard;
import chemister.character.Chemister;
import chemister.powers.DilutionPower;
import chemister.util.GeneralUtils;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;

import static chemister.ChemisterMod.makeID;

public class DilutionSolution extends BasePotion {
    public static final String ID = makeID(DilutionSolution.class.getSimpleName());

    private static final Color LIQUID_COLOR = CardHelper.getColor(10, 146, 240);
    private static final Color HYBRID_COLOR = CardHelper.getColor(15, 137, 246);
    private static final Color SPOTS_COLOR = null;

    public DilutionSolution() {
        super(ID, 1, PotionRarity.UNCOMMON, PotionSize.CARD, LIQUID_COLOR, HYBRID_COLOR, SPOTS_COLOR);
        playerClass = Chemister.Meta.CHEMISTER;
    }

    @Override
    public String getDescription() {
        return potionStrings.DESCRIPTIONS[0] +
                (potency == 1 ? potionStrings.DESCRIPTIONS[1]
                : String.format(potionStrings.DESCRIPTIONS[2], potency));
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DilutionPower(AbstractDungeon.player, 1)));

        CardGroup cards = GeneralUtils.filterCardsForDiscovery(c -> c instanceof WithdrawalCard && !c.hasTag(AbstractCard.CardTags.HEALING) && c.rarity != AbstractCard.CardRarity.SPECIAL && c.rarity != AbstractCard.CardRarity.BASIC);
        addToBot(new FlexibleDiscoveryAction(GeneralUtils.createCardsForDiscovery(cards, 3), true));
    }
}
