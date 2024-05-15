package chemister.cards.uncommon;

import chemister.ChemisterMod;
import chemister.cards.BaseCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.HashSet;
import java.util.Set;

public class VialVolley extends BaseCard {
    public static final String ID = makeID(VialVolley.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1
    );

    public VialVolley() {
        super(ID, info);

        setDamage(6, 2);
    }

    @Override
    public void applyPowers() {
        Set<Chemister.Flasks> unique = new HashSet<>(ChemisterMod.infusedTypesThisTurn);
        baseMagicNumber = 1;
        magicNumber = unique.size() + 1;
        super.applyPowers();

        isMagicNumberModified = magicNumber != baseMagicNumber;

        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster m) {
        Set<Chemister.Flasks> unique = new HashSet<>(ChemisterMod.infusedTypesThisTurn);
        baseMagicNumber = 1;
        magicNumber = unique.size() + 1;

        super.calculateCardDamage(m);

        isMagicNumberModified = magicNumber != baseMagicNumber;

        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    public void onMoveToDiscard() {
        magicNumber = baseMagicNumber = -1;
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; ++i) {
            //Use bouncing flask vfx or similar
            damageSingle(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        }
    }
}
