package chemister.cards.uncommon;

import chemister.actions.QuicksilverBlitzAction;
import chemister.actions.ResetCatalystAction;
import chemister.cards.CatalystCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class QuicksilverBlitz extends CatalystCard {
    public static final String ID = makeID(QuicksilverBlitz.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            6
    );

    private static final Chemister.Flasks[] flasks = new Chemister.Flasks[] {
            Chemister.Flasks.AER
    };

    public QuicksilverBlitz() {
        super(ID, info, flasks);

        setCostUpgrade(5);
        setDamage(30);
        setMagic(5);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ResetCatalystAction(this));

        addToBot(new QuicksilverBlitzAction(m, new DamageInfo(p, damage, damageTypeForTurn), this.magicNumber));
    }
}
