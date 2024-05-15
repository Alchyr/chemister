package chemister.cards.uncommon;

import chemister.cards.BaseCard;
import chemister.cards.InfuseCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.PutOnDeckAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Brainstorm extends BaseCard implements InfuseCard {
    public static final String ID = makeID(Brainstorm.class.getSimpleName());
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

    public Brainstorm() {
        super(ID, info);

        setMagic(2, 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        drawCards(magicNumber);
        addToBot(new PutOnDeckAction(p, p, 1, false));
        infuse(Chemister.Flasks.AER);
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
