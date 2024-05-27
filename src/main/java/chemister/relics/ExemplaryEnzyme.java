package chemister.relics;

import chemister.actions.infuse.InfuseAction;
import chemister.cards.CatalystCard;
import chemister.character.Chemister;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static chemister.ChemisterMod.makeID;

public class ExemplaryEnzyme extends BaseRelic {
    private static final String NAME = ExemplaryEnzyme.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private boolean activated = false;

    public ExemplaryEnzyme() {
        super(ID, NAME, Chemister.Meta.CARD_COLOR, RelicTier.COMMON, LandingSound.CLINK);
    }

    public void atBattleStartPreDraw() {
        this.activated = false;
    }

    @Override
    public void onCardDraw(AbstractCard drawnCard) {
        if (!activated && drawnCard instanceof CatalystCard) {
            this.activated = true;
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

            Chemister.Flasks[] flasks = ((CatalystCard) drawnCard).getFlasks();
            addToBot(new InfuseAction(flasks[AbstractDungeon.cardRandomRng.random(flasks.length - 1)]));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
