package chemister.cards.uncommon;

import chemister.ChemisterMod;
import chemister.actions.infuse.InfuseAction;
import chemister.cards.BaseCard;
import chemister.cards.InfuseCard;
import chemister.character.Chemister;
import chemister.relics.starter.FlaskAqua;
import chemister.util.CardStats;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class SuddenSurge extends BaseCard implements InfuseCard {
    public static final String ID = makeID(SuddenSurge.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            0
    );

    private static final Chemister.Flasks[] flasks = new Chemister.Flasks[] {
            Chemister.Flasks.AQUA
    };

    public SuddenSurge() {
        super(ID, info);

        setDamage(0);
        setMagic(0, 1);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (upgraded)
            addToBot(new ScryAction(magicNumber));

        if (m != null) {
            AbstractCard src = this;
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    isDone = true;
                    AbstractRelic r = AbstractDungeon.player.getRelic(FlaskAqua.ID);
                    if (r instanceof FlaskAqua) {
                        ((FlaskAqua) r).addDrawFollowup(
                                (cards)->{
                                    int totalBase = 0;
                                    for (AbstractCard c : cards) {
                                        totalBase += ChemisterMod.getCardBase(c);
                                    }

                                    src.baseDamage = totalBase;
                                    src.calculateCardDamage(m);

                                    addToTop(new DamageAction(m, new DamageInfo(p, src.damage, src.damageTypeForTurn), AttackEffect.SLASH_DIAGONAL));

                                    src.baseDamage = src.damage = 0;
                                    src.applyPowers();
                                });
                    };
                }
            });
        }

        infuse(Chemister.Flasks.AQUA);
    }

    @Override
    public boolean specialRender(SpriteBatch sb) {
        return renderInfuseEffects(this, false, sb);
    }

    @Override
    public Chemister.Flasks[] getFlasks() {
        return flasks;
    }
}
