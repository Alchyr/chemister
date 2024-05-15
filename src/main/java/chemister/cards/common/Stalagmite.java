package chemister.cards.common;

import chemister.cards.BaseCard;
import chemister.cards.InfuseCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Stalagmite extends BaseCard implements InfuseCard {
    public static final String ID = makeID(Stalagmite.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1
    );

    private static final Chemister.Flasks[] flasks = new Chemister.Flasks[] {
            Chemister.Flasks.TERRA
    };

    public Stalagmite() {
        super(ID, info);

        setDamage(6, 3);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        damageSingle(m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        infuse(Chemister.Flasks.TERRA);
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
