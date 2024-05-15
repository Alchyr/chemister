package chemister.cards.uncommon;

import chemister.cards.BaseCard;
import chemister.character.Chemister;
import chemister.powers.FlaskFracturePower;
import chemister.util.CardStats;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.PotionBounceEffect;

public class FlaskFracture extends BaseCard {
    public static final String ID = makeID(FlaskFracture.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1
    );

    public FlaskFracture() {
        super(ID, info);

        setDamage(13, 4);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null)
            vfx(new PotionBounceEffect(p.hb.cX, p.hb.cY, m.hb.cX, m.hb.cY), 0.4F);
        damageSingle(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        applySelf(new FlaskFracturePower(p, 1));
    }
}
