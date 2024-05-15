package chemister.cards.common;

import chemister.cards.WithdrawalCard;
import chemister.character.Chemister;
import chemister.patches.BetterPowerNegationCheckPatch;
import chemister.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class PotentMiasma extends WithdrawalCard {
    public static final String ID = makeID(PotentMiasma.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1
    );

    public PotentMiasma() {
        super(ID, info);

        setMagic(1, 1);
        setCustomVar("STR", 3, 2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applySingle(m, getWeak(m, magicNumber));
        if (!infusedThisTurn()) {
            queueWithdrawalEffect(p, m);
        }
    }

    @Override
    public void withdrawalEffect(AbstractPlayer p, AbstractMonster m) {
        int debuff = customVar("STR");
        ApplyPowerAction debuffAction = applySingle(m, new StrengthPower(m, -debuff));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;

                if (BetterPowerNegationCheckPatch.Field.appliedSuccess.get(debuffAction)) {
                    addToTop(new ApplyPowerAction(m, p, new GainStrengthPower(m, debuff)));
                }
            }
        });
    }
}
