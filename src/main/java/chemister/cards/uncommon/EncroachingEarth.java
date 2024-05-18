package chemister.cards.uncommon;

import basemod.helpers.CardModifierManager;
import chemister.actions.infuse.ApplyPowerToAllEnemiesDisplayAction;
import chemister.actions.infuse.DisplayableAction;
import chemister.cardmods.TemporaryCardmod;
import chemister.cards.ReagentCard;
import chemister.character.Chemister;
import chemister.relics.starter.FlaskRelic;
import chemister.util.CardStats;
import chemister.util.GeneralUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.List;

public class EncroachingEarth extends ReagentCard {
    public static final String ID = makeID(EncroachingEarth.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            -2
    );

    private static final Chemister.Flasks[] flasks = new Chemister.Flasks[] {
            Chemister.Flasks.TERRA
    };

    public EncroachingEarth() {
        super(ID, info);

        CardModifierManager.addModifier(this, new TemporaryCardmod(4, true));
    }

    @Override
    public Chemister.Flasks[] getFlasks() {
        return flasks;
    }

    @Override
    public ReagentEffect getEffect(FlaskRelic flask) {
        if (flask.flaskType() == Chemister.Flasks.TERRA) {
            return new ReagentEffect(ID, 1, 1) {
                @Override
                protected int getAmount(int infusedCount, List<Chemister.Flasks> infusedThisTurn, List<List<Chemister.Flasks>> infusedThisCombat) {
                    return maxAmount - GeneralUtils.countLayered(infusedThisCombat, Chemister.Flasks.TERRA);
                }

                @Override
                public DisplayableAction createAction(int amt) {
                    return new ApplyPowerToAllEnemiesDisplayAction((m)->new WeakPower(m, 2, false), true, AbstractGameAction.AttackEffect.NONE);
                }

                @Override
                public PowerTip getTip() {
                    return new PowerTip(text[0], maxAmount == 1 ? text[1] : String.format(text[2], maxAmount));
                }
            };
        }
        return null;
    }
}
