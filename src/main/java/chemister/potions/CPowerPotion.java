package chemister.potions;

import basemod.helpers.CardModifierManager;
import chemister.ChemisterMod;
import chemister.actions.FlexibleDiscoveryAction;
import chemister.cardmods.TemporaryCardmod;
import chemister.cards.ReagentCard;
import chemister.character.Chemister;
import chemister.util.GeneralUtils;
import chemister.util.KeywordInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.PowerPotion;

import java.util.ArrayList;

import static chemister.ChemisterMod.makeID;

public class CPowerPotion extends BasePotion {
    private static final KeywordInfo reagentKeyword = ChemisterMod.keywords.get("reagent");

    public static final String ID = makeID(CPowerPotion.class.getSimpleName());

    public CPowerPotion() {
        super(ID, 1, PotionRarity.COMMON, PotionSize.CARD, PotionColor.BLUE);
        playerClass = Chemister.Meta.CHEMISTER;
    }

    @Override
    public void initializeData() {
        this.potency = this.getPotency();

        PotionStrings nameStrings = CardCrawlGame.languagePack.getPotionString(PowerPotion.POTION_ID);
        potionStrings = CardCrawlGame.languagePack.getPotionString(CPowerPotion.ID);
        this.name = nameStrings.NAME;
        this.description = getDescription();

        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        addAdditionalTips();
    }

    @Override
    public String getDescription() {
        return potency == 1 ? potionStrings.DESCRIPTIONS[0] :
                String.format(potionStrings.DESCRIPTIONS[1], potency);
    }

    @Override
    public void addAdditionalTips() {
        tips.add(new PowerTip(reagentKeyword.PROPER_NAME, reagentKeyword.DESCRIPTION));
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
        CardGroup cards = GeneralUtils.filterCardsForDiscovery(c -> c instanceof ReagentCard && !c.hasTag(AbstractCard.CardTags.HEALING) && c.rarity != AbstractCard.CardRarity.SPECIAL && c.rarity != AbstractCard.CardRarity.BASIC);
        ArrayList<AbstractCard> discoveryCards = GeneralUtils.createCardsForDiscovery(cards, 3);

        for (AbstractCard c : discoveryCards) {
            boolean hasInherentTemporary = CardModifierManager.getModifiers(c, TemporaryCardmod.ID).stream().anyMatch((modifier)->modifier.isInherent(c));

            CardModifierManager.removeModifiersById(c, TemporaryCardmod.ID, true);
            CardModifierManager.addModifier(c, new TemporaryCardmod(1, hasInherentTemporary));
        }

        addToBot(new FlexibleDiscoveryAction(discoveryCards, potency, true));
    }
}
