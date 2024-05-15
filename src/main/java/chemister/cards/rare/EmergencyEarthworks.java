package chemister.cards.rare;

import chemister.cards.BaseCard;
import chemister.cards.InfuseCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Arrays;

public class EmergencyEarthworks extends BaseCard implements InfuseCard {
    public static final String ID = makeID(EmergencyEarthworks.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            2
    );

    private Chemister.Flasks[] flasks = new Chemister.Flasks[] { };

    public EmergencyEarthworks() {
        super(ID, info);

        setMagic(1, 1);
    }

    @Override
    public float getTitleFontSize() {
        return 20f;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        calcInfusion();
    }

    @Override
    public void calculateCardDamage(AbstractMonster m) {
        super.calculateCardDamage(m);
        calcInfusion();
    }

    private void calcInfusion() {
        int amt = magicNumber;

        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped()) ++amt;
        }

        if (flasks.length != amt) {
            flasks = new Chemister.Flasks[amt];
            Arrays.fill(flasks, Chemister.Flasks.TERRA);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (Chemister.Flasks flask : flasks) {
            infuse(flask);
        }
    }

    @Override
    public boolean specialRender(SpriteBatch sb) {
        return renderInfuseEffects(this, false, sb);
    }

    @Override
    public Chemister.Flasks[] getFlasks() {
        return flasks;
    }
}
