package chemister.cards.uncommon;

import chemister.cards.BaseCard;
import chemister.character.Chemister;
import chemister.patches.NoUseVigorPatch;
import chemister.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class TertiaryIngredient extends BaseCard {
    public static final String ID = makeID(TertiaryIngredient.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            3
    );

    public TertiaryIngredient() {
        super(ID, info);

        setDamage(33, 9);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (AbstractDungeon.actionManager.cardsPlayedThisTurn.size() < 2) {
            this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
            return false;
        }
        if (!upgraded && AbstractDungeon.actionManager.cardsPlayedThisTurn.size() > 2) {
            this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[1];
            return false;
        }
        return super.canUse(p, m);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        damageSingle(m, AbstractGameAction.AttackEffect.SMASH);
    }
}
