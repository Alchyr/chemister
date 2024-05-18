package chemister.powers;

import chemister.ChemisterMod;
import chemister.util.GeneralUtils;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

import static chemister.ChemisterMod.makeID;

public class ConvectionPower extends BasePower {
    public static final String POWER_ID = makeID(ConvectionPower.class.getSimpleName());
    private static final boolean TURN_BASED = true;

    private List<AbstractCard> copies = new ArrayList<>();

    public ConvectionPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, TURN_BASED, owner, amount);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        int cost = GeneralUtils.getLogicalCardCost(card);
        if (!copies.contains(card) && cost < ChemisterMod.getCardBase(card)) {
            this.flash();
            AbstractMonster m = null;
            if (action.target instanceof AbstractMonster) {
                m = (AbstractMonster)action.target;
            }

            AbstractCard tmp = card.makeSameInstanceOf();
            AbstractDungeon.player.limbo.addToBottom(tmp);
            tmp.current_x = card.current_x;
            tmp.current_y = card.current_y;
            tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
            tmp.target_y = (float)Settings.HEIGHT / 2.0F;
            if (m != null) {
                tmp.calculateCardDamage(m);
            }
            else {
                tmp.applyPowers();
            }

            tmp.purgeOnUse = true;
            copies.add(tmp);
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);
        }
    }

    public void atStartOfTurn() {
        copies.clear();
        addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
    }

    @Override
    public void updateDescription() {
        this.description =
                amount == 1 ? DESCRIPTIONS[0] :
                        String.format(DESCRIPTIONS[1], amount);
    }
}
