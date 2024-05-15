package chemister.cards.uncommon;

import chemister.actions.infuse.InfuseAction;
import chemister.cards.BaseCard;
import chemister.cards.InfuseCard;
import chemister.cards.WithdrawalCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import chemister.util.GeneralUtils;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BagBlock extends BaseCard implements InfuseCard {
    public static final String ID = makeID(BagBlock.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            2
    );

    public BagBlock() {
        super(ID, info);

        setBlock(8, 4);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        block();

        infuse(Chemister.Flasks.values()[AbstractDungeon.cardRandomRng.random(Chemister.Flasks.values().length - 1)]);
        infuse(Chemister.Flasks.values()[AbstractDungeon.cardRandomRng.random(Chemister.Flasks.values().length - 1)]);
    }

    @Override
    public boolean specialRender(SpriteBatch sb) {
        return renderInfuseEffects(this, false, sb);
    }

    @Override
    public Chemister.Flasks[] getFlasks() {
        return Chemister.Flasks.values();
    }
}
