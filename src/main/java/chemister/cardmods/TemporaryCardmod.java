package chemister.cardmods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import static chemister.ChemisterMod.makeID;

public class TemporaryCardmod extends AbstractCardModifier {
    public static final String ID = makeID("TemporaryCard");
    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    private final boolean isInherent;
    private int remainingDuration = 0;

    public TemporaryCardmod(int duration, boolean inherent) {
        this.isInherent = inherent;
        remainingDuration = duration;
    }

    @Override
    public boolean isInherent(AbstractCard card) {
        return isInherent;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + String.format(TEXT[0], remainingDuration);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new TemporaryCardmod(remainingDuration, isInherent);
    }

    public boolean age(AbstractCard c) {
        --remainingDuration;
        c.initializeDescription();
        if (remainingDuration <= 0) {
            remainingDuration = 0;
            return true;
        }
        return false;
    }
}
