package chemister.relics;

import chemister.cards.uncommon.EncroachingEarth;
import chemister.cards.uncommon.SeepingSolvent;
import chemister.cards.uncommon.SunderSalt;
import chemister.cards.uncommon.TurbulenceTonic;
import chemister.character.Chemister;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static chemister.ChemisterMod.makeID;

public class TravelRefill extends BaseRelic {
    private static final String NAME = TravelRefill.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private static final String[] tempReagents = {
            SeepingSolvent.ID,
            EncroachingEarth.ID,
            SunderSalt.ID,
            TurbulenceTonic.ID
    };

    public TravelRefill() {
        super(ID, NAME, Chemister.Meta.CARD_COLOR, RelicTier.UNCOMMON, LandingSound.CLINK);

        counter = 4;
    }

    @Override
    public void onVictory() {
        --counter;
        if (counter <= 0) {
            flash();
            counter = 4;
            addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractCard tempReagent = CardLibrary.getCard(tempReagents[AbstractDungeon.cardRandomRng.random(tempReagents.length - 1)]);
            if (tempReagent == null) tempReagent = new Madness();
            tempReagent = tempReagent.makeCopy();

            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(tempReagent, Settings.WIDTH / 2f, Settings.HEIGHT / 2f));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public boolean canSpawn() {
        return Settings.isEndless || AbstractDungeon.floorNum <= 40;
    }
}
