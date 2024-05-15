package chemister.cards.uncommon;

import chemister.cards.BaseCard;
import chemister.cards.InfuseCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BlurPower;

import java.util.Arrays;

public class SturdySubstrate extends BaseCard implements InfuseCard {
    public static final String ID = makeID(SturdySubstrate.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    private Chemister.Flasks[] flasks;

    public SturdySubstrate() {
        super(ID, info);

        setMagic(1, 1);
        createFlasks();
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
        Arrays.fill(flasks, Chemister.Flasks.TERRA);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; ++i) {
            infuse(Chemister.Flasks.TERRA);
        }
        applySelf(new BlurPower(p, 1));
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
