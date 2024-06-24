package chemister.cards;

import chemister.ChemisterMod;
import chemister.powers.DilutionPower;
import chemister.powers.SneakySipPower;
import chemister.util.CardStats;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public abstract class WithdrawalCard extends BaseCard {
    public WithdrawalCard(String ID, CardStats info) {
        super(ID, info);
    }

    public WithdrawalCard(String ID, CardStats info, boolean upgradesDescription) {
        super(ID, info, upgradesDescription);
    }

    public WithdrawalCard(String ID, int cost, CardType cardType, CardTarget target, CardRarity rarity, CardColor color) {
        super(ID, cost, cardType, target, rarity, color);
    }

    public WithdrawalCard(String ID, int cost, CardType cardType, CardTarget target, CardRarity rarity, CardColor color, boolean upgradesDescription) {
        super(ID, cost, cardType, target, rarity, color, upgradesDescription);
    }

    @Override
    public void triggerOnGlowCheck() {
        if (!infusedThisTurn()) {
            if (!glowColor.equals(AbstractCard.GOLD_BORDER_GLOW_COLOR))
                this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            if (!glowColor.equals(AbstractCard.BLUE_BORDER_GLOW_COLOR))
                this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    public void queueWithdrawalEffect(AbstractPlayer p, AbstractMonster m) {
        int times = withdrawalEffectCount();
        for (int i = 0; i < times; ++i) {
            withdrawalEffect(p, m);
        }
    }
    public abstract void withdrawalEffect(AbstractPlayer p, AbstractMonster m);

    public static boolean infusedThisTurn() {
        if (AbstractDungeon.player != null) {
            for (AbstractPower pow : AbstractDungeon.player.powers) {
                if (pow instanceof DilutionPower) return false;
                if (pow instanceof SneakySipPower) return false;
            }
        }

        return ChemisterMod.infusedCountThisTurn > 0;
    }
    public static int withdrawalEffectCount() {
        int times = 1;
        for (AbstractPower pow : AbstractDungeon.player.powers) {
            if (pow instanceof IncreaseWithdrawalPower) {
                times += ((IncreaseWithdrawalPower) pow).increaseWithdrawalAmount();
            }
        }
        return times;
    }

    public interface IncreaseWithdrawalPower {
        int increaseWithdrawalAmount();
    }
}
