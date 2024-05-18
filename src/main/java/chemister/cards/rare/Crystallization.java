package chemister.cards.rare;

import chemister.actions.ResetCatalystAction;
import chemister.cards.CatalystCard;
import chemister.character.Chemister;
import chemister.powers.CrystallizationPower;
import chemister.powers.PyroclasticlysmPower;
import chemister.util.CardStats;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Crystallization extends CatalystCard {
    public static final String ID = makeID(Crystallization.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            15
    );

    private static final Chemister.Flasks[] flasks = new Chemister.Flasks[] {
            Chemister.Flasks.TERRA,
            Chemister.Flasks.AQUA
    };

    public Crystallization() {
        super(ID, info, flasks);

        setCostUpgrade(12);
        setBlock(5);
        setMagic(3);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ResetCatalystAction(this));

        int finalBlock = this.block;
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                for (AbstractCard c : AbstractDungeon.player.hand.group) {
                    c.flash(Color.CYAN.cpy());
                    addToTop(new GainBlockAction(p, finalBlock));
                }
                isDone = true;
            }
        });

        applySelf(new CrystallizationPower(p, magicNumber));
    }
}
