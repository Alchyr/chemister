package chemister.relics.replace;

import chemister.ChemisterMod;
import chemister.actions.infuse.InfuseAction;
import chemister.character.Chemister;
import chemister.relics.BaseRelic;
import chemister.relics.starter.FlaskRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.OrangePellets;

import static chemister.ChemisterMod.makeID;

public class COrangePellets extends BaseRelic implements InfuseAction.PostInfuseTrigger {
    private static final String NAME = COrangePellets.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private static final String copyID = OrangePellets.ID;
    static {
        RelicStrings base = CardCrawlGame.languagePack.getRelicStrings(copyID);
        RelicStrings copy = CardCrawlGame.languagePack.getRelicStrings(ID);

        copy.NAME = base.NAME;
        copy.FLAVOR = base.FLAVOR;
    }

    private boolean triggered = false;

    public COrangePellets() {
        super(ID, "pellets.png", Chemister.Meta.CARD_COLOR, RelicTier.SHOP, LandingSound.CLINK);
    }


    @Override
    public void atPreBattle() {
        triggered = false;
        beginLongPulse();
    }


    @Override
    public void postInfusion(FlaskRelic flask) {
        if (!triggered) {
            int flags = 0b0000;
            for (Chemister.Flasks flaskType : ChemisterMod.infusedTypesThisTurn) {
                switch (flaskType) {
                    case AER:
                        flags |= 0b1000;
                        break;
                    case TERRA:
                        flags |= 0b0100;
                        break;
                    case IGNIS:
                        flags |= 0b0010;
                        break;
                    case AQUA:
                        flags |= 0b0001;
                        break;
                }
            }
            if (flags == 0b1111) {
                triggered = true;
                stopPulse();
                flash();

                addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                addToBot(new RemoveDebuffsAction(AbstractDungeon.player));
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
