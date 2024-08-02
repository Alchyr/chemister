package chemister.cards.common;

import chemister.actions.ResetCatalystAction;
import chemister.cards.CatalystCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class TripleThreat extends CatalystCard {
    public static final String ID = makeID(TripleThreat.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            3
    );

    private static final Chemister.Flasks[] flasks = new Chemister.Flasks[] {
            Chemister.Flasks.TERRA,
            Chemister.Flasks.IGNIS,
            Chemister.Flasks.AQUA,
            Chemister.Flasks.AER
    };

    public TripleThreat() {
        super(ID, info, flasks);

        setCostUpgrade(2);

        setDamage(4);
        setMagic(3);
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ResetCatalystAction(this));

        for (int i = 0; i < magicNumber; ++i) {
            damageSingle(m, damage > 10 ? AbstractGameAction.AttackEffect.SLASH_HEAVY :
                    (MathUtils.randomBoolean() ? AbstractGameAction.AttackEffect.SLASH_VERTICAL : AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }
    }
}
