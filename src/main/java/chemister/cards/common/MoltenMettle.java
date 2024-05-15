package chemister.cards.common;

import chemister.cards.BaseCard;
import chemister.cards.InfuseCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MoltenMettle extends BaseCard implements InfuseCard {
    public static final String ID = makeID(MoltenMettle.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1
    );

    private static final Chemister.Flasks[] flasks = new Chemister.Flasks[] {
            Chemister.Flasks.IGNIS
    };

    public MoltenMettle() {
        super(ID, info);

        setBlock(7, 3);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        block();
        infuse(Chemister.Flasks.IGNIS);
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
