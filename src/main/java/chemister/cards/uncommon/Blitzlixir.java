package chemister.cards.uncommon;

import chemister.cards.BaseCard;
import chemister.cards.InfuseCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Arrays;

public class Blitzlixir extends BaseCard implements InfuseCard {
    public static final String ID = makeID(Blitzlixir.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            1
    );

    private Chemister.Flasks[] flasks;

    public Blitzlixir() {
        super(ID, info);

        setMagic(2, 1);
        createFlasks();
        setExhaust(true);
    }

    @Override
    public void applyPowers() {
        super.applyPowers();

        if (magicNumber != flasks.length) {
            createFlasks();
        }
    }

    private void createFlasks() {
        flasks = new Chemister.Flasks[magicNumber];
        Arrays.fill(flasks, Chemister.Flasks.AER);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; ++i) {
            infuse(Chemister.Flasks.AER);
            drawCards(1);
        }
    }

    @Override
    public boolean specialRender(SpriteBatch sb) {
        return renderInfuseEffects(this, true, sb);
    }

    @Override
    public Chemister.Flasks[] getFlasks() {
        return flasks;
    }
}
