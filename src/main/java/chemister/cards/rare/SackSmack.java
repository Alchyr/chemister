package chemister.cards.rare;

import chemister.actions.infuse.InfuseAction;
import chemister.cards.BaseCard;
import chemister.cards.InfuseCard;
import chemister.cards.WithdrawalCard;
import chemister.cards.special.InfuseChoiceCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SackSmack extends BaseCard implements InfuseCard {
    public static final String ID = makeID(SackSmack.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ENEMY,
            1
    );

    public SackSmack() {
        super(ID, info);

        setDamage(13);
        setMagic(2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        damageSingle(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);

        if (!upgraded) {
            for (int i = 0; i < magicNumber; ++i) {
                addToBot(new InfuseAction(Chemister.Flasks.values()[AbstractDungeon.cardRandomRng.random(Chemister.Flasks.values().length - 1)]));
            }
        }
        else {
            addToBot(new ChooseOneAction(makeOptions()));
        }
    }

    private ArrayList<AbstractCard> makeOptions() {
        ArrayList<AbstractCard> options = new ArrayList<>();

        for (Chemister.Flasks flask : Chemister.Flasks.values()) {
            options.add(new InfuseChoiceCard(flask, magicNumber));
        }

        return options;
    }

    @Override
    public boolean specialRender(SpriteBatch sb) {
        return renderInfuseEffects(this, false, sb);
    }

    @Override
    public Chemister.Flasks[] getFlasks() {
        return Chemister.Flasks.values();
    }
}
