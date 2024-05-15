package chemister.cards.common;

import chemister.ChemisterMod;
import chemister.cards.BaseCard;
import chemister.cards.InfuseCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DustUp extends BaseCard {
    public static final String ID = makeID(DustUp.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            2
    );

    public DustUp() {
        super(ID, info);

        setDamage(13, 3);
        setBlock(7, 3);
    }

    @Override
    public void triggerOnGlowCheck() {
        if (ChemisterMod.infusedTypesThisTurn.contains(Chemister.Flasks.TERRA)) {
            if (!glowColor.equals(AbstractCard.GOLD_BORDER_GLOW_COLOR))
                this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            if (!glowColor.equals(AbstractCard.BLUE_BORDER_GLOW_COLOR))
                this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        damageSingle(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        if (ChemisterMod.infusedTypesThisTurn.contains(Chemister.Flasks.TERRA)) {
            block();
        }
    }
}
