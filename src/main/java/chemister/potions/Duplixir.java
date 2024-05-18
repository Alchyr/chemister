package chemister.potions;

import basemod.helpers.CardModifierManager;
import chemister.cardmods.TemporaryCardmod;
import chemister.cards.ReagentCard;
import chemister.character.Chemister;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.shrines.WeMeetAgain;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static chemister.ChemisterMod.makeID;

public class Duplixir extends BasePotion {
    public static final String ID = makeID(Duplixir.class.getSimpleName());

    private static final Color LIQUID_COLOR = CardHelper.getColor(255, 255, 255);
    private static final Color HYBRID_COLOR = CardHelper.getColor(10, 10, 10);
    private static final Color SPOTS_COLOR = null;

    public Duplixir() {
        super(ID, 4, PotionRarity.RARE, PotionSize.SPHERE, LIQUID_COLOR, HYBRID_COLOR, SPOTS_COLOR);
        playerClass = Chemister.Meta.CHEMISTER;
    }

    @Override
    public boolean canUse() {
        if (AbstractDungeon.actionManager.turnHasEnded && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            return false;
        } else {
            return AbstractDungeon.getCurrRoom().event == null || !(AbstractDungeon.getCurrRoom().event instanceof WeMeetAgain);
        }
    }

    @Override
    public String getDescription() {
        return String.format(potionStrings.DESCRIPTIONS[0], potency);
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
        AbstractCard reagent = getNewestReagent();

        if (reagent != null) {
            reagent = reagent.makeStatEquivalentCopy();

            AbstractCard finalReagent = reagent;
            boolean hasInherentTemporary = CardModifierManager.getModifiers(reagent, TemporaryCardmod.ID).stream().anyMatch((modifier)->modifier.isInherent(finalReagent));

            CardModifierManager.removeModifiersById(reagent, TemporaryCardmod.ID, true);
            CardModifierManager.addModifier(reagent, new TemporaryCardmod(this.potency, hasInherentTemporary));

            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(reagent, Settings.WIDTH / 2f, Settings.HEIGHT / 2f));
        }
    }

    private AbstractCard getNewestReagent() {
        AbstractCard reagent = null;
        for (int i = AbstractDungeon.player.masterDeck.group.size() - 1; i >= 0; --i) {
            AbstractCard c = AbstractDungeon.player.masterDeck.group.get(i);
            if (c instanceof ReagentCard) {
                reagent = c;
                break;
            }
        }
        return reagent;
    }
}
