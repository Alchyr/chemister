package chemister.relics;

import chemister.actions.infuse.InfuseAction;
import chemister.character.Chemister;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static chemister.ChemisterMod.makeID;

public class FragileFlasks extends BaseRelic {
    private static final String NAME = FragileFlasks.class.getSimpleName();
    public static final String ID = makeID(NAME);

    public FragileFlasks() {
        super(ID, NAME, Chemister.Meta.CARD_COLOR, RelicTier.RARE, LandingSound.CLINK);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.owner != null && info.type == DamageInfo.DamageType.NORMAL && damageAmount > 0) {
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new InfuseAction());
        }
        return damageAmount;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
