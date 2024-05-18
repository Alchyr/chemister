package chemister.cards.rare;

import chemister.actions.ResetCatalystAction;
import chemister.cards.CatalystCard;
import chemister.character.Chemister;
import chemister.powers.ConvectionPower;
import chemister.util.CardStats;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Convection extends CatalystCard {
    public static final String ID = makeID(Convection.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            15
    );

    private static final Chemister.Flasks[] flasks = new Chemister.Flasks[] {
            Chemister.Flasks.IGNIS,
            Chemister.Flasks.AER
    };

    public Convection() {
        super(ID, info, flasks);

        setCostUpgrade(12);
        setMagic(3);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ResetCatalystAction(this));

        applySelf(new ConvectionPower(p, magicNumber));
    }
}
