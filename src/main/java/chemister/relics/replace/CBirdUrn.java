package chemister.relics.replace;

import chemister.cards.ReagentCard;
import chemister.character.Chemister;
import chemister.relics.BaseRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.BirdFacedUrn;

import static chemister.ChemisterMod.makeID;

public class CBirdUrn extends BaseRelic {
    private static final String NAME = CBirdUrn.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private static final String copyID = BirdFacedUrn.ID;
    static {
        RelicStrings base = CardCrawlGame.languagePack.getRelicStrings(copyID);
        RelicStrings copy = CardCrawlGame.languagePack.getRelicStrings(ID);

        copy.NAME = base.NAME;
        copy.FLAVOR = base.FLAVOR;
    }

    public CBirdUrn() {
        super(ID, "bird_urn.png", Chemister.Meta.CARD_COLOR, RelicTier.RARE, LandingSound.SOLID);
    }

    @Override
    public void onObtainCard(AbstractCard c) {
        if (c instanceof ReagentCard) {
            flash();
            AbstractDungeon.player.increaseMaxHp(2, true);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
