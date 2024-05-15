package chemister.cards.uncommon;

import chemister.ChemisterMod;
import chemister.cards.BaseCard;
import chemister.cards.InfuseCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
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
        if (!unique.isEmpty())
            isBlockModified = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        block();
    }
}
