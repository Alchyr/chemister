package chemister.cards.rare;

import chemister.actions.ResetCatalystAction;
import chemister.actions.infuse.InfuseAction;
import chemister.cards.BaseCard;
import chemister.cards.CatalystCard;
import chemister.cards.InfuseCard;
import chemister.cards.special.InfuseChoiceCard;
import chemister.character.Chemister;
import chemister.powers.PyroclasticlysmPower;
import chemister.util.CardStats;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class Pyroclasticlysm extends CatalystCard {
    public static final String ID = makeID(Pyroclasticlysm.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            15
    );

    private static final Chemister.Flasks[] flasks = new Chemister.Flasks[] {
            Chemister.Flasks.TERRA,
            Chemister.Flasks.IGNIS
    };

    public Pyroclasticlysm() {
        super(ID, info, flasks);

        setCostUpgrade(12);
        setMagic(3);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ResetCatalystAction(this));

        applySelf(new PyroclasticlysmPower(p, magicNumber));
    }
}
