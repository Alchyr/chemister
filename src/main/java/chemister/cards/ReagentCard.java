package chemister.cards;

import basemod.helpers.TooltipInfo;
import chemister.ChemisterMod;
import chemister.character.Chemister;
import chemister.infuse.InfuseEffect;
import chemister.relics.starter.FlaskRelic;
import chemister.util.CardStats;
import chemister.util.KeywordInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

public abstract class ReagentCard extends BaseCard {
    private static final KeywordInfo reagentKeyword = ChemisterMod.keywords.get("reagent");
    private static final List<TooltipInfo> reagentKeywordTip;
    static {
        reagentKeywordTip = new ArrayList<>();
        reagentKeywordTip.add(new TooltipInfo(reagentKeyword.PROPER_NAME, reagentKeyword.DESCRIPTION));
    }

    public ReagentCard(String ID, CardStats info) {
        super(ID, info);
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

    }

    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        return reagentKeywordTip;
    }

    public abstract Chemister.Flasks[] getFlasks();

    public abstract ReagentEffect getEffect(FlaskRelic flask);

    public static abstract class ReagentEffect extends InfuseEffect {
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

        public ReagentEffect(String ID, int priority, int amount) {
            super(ID, amount);
            this.priority = priority;
            text = CardCrawlGame.languagePack.getUIString(this.ID).TEXT;
            maxAmount = amount;
        }

        public ReagentEffect merge(ReagentEffect otherEffect) {
            if (otherEffect.ID.equals(ID)) {
                this.maxAmount += otherEffect.maxAmount;
            }
            else {
                ChemisterMod.logger.error("Attempted to merge ReagentEffects with different IDs; " + ID + " | " + otherEffect.ID);
            }
            return (ReagentEffect) super.merge(otherEffect);
        }

        public abstract PowerTip getTip();
    }
}
