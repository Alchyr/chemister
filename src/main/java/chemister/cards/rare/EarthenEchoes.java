package chemister.cards.rare;

import chemister.actions.infuse.DisplaySkillDuplicateAction;
import chemister.actions.infuse.DisplayableAction;
import chemister.cards.AugmentCard;
import chemister.character.Chemister;
import chemister.relics.starter.FlaskRelic;
import chemister.util.CardStats;
import chemister.util.GeneralUtils;
import com.megacrit.cardcrawl.helpers.PowerTip;

import java.util.List;

public class EarthenEchoes extends AugmentCard {
    public static final String ID = makeID(EarthenEchoes.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.NONE,
            -2
    );

    private static final Chemister.Flasks[] flasks = new Chemister.Flasks[] {
            Chemister.Flasks.TERRA,
            Chemister.Flasks.AER
    };

    public EarthenEchoes() {
        super(ID, info);
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
        if (GeneralUtils.arrContains(flasks, flask.flaskType())) {
            return new AugmentEffect(ID, 6, 1) {
                @Override
                protected int getAmount(int infusedCount, List<Chemister.Flasks> infusedThisTurn, List<List<Chemister.Flasks>> infusedThisCombat) {
                    //Man this is gross, but I'm not rewriting this system
                    int possibleTriggers = 0;
                    int flags = 0;

                    for (List<Chemister.Flasks> turnFlasks : infusedThisCombat) {
                        flags = 0;

                        for (Chemister.Flasks infused : turnFlasks) {
                            switch (infused) {
                                case TERRA:
                                    flags |= 1;
                                    break;
                                case AER:
                                    flags |= 2;
                                    break;
                            }
                            if (flags == 3) {
                                flags = 0;
                                ++possibleTriggers;
                            }
                        }
                    }

                    if (maxAmount - possibleTriggers > 0) {
                        switch (flask.flaskType()) {
                            case TERRA:
                                flags |= 1;
                                break;
                            case AER:
                                flags |= 2;
                                break;
                        }
                        if (flags == 3) {
                            return 1;
                        }
                    }
                    return 0;
                }

                @Override
                public DisplayableAction createAction(int amt) {
                    return new DisplaySkillDuplicateAction();
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
