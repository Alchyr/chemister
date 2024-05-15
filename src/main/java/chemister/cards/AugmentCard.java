package chemister.cards;

import chemister.ChemisterMod;
import chemister.character.Chemister;
import chemister.infuse.InfuseEffect;
import chemister.relics.starter.FlaskRelic;
import chemister.util.CardStats;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public abstract class AugmentCard extends BaseCard {
    public AugmentCard(String ID, CardStats info) {
        super(ID, info);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

    }

    public abstract Chemister.Flasks[] getFlasks();

    public abstract AugmentEffect getEffect(FlaskRelic flask);

    public static abstract class AugmentEffect extends InfuseEffect {
        public int maxAmount;

        /*
        Priorities:
        -5 - Clairvoyant Crosswinds
        -1 - Flat value buffs (no action)
        0 - Reagent base effects
        2 - Acidic Acetate
        4 - Seeping Solvent
        5 - Buffered
        6 - Echoes
        8 - Oxidizing
        10 - Bernoulli's
        99 - Fulminating
         */

        public final String[] text;

        public AugmentEffect(String ID, int priority, int amount) {
            super(ID, amount);
            this.priority = priority;
            text = CardCrawlGame.languagePack.getUIString(this.ID).TEXT;
            maxAmount = amount;
        }

        public AugmentEffect merge(AugmentEffect otherEffect) {
            if (otherEffect.ID.equals(ID)) {
                this.maxAmount += otherEffect.maxAmount;
            }
            else {
                ChemisterMod.logger.error("Attempted to merge AugmentEffects with different IDs; " + ID + " | " + otherEffect.ID);
            }
            return (AugmentEffect) super.merge(otherEffect);
        }

        public abstract PowerTip getTip();
    }
}
