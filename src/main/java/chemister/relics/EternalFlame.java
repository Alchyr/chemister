package chemister.relics;

import chemister.character.Chemister;

import static chemister.ChemisterMod.makeID;

public class EternalFlame extends BaseRelic {
    private static final String NAME = EternalFlame.class.getSimpleName();
    public static final String ID = makeID(NAME);

    public EternalFlame() {
        super(ID, NAME, Chemister.Meta.CARD_COLOR, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
