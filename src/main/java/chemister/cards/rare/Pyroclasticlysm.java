package chemister.cards.rare;

import chemister.actions.ResetCatalystAction;
import chemister.cards.CatalystCard;
import chemister.character.Chemister;
import chemister.powers.PyroclasticlysmPower;
import chemister.util.CardStats;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Pyroclasticlysm extends CatalystCard {
    public static final String ID = makeID(Pyroclasticlysm.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            10
    );

    private static final Chemister.Flasks[] flasks = new Chemister.Flasks[] {
            Chemister.Flasks.TERRA,
            Chemister.Flasks.IGNIS
    };

    public Pyroclasticlysm() {
        super(ID, info, flasks);

        setCostUpgrade(8);
        setMagic(2);
        setCustomVar("M2", 3);
        setCustomVar("M3", 7);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ResetCatalystAction(this));

        applySelf(new PyroclasticlysmPower(p, magicNumber, customVar("M2"), customVar("M3")));
    }
}
