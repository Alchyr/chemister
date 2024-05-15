package chemister.cards.uncommon;

import chemister.cards.BaseCard;
import chemister.cards.InfuseCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class InvigoratingInfusion extends BaseCard implements InfuseCard {
    public static final String ID = makeID(InvigoratingInfusion.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            1
    );

    private static final Chemister.Flasks[] flasks = new Chemister.Flasks[] {
            Chemister.Flasks.IGNIS
    };

    public InvigoratingInfusion() {
        super(ID, info);

        setMagic(1, 1);
    }

    @Override
    public float getTitleFontSize() {
        return 20f;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        infuse(Chemister.Flasks.IGNIS);
        addToBot(new GainEnergyAction(magicNumber));
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
