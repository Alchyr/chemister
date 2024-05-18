package chemister.potions;

import chemister.cards.special.InfuseChoiceCard;
import chemister.character.Chemister;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.CardHelper;

import java.util.ArrayList;

import static chemister.ChemisterMod.makeID;

public class DoubleDose extends BasePotion {
    public static final String ID = makeID(DoubleDose.class.getSimpleName());

    private static final Color LIQUID_COLOR = CardHelper.getColor(230, 46, 40);
    private static final Color HYBRID_COLOR = CardHelper.getColor(220, 50, 45);
    private static final Color SPOTS_COLOR = null;

    public DoubleDose() {
        super(ID, 2, PotionRarity.COMMON, PotionSize.BOLT, LIQUID_COLOR, HYBRID_COLOR, SPOTS_COLOR);
        playerClass = Chemister.Meta.CHEMISTER;
    }

    @Override
    public String getDescription() {
        return String.format(potionStrings.DESCRIPTIONS[0], potency);
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
        addToBot(new ChooseOneAction(makeOptions()));
    }

    private ArrayList<AbstractCard> makeOptions() {
        ArrayList<AbstractCard> options = new ArrayList<>();

        for (Chemister.Flasks flask : Chemister.Flasks.values()) {
            options.add(new InfuseChoiceCard(flask, potency));
        }

        return options;
    }
}
