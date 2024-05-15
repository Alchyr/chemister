package chemister.cards.uncommon;

import chemister.actions.infuse.InfuseAction;
import chemister.cards.BaseCard;
import chemister.cards.InfuseCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Thermoblast extends BaseCard implements InfuseCard {
    public static final String ID = makeID(Thermoblast.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            2
    );

    private static final Chemister.Flasks[] flasks = new Chemister.Flasks[] {
            Chemister.Flasks.IGNIS
    };

    public Thermoblast() {
        super(ID, info);

        setDamage(18, 6);
        setMagic(1, 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean wasAlive = !m.isDeadOrEscaped();
        damageSingle(m, AbstractGameAction.AttackEffect.FIRE);
        int amt = magicNumber;
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                if (wasAlive && m.isDeadOrEscaped()) {
                    for (int i = 0; i < amt; ++i) {
                        addToTop(new InfuseAction(Chemister.Flasks.IGNIS));
                    }
                }
            }
        });
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
