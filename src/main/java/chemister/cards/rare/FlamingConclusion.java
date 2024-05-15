package chemister.cards.rare;

import chemister.actions.infuse.InfuseAction;
import chemister.cards.BaseCard;
import chemister.cards.InfuseCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.blue.Blizzard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Arrays;
import java.util.List;

public class FlamingConclusion extends BaseCard implements InfuseCard {
    public static final String ID = makeID(FlamingConclusion.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ENEMY,
            3
    );

    private Chemister.Flasks[] flasks = new Chemister.Flasks[] { };

    public FlamingConclusion() {
        super(ID, info);

        setDamage(28, 7);
        setMagic(7);
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
        int total = damage / magicNumber;
        /*List<AbstractMonster> monsters = AbstractDungeon.getMonsters().monsters;
        for (int i = 0; i < multiDamage.length; ++i) {
            if (i >= monsters.size()) break;

            if (!monsters.get(i).isDeadOrEscaped()) {
                total += multiDamage[i];
            }
        }

        total /= magicNumber;*/

        if (flasks.length != total) {
            flasks = new Chemister.Flasks[total];
            Arrays.fill(flasks, Chemister.Flasks.IGNIS);
        }

        rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0] + total + cardStrings.EXTENDED_DESCRIPTION[1];
        initializeDescription();
    }

    public void onMoveToDiscard() {
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //damageAll(AbstractGameAction.AttackEffect.FIRE);
        damageSingle(m, AbstractGameAction.AttackEffect.FIRE);
        for (Chemister.Flasks flask : flasks) {
            infuse(flask);
        }
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
