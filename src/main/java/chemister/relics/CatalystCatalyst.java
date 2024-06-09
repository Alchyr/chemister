package chemister.relics;

import chemister.cards.CatalystCard;
import chemister.character.Chemister;

import static chemister.ChemisterMod.makeID;

public class CatalystCatalyst extends BaseRelic implements CatalystCard.CatalystDiscountRelic {
    private static final String NAME = CatalystCatalyst.class.getSimpleName();
    public static final String ID = makeID(NAME);

    public CatalystCatalyst() {
        super(ID, NAME, Chemister.Meta.CARD_COLOR, RelicTier.SHOP, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public int modifyCatalystDiscount(int discount) {
        return discount + 1;
    }
}
