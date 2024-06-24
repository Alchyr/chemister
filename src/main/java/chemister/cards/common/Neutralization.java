package chemister.cards.common;

import chemister.ChemisterMod;
import chemister.actions.ResetCatalystAction;
import chemister.cards.CatalystCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Neutralization extends CatalystCard {
    public static final String ID = makeID(Neutralization.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            3
    );

    private static final Chemister.Flasks[] flasks = new Chemister.Flasks[] {
            Chemister.Flasks.TERRA,
            Chemister.Flasks.IGNIS,
            Chemister.Flasks.AQUA,
            Chemister.Flasks.AER
    };

    public Neutralization() {
        super(ID, info, flasks);

        setCostUpgrade(2);
        setDamage(0);
    }

    @Override
    public void applyPowers() {
        baseDamage = 0;
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            baseDamage += ChemisterMod.getCardBase(c);
        }
        super.applyPowers();

        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster m) {
        baseDamage = 0;
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            baseDamage += ChemisterMod.getCardBase(c);
        }
        super.calculateCardDamage(m);

        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    public void onMoveToDiscard() {
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ResetCatalystAction(this));

        damageSingle(m, damage > 10 ? AbstractGameAction.AttackEffect.SLASH_HEAVY : AbstractGameAction.AttackEffect.SLASH_VERTICAL);
    }
}
