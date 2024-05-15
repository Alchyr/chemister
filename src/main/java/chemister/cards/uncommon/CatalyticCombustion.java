package chemister.cards.uncommon;

import chemister.actions.CatalyticCombustionAction;
import chemister.actions.ResetCatalystAction;
import chemister.cards.CatalystCard;
import chemister.character.Chemister;
import chemister.powers.SeismicShellPower;
import chemister.util.CardStats;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class CatalyticCombustion extends CatalystCard {
    public static final String ID = makeID(CatalyticCombustion.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            10
    );

    private static final Chemister.Flasks[] flasks = new Chemister.Flasks[] {
            Chemister.Flasks.IGNIS
    };

    public CatalyticCombustion() {
        super(ID, info, flasks);

        setDamage(50);
        setCostUpgrade(8);
    }

    @Override
    public float getTitleFontSize() {
        return 20f;
    }

    @Override
    public boolean specialRender(SpriteBatch sb) {
        AbstractMonster target = CatalyticCombustionAction.getClosestTarget();
        if (target != null)
            target.renderReticle(sb);
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ResetCatalystAction(this));

        addToBot(new CatalyticCombustionAction(p, this.damage, this.damageTypeForTurn));
    }
}
