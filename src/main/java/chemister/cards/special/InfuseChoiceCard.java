package chemister.cards.special;

import chemister.actions.infuse.InfuseAction;
import chemister.cards.BaseCard;
import chemister.cards.InfuseCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Arrays;

public class InfuseChoiceCard extends BaseCard implements InfuseCard {
    public static final String ID = makeID(InfuseChoiceCard.class.getSimpleName());
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.SKILL,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2
    );

    private static final String[] FLASK_TEXT = CardCrawlGame.languagePack.getUIString(makeID("Flasks")).TEXT;
    public static String getInfuseName(Chemister.Flasks flask) {
        String flaskName;
        switch (flask) {
            case IGNIS:
                flaskName = FLASK_TEXT[0];
                break;
            case TERRA:
                flaskName = FLASK_TEXT[1];
                break;
            case AQUA:
                flaskName = FLASK_TEXT[2];
                break;
            case AER:
                flaskName = FLASK_TEXT[3];
                break;
            default:
                flaskName = "???";
                break;
        }
        return flaskName;
    }

    private static final Chemister.Flasks[] none = new Chemister.Flasks[] { };
    private Chemister.Flasks[] flask = none;

    public InfuseChoiceCard() {
        super(ID, info);
    }

    public InfuseChoiceCard(Chemister.Flasks flask, int amt) {
        this();

        setMagic(amt);
        this.flask = new Chemister.Flasks[amt];
        Arrays.fill(this.flask, flask);

        this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[0] + getInfuseName(flask) + cardStrings.EXTENDED_DESCRIPTION[1];
        initializeDescription();
    }

    @Override
    public void onChoseThisOption() {
        for (Chemister.Flasks flask : this.flask) {
            addToTop(new InfuseAction(flask));
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (Chemister.Flasks flask : this.flask) {
            infuse(flask);
        }
    }

    @Override
    public boolean specialRender(SpriteBatch sb) {
        return renderInfuseEffects(this, false, sb);
    }

    @Override
    public Chemister.Flasks[] getFlasks() {
        return flask;
    }
}
