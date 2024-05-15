package chemister.cards.rare;

import basemod.helpers.CardModifierManager;
import chemister.actions.infuse.DisplayableAction;
import chemister.actions.infuse.InfuseAction;
import chemister.actions.infuse.PowerDisplayAction;
import chemister.cardmods.TemporaryCardmod;
import chemister.cards.AugmentCard;
import chemister.character.Chemister;
import chemister.powers.FulminatingIgnisPower;
import chemister.powers.FulminatingTerraPower;
import chemister.relics.starter.FlaskRelic;
import chemister.util.CardStats;
import chemister.util.GeneralUtils;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;

import java.util.List;

public class FulminatingPhial extends AugmentCard {
    public static final String ID = makeID(FulminatingPhial.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.NONE,
            -2
    );

    private static final Chemister.Flasks[] flasks = new Chemister.Flasks[] {
            Chemister.Flasks.TERRA,
            Chemister.Flasks.IGNIS
    };

    public FulminatingPhial() {
        super(ID, info);

        setMagic(2);
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public Chemister.Flasks[] getFlasks() {
        return flasks;
    }

    @Override
    public AugmentEffect getEffect(FlaskRelic flask) {
        if (flask.flaskType() == Chemister.Flasks.TERRA) {
            return new AugmentEffect(ID, 99, magicNumber) {
                @Override
                protected int getAmount(int infusedCount, List<Chemister.Flasks> infusedThisTurn, List<List<Chemister.Flasks>> infusedThisCombat) {
                    return maxAmount;
                }

                @Override
                public DisplayableAction createAction(int amt) {
                    return new PowerDisplayAction(AbstractDungeon.player, AbstractDungeon.player,
                            new FulminatingIgnisPower(AbstractDungeon.player, maxAmount));
                }

                @Override
                public PowerTip getTip() {
                    return new PowerTip(text[0], String.format(text[1], maxAmount));
                }
            };
        }
        else if (flask.flaskType() == Chemister.Flasks.IGNIS) {
            return new AugmentEffect(ID, 99, magicNumber) {
                @Override
                protected int getAmount(int infusedCount, List<Chemister.Flasks> infusedThisTurn, List<List<Chemister.Flasks>> infusedThisCombat) {
                    return maxAmount;
                }

                @Override
                public DisplayableAction createAction(int amt) {
                    return new PowerDisplayAction(AbstractDungeon.player, AbstractDungeon.player,
                            new FulminatingTerraPower(AbstractDungeon.player, maxAmount));
                }

                @Override
                public PowerTip getTip() {
                    return new PowerTip(text[0], String.format(text[2], maxAmount));
                }
            };
        }
        return null;
    }
}