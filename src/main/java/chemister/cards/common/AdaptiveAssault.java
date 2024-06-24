package chemister.cards.common;

import chemister.cards.BaseCard;
import chemister.cards.InfuseCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class AdaptiveAssault extends BaseCard implements InfuseCard {
    public static final String ID = makeID(AdaptiveAssault.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            0
    );

    private static final Chemister.Flasks[] flasks = new Chemister.Flasks[] {
            Chemister.Flasks.TERRA,
            Chemister.Flasks.IGNIS
    };
    private static final Chemister.Flasks[] terra = new Chemister.Flasks[] {
            Chemister.Flasks.TERRA
    };
    private static final Chemister.Flasks[] ignis = new Chemister.Flasks[] {
            Chemister.Flasks.IGNIS
    };

    private Chemister.Flasks[] tempFlasks = flasks;

    public AdaptiveAssault() {
        super(ID, info);

        setDamage(2, 3);
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        tempFlasks = flasks;
    }

    @Override
    public void calculateCardDamage(AbstractMonster m) {
        super.calculateCardDamage(m);
        if (m != null && m.getIntentBaseDmg() >= 0) {
            tempFlasks = terra;
        }
        else {
            tempFlasks = ignis;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        damageSingle(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;

                if (m != null && m.getIntentBaseDmg() >= 0) {
                    infuseTop(Chemister.Flasks.TERRA);
                } else {
                    infuseTop(Chemister.Flasks.IGNIS);
                }
            }
        });
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
