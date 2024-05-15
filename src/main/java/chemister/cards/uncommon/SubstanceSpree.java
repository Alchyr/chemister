package chemister.cards.uncommon;

import chemister.actions.XCostAction;
import chemister.cards.BaseCard;
import chemister.cards.InfuseCard;
import chemister.cards.WithdrawalCard;
import chemister.character.Chemister;
import chemister.util.CardStats;
import chemister.vfx.ExpandCleaveEffect;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SubstanceSpree extends WithdrawalCard {
    public static final String ID = makeID(SubstanceSpree.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ALL_ENEMY,
            -1
    );


    public SubstanceSpree() {
        super(ID, info);

        isMultiDamage = true;
        setDamage(5, 3);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int bonus = 0;
        if (!infusedThisTurn()) {
            bonus = withdrawalEffectCount();
        }

        addToBot(new XCostAction(this, (amt, params)->{
            for (int i = 0; i < params[0]; ++i) {
                addToTop(new GainEnergyAction(amt / 2));
            }

            for (int i = 0; i < amt; ++i) {
                addToTop(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, true));
                addToTop(new VFXAction(new ExpandCleaveEffect(p, new Color(MathUtils.random(0.3f, 1.0f), MathUtils.random(0.3f, 1.0f), MathUtils.random(0.3f, 1.0f), 0.8f)), Settings.FAST_MODE ? 0.15f : 0.3f));
            }

            return true;
        }, bonus));
    }

    @Override
    public void withdrawalEffect(AbstractPlayer p, AbstractMonster m) {

    }
}
