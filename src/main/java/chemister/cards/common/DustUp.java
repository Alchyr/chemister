package chemister.cards.common;

import chemister.actions.DamageEqualToBlockAction;
import chemister.actions.infuse.BlockDisplayAction;
import chemister.actions.infuse.DisplayableAction;
import chemister.cards.BaseCard;
import chemister.cards.InfuseCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.List;

public class DustUp extends BaseCard implements InfuseCard {
    public static final String ID = makeID(DustUp.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            2
    );

    private static final Chemister.Flasks[] flasks = new Chemister.Flasks[] {
            Chemister.Flasks.TERRA
    };

    private int flaskBlock;

    public DustUp() {
        super(ID, info);

        setCostUpgrade(1);
        setDamage(0);
        setBlock(4);
    }

    @Override
    public void applyPowers() {
        super.applyPowers(); //calc block

        baseDamage = AbstractDungeon.player.currentBlock;
        baseDamage += block;
        baseDamage += flaskBlock;

        super.applyPowers();

        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster m) {
        super.calculateCardDamage(m);
        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    @Override
    public void onMoveToDiscard() {
        rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        infuse(Chemister.Flasks.TERRA);
        block();

        addToBot(new DamageEqualToBlockAction(this, m, damage > 15 ? AbstractGameAction.AttackEffect.BLUNT_HEAVY : AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public boolean specialRender(SpriteBatch sb) {
        boolean returnVal = renderInfuseEffects(this, true, sb);

        int prevBlock = flaskBlock;

        flaskBlock = 0;
        for (List<DisplayableAction> displayable : flaskActions) {
            for (DisplayableAction action : displayable) {
                if (action instanceof BlockDisplayAction) {
                    flaskBlock += ((BlockDisplayAction) action).amount;
                }
            }
        }

        if (flaskBlock != prevBlock && AbstractDungeon.player.hand.contains(this)) {
            applyPowers();
        }

        return returnVal;
    }

    @Override
    public Chemister.Flasks[] getFlasks() {
        return flasks;
    }
}
