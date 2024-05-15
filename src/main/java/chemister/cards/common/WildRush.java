package chemister.cards.common;

import chemister.cards.BaseCard;
import chemister.cards.InfuseCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;

public class WildRush extends BaseCard implements InfuseCard {
    public static final String ID = makeID(WildRush.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ALL_ENEMY,
            2
    );

    private static final Chemister.Flasks[] flasks = new Chemister.Flasks[] {
            Chemister.Flasks.AER
    };
    private static final Chemister.Flasks[] upgFlasks = new Chemister.Flasks[] {
            Chemister.Flasks.AER,
            Chemister.Flasks.AER
    };

    public WildRush() {
        super(ID, info);

        isMultiDamage = true;
        setDamage(10, 2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        infuse(Chemister.Flasks.AER);
        if (upgraded)
            infuse(Chemister.Flasks.AER);

        this.addToBot(new SFXAction("ATTACK_HEAVY"));
        this.addToBot(new VFXAction(p, new CleaveEffect(), 0.1F));
        damageAll(AbstractGameAction.AttackEffect.NONE);
    }

    @Override
    public boolean specialRender(SpriteBatch sb) {
        return renderInfuseEffects(this, true, sb);
    }

    @Override
    public Chemister.Flasks[] getFlasks() {
        return upgraded ? upgFlasks : flasks;
    }
}
