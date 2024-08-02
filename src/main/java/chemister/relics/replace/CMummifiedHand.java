package chemister.relics.replace;

import chemister.actions.infuse.InfuseAction;
import chemister.character.Chemister;
import chemister.relics.BaseRelic;
import chemister.relics.starter.FlaskRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.MummifiedHand;

import java.util.ArrayList;

import static chemister.ChemisterMod.makeID;

public class CMummifiedHand extends BaseRelic implements InfuseAction.PostInfuseTrigger {
    private static final String NAME = CMummifiedHand.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private static final String copyID = MummifiedHand.ID;
    static {
        RelicStrings base = CardCrawlGame.languagePack.getRelicStrings(copyID);
        RelicStrings copy = CardCrawlGame.languagePack.getRelicStrings(ID);

        copy.NAME = base.NAME;
        copy.FLAVOR = base.FLAVOR;
    }

    private boolean canTrigger = true;
    private boolean firstTurn = false;

    public CMummifiedHand() {
        super(ID, "mummifiedHand.png", Chemister.Meta.CARD_COLOR, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public void atPreBattle() {
        canTrigger = true;
        firstTurn = true;
        beginLongPulse();
    }

    @Override
    public void atTurnStart() {
        if (!firstTurn) {
            canTrigger = true;
            beginLongPulse();
        }
    }

    @Override
    public void onPlayerEndTurn() {
        firstTurn = false;
    }

    @Override
    public void postInfusion(FlaskRelic flask) {
        if (canTrigger) {
            canTrigger = false;
            flash();
            addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));

            //copying mummified hand code
            ArrayList<AbstractCard> groupCopy = new ArrayList<>();

            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if (c.cost > 0 && c.costForTurn > 0 && !c.freeToPlayOnce) {
                    groupCopy.add(c);
                }
            }
            for (CardQueueItem i : AbstractDungeon.actionManager.cardQueue) {
                if (i.card != null) {
                    groupCopy.remove(i.card);
                }
            }

            if (!groupCopy.isEmpty()) {
                groupCopy.get(AbstractDungeon.cardRandomRng.random(0, groupCopy.size() - 1)).setCostForTurn(0);
            }
        }
    }


    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
