package chemister.cards.uncommon;

import basemod.BaseMod;
import chemister.actions.ResetCatalystAction;
import chemister.cards.CatalystCard;
import chemister.character.Chemister;
import chemister.powers.SeismicShellPower;
import chemister.util.CardStats;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.unique.ExpertiseAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MomentousMaelstrom extends CatalystCard {
    public static final String ID = makeID(MomentousMaelstrom.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            10
    );

    private static final Chemister.Flasks[] flasks = new Chemister.Flasks[] {
            Chemister.Flasks.AQUA
    };

    public MomentousMaelstrom() {
        super(ID, info, flasks);

        setCostUpgrade(8);
    }

    @Override
    public float getTitleFontSize() {
        return 20f;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ResetCatalystAction(this));

        addToBot(new ExpertiseAction(p, BaseMod.MAX_HAND_SIZE));
        addToBot(new GainEnergyAction(AbstractDungeon.player.energy.energy));
    }
}
