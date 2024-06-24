package chemister.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static chemister.ChemisterMod.makeID;

public class TripleThreatDebuff extends BasePower {
    public static final String POWER_ID = makeID("TripleThreatDebuff");
    private static final boolean TURN_BASED = false;

    public TripleThreatDebuff(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID, PowerType.BUFF, TURN_BASED, owner, source, amount);
    }

    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL && info.owner == this.source) {
            this.flash();
            this.addToTop(new ApplyPowerAction(this.source, this.source, new VigorPower(this.source, this.amount), this.amount, Settings.FAST_MODE));
        }

        return damageAmount;
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], amount);
    }
}
