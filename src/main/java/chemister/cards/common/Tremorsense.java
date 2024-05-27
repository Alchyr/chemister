package chemister.cards.common;

import chemister.ChemisterMod;
import chemister.actions.infuse.InfuseAction;
import chemister.cards.BaseCard;
import chemister.cards.InfuseCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Tremorsense extends BaseCard implements InfuseCard {
    public static final String ID = makeID(Tremorsense.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.NONE,
            0
    );

    private static final Chemister.Flasks[] flasks = new Chemister.Flasks[] {
            Chemister.Flasks.AER,
            Chemister.Flasks.TERRA
    };

    public Tremorsense() {
        super(ID, info);

        setInnate(true);
        setMagic(2, 2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ScryAction(magicNumber));
        infuse(Chemister.Flasks.AER);

        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                if (AbstractDungeon.player.drawPile.isEmpty()) {
                    return;
                }
                AbstractCard top = AbstractDungeon.player.drawPile.getTopCard();
                if (top != null && ChemisterMod.getCardBase(top) >= 2) {
                    addToTop(new InfuseAction(Chemister.Flasks.TERRA));
                }
            }
        });
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
