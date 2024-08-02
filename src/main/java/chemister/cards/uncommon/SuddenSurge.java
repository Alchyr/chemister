package chemister.cards.uncommon;

import chemister.actions.DamageFromDrawnCardsAction;
import chemister.actions.infuse.DisplayableAction;
import chemister.actions.infuse.DrawDisplayAction;
import chemister.cards.BaseCard;
import chemister.cards.InfuseCard;
import chemister.character.Chemister;
import chemister.patches.TrackCardsDrawnDuringTurnPatch;
import chemister.util.CardStats;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.List;

public class SuddenSurge extends BaseCard implements InfuseCard {
    public static final String ID = makeID(SuddenSurge.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            2
    );

    private static final Chemister.Flasks[] flasks = new Chemister.Flasks[] {
            Chemister.Flasks.AQUA
    };

    private int flaskDraw = 0;

    public SuddenSurge() {
        super(ID, info);

        setDamage(0);
        setMagic(2);
        setExhaust(true, false);
    }

    @Override
    public void applyPowers() {
        baseDamage = (TrackCardsDrawnDuringTurnPatch.DRAWN_THIS_TURN + flaskDraw) * magicNumber;
        super.applyPowers();

        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster m) {
        super.calculateCardDamage(m);

        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    @Override
    public void onMoveToDiscard() {
        rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        infuse(Chemister.Flasks.AQUA);

        addToBot(new DamageFromDrawnCardsAction(this, magicNumber, m, damage > 15 ? AbstractGameAction.AttackEffect.SLASH_HEAVY : AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public boolean specialRender(SpriteBatch sb) {
        boolean returnVal = renderInfuseEffects(this, true, sb);

        int prevDraw = flaskDraw;

        flaskDraw = 0;
        for (List<DisplayableAction> displayable : flaskActions) {
            for (DisplayableAction action : displayable) {
                if (action instanceof DrawDisplayAction) {
                    flaskDraw += ((DrawDisplayAction) action).amount;
                }
            }
        }

        if (flaskDraw != prevDraw && AbstractDungeon.player.hand.contains(this)) {
            applyPowers();
        }

        return returnVal;
    }

    @Override
    public Chemister.Flasks[] getFlasks() {
        return flasks;
    }
}
