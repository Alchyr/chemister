package chemister.cards.common;

import chemister.ChemisterMod;
import chemister.cards.BaseCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BlisterBlast extends BaseCard {
    public static final String ID = makeID(BlisterBlast.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1
    );

    public BlisterBlast() {
        super(ID, info);

        setDamage(8, 3);
        setMagic(1);

        setCustomVar("2M", VariableType.MAGIC, baseMagicNumber * 2,
                (card, m, base)->{
                    card.setCustomVarValue("2M", card.baseMagicNumber * 2, 0);
                    return card.magicNumber * 2;
                });
    }

    @Override
    public void triggerOnGlowCheck() {
        if (ChemisterMod.infusedTypesThisTurn.contains(Chemister.Flasks.IGNIS)) {
            if (!glowColor.equals(AbstractCard.GOLD_BORDER_GLOW_COLOR))
                this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            if (!glowColor.equals(AbstractCard.BLUE_BORDER_GLOW_COLOR))
                this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        damageSingle(m, AbstractGameAction.AttackEffect.FIRE);
        applySingle(m, getVuln(m,
                ChemisterMod.infusedTypesThisTurn.contains(Chemister.Flasks.IGNIS) ?
                        customVar("2M") : magicNumber));
    }
}
