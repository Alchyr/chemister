package chemister.cards.uncommon;

import chemister.cards.BaseCard;
import chemister.cards.InfuseCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Hypothesize extends BaseCard implements InfuseCard {
    public static final String ID = makeID(Hypothesize.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            1
    );

    private static final Chemister.Flasks[] flasks = new Chemister.Flasks[] {
            Chemister.Flasks.AER
    };
    private static final Chemister.Flasks[] upgFlasks = new Chemister.Flasks[] {
            Chemister.Flasks.AER,
            Chemister.Flasks.AQUA
    };

    public Hypothesize() {
        super(ID, info);

        setMagic(2, 2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ScryAction(magicNumber));
        infuse(Chemister.Flasks.AER);
        if (upgraded)
            infuse(Chemister.Flasks.AQUA);
    }

    @Override
    public boolean specialRender(SpriteBatch sb) {
        return renderInfuseEffects(this, true, sb);
    }

    @Override
    public Chemister.Flasks[] getFlasks() {
        return upgraded ? upgFlasks : flasks;
    }
}
