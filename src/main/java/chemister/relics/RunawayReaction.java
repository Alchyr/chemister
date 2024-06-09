package chemister.relics;

import chemister.actions.infuse.InfuseAction;
import chemister.character.Chemister;
import chemister.relics.starter.FlaskRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static chemister.ChemisterMod.makeID;

public class RunawayReaction extends BaseRelic implements InfuseAction.PostInfuseTrigger {
    private static final String NAME = RunawayReaction.class.getSimpleName();
    public static final String ID = makeID(NAME);

    public static final int DAMAGE = 3;

    public RunawayReaction() {
        super(ID, NAME, Chemister.Meta.CARD_COLOR, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(DESCRIPTIONS[0], DAMAGE);
    }

    @Override
    public void postInfusion(FlaskRelic flask) {
        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new DamageRandomEnemyAction(new DamageInfo(AbstractDungeon.player, DAMAGE, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
    }
}
