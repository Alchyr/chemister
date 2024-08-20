package chemister.cards.uncommon;

import chemister.ChemisterMod;
import chemister.cards.BaseCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.HashSet;
import java.util.Set;

public class SuperbSolution extends BaseCard {
    public static final String ID = makeID(SuperbSolution.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    public SuperbSolution() {
        super(ID, info);

        setBlock(5, 2);
        setMagic(2, 1);
    }

    @Override
    public void applyPowers() {
        int origBase = baseBlock;

        Set<Chemister.Flasks> unique = new HashSet<>(ChemisterMod.infusedTypesThisTurn);
        baseBlock += unique.size() * magicNumber;

        super.applyPowers();

        baseBlock = origBase;
        isBlockModified = baseBlock != block;

        String newDesc = this.cardStrings.DESCRIPTION + String.format(this.cardStrings.EXTENDED_DESCRIPTION[0], unique.size());
        if (!rawDescription.equals(newDesc)) {
            rawDescription = newDesc;
            initializeDescription();
        }
    }

    @Override
    public void calculateCardDamage(AbstractMonster m) {
        int origBase = baseBlock;

        Set<Chemister.Flasks> unique = new HashSet<>(ChemisterMod.infusedTypesThisTurn);
        baseBlock += unique.size() * magicNumber;

        super.calculateCardDamage(m);

        baseBlock = origBase;
        isBlockModified = baseBlock != block;

        String newDesc = this.cardStrings.DESCRIPTION + String.format(this.cardStrings.EXTENDED_DESCRIPTION[0], unique.size());
        if (!rawDescription.equals(newDesc)) {
            rawDescription = newDesc;
            initializeDescription();
        }
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        block();
    }
}
