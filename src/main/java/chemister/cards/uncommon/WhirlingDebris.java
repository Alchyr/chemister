package chemister.cards.uncommon;

import chemister.cards.BaseCard;
import chemister.cards.InfuseCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class WhirlingDebris extends BaseCard implements InfuseCard {
    public static final String ID = makeID(WhirlingDebris.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ALL_ENEMY,
            1
    );

    private static final Chemister.Flasks[] flasks = new Chemister.Flasks[] {
            Chemister.Flasks.TERRA,
            Chemister.Flasks.TERRA,
    };
    private static final Chemister.Flasks[] none = new Chemister.Flasks[] {
    };

    public WhirlingDebris() {
        super(ID, info);

        isMultiDamage = true;
        setDamage(8, 3);
        setMagic(2);
    }

    private boolean bonusActive() {
        if (AbstractDungeon.currMapNode == null || AbstractDungeon.getCurrRoom() == null || AbstractDungeon.getMonsters() == null) return false;

        int enemyCount = 0;

        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped()) {
                ++enemyCount;
            }
        }
        return enemyCount >= magicNumber;
    }

    @Override
    public void triggerOnGlowCheck() {
        if (bonusActive()) {
            if (!glowColor.equals(AbstractCard.GOLD_BORDER_GLOW_COLOR))
                this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            if (!glowColor.equals(AbstractCard.BLUE_BORDER_GLOW_COLOR))
                this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        damageAll(AbstractGameAction.AttackEffect.BLUNT_HEAVY);

        if (bonusActive()) {
            infuse(Chemister.Flasks.TERRA);
            infuse(Chemister.Flasks.TERRA);
        }
    }

    @Override
    public boolean specialRender(SpriteBatch sb) {
        return renderInfuseEffects(this, true, sb);
    }

    @Override
    public Chemister.Flasks[] getFlasks() {
        return bonusActive() ? flasks : none;
    }
}
