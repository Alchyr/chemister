package chemister.cards.special;

import chemister.actions.infuse.DisplayableAction;
import chemister.cards.AugmentCard;
import chemister.character.Chemister;
import chemister.relics.starter.FlaskRelic;
import chemister.util.CardStats;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.PowerTip;

public class Ashes extends AugmentCard {
    public static final String ID = makeID(Ashes.class.getSimpleName());
    private static final CardStats info = new CardStats(
            CardColor.COLORLESS,
            CardType.POWER,
            CardRarity.SPECIAL,
            CardTarget.NONE,
            -2
    );

    private static final Chemister.Flasks[] flasks = new Chemister.Flasks[] {
            Chemister.Flasks.IGNIS
    };

    public Ashes() {
        super(ID, info);

        setMagic(1);
        misc = 1;
    }

    @Override
    public void onLoadedMisc() {
        this.magicNumber = this.baseMagicNumber = misc;
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
        if (flask.flaskType() == Chemister.Flasks.IGNIS) {
            flask.counter += magicNumber;
            return new AugmentEffect(ID, -1, magicNumber) {
                @Override
                public DisplayableAction createAction(int amt) {
                    return null;
                }

                @Override
                public void applyValueChange(FlaskRelic flaskRelic) {
                    flaskRelic.counter += maxAmount;
                }

                @Override
                public PowerTip getTip() {
                    return new PowerTip(text[0], String.format(text[1], maxAmount));
                }
            };
        }
        return null;
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard copy = super.makeStatEquivalentCopy();
        copy.magicNumber = copy.baseMagicNumber = copy.misc;
        return copy;
    }
}
