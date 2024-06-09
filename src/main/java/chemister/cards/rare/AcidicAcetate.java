package chemister.cards.rare;

import chemister.ChemisterMod;
import chemister.actions.infuse.DisplayableAction;
import chemister.cards.ReagentCard;
import chemister.character.Chemister;
import chemister.relics.starter.FlaskAqua;
import chemister.relics.starter.FlaskRelic;
import chemister.util.CardStats;
import chemister.util.GeneralUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import java.util.List;
import java.util.function.Consumer;

public class AcidicAcetate extends ReagentCard {
    public static final String ID = makeID(AcidicAcetate.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.NONE,
            -2
    );

    private static final Chemister.Flasks[] flasks = new Chemister.Flasks[] {
            Chemister.Flasks.AQUA
    };

    public AcidicAcetate() {
        super(ID, info);
    }

    @Override
    public Chemister.Flasks[] getFlasks() {
        return flasks;
    }

    @Override
    public ReagentEffect getEffect(FlaskRelic flask) {
        if (flask.flaskType() == Chemister.Flasks.AQUA) {
            return new AcidicAcetateReagentEffect();
        }
        return null;
    }

    public static class AcidicAcetateReagentEffect extends ReagentEffect implements FlaskAqua.AquaFollowup {
        private boolean isActive = false;

        public AcidicAcetateReagentEffect() {
            super(AcidicAcetate.ID, 2, 1);
        }

        @Override
        protected int getAmount(int infusedCount, List<Chemister.Flasks> infusedThisTurn, List<List<Chemister.Flasks>> infusedThisCombat) {
            int amt = maxAmount - GeneralUtils.count(infusedThisTurn, Chemister.Flasks.AQUA);
            isActive = amt > 0;
            return amt;
        }

        @Override
        public DisplayableAction createAction(int amt) {
            return null;
        }

        @Override
        public PowerTip getTip() {
            return new PowerTip(text[0], maxAmount == 1 ? text[1] : String.format(text[2], maxAmount));
        }

        @Override
        public Consumer<List<AbstractCard>> getFollowup() {
            if (isActive) {
                return (cards)->{
                    int totalBase = 0;
                    for (AbstractCard c : cards) {
                        totalBase += ChemisterMod.getCardBase(c);
                    }

                    if (totalBase > 0) {
                        AbstractDungeon.actionManager.addToTop(
                                new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new VigorPower(AbstractDungeon.player, totalBase),
                                        totalBase, true));
                    }
                };
            }
            return null;
        }
    }
}
