package chemister.cards.rare;

import chemister.ChemisterMod;
import chemister.actions.EffervescenceAction;
import chemister.actions.ResetCatalystAction;
import chemister.cards.CatalystCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Effervescence extends CatalystCard {
    public static final String ID = makeID(Effervescence.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.NONE,
            15
    );

    private static final Chemister.Flasks[] flasks = new Chemister.Flasks[] {
            Chemister.Flasks.AQUA,
            Chemister.Flasks.AER
    };

    public Effervescence() {
        super(ID, info, flasks);

        setCostUpgrade(12);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ResetCatalystAction(this));

        int amt = ChemisterMod.getCardBase(this);
        addToBot(new EffervescenceAction(amt, true));
    }
}
