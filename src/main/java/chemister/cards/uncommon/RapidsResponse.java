package chemister.cards.uncommon;

import chemister.ChemisterMod;
import chemister.cards.BaseCard;
import chemister.character.Chemister;
import chemister.patches.NoUseVigorPatch;
import chemister.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RapidsResponse extends BaseCard {
    public static final String ID = makeID(RapidsResponse.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            2
    );

    public RapidsResponse() {
        super(ID, info);

        setDamage(13, 4);
        setMagic(2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        damageSingle(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);

        if (ChemisterMod.infusedTypesThisTurn.contains(Chemister.Flasks.AQUA)) {
            addToBot(new GainEnergyAction(magicNumber));
        }
    }
}
