package chemister.cards.rare;

import chemister.ChemisterMod;
import chemister.actions.XCostAction;
import chemister.actions.infuse.InfuseAction;
import chemister.cards.BaseCard;
import chemister.cards.InfuseCard;
import chemister.character.Chemister;
import chemister.relics.starter.FlaskAqua;
import chemister.util.CardStats;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.Arrays;

public class CascadeCrush extends BaseCard implements InfuseCard {
    public static final String ID = makeID(CascadeCrush.class.getSimpleName());
    private static final CardStats info = new CardStats(
            Chemister.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ALL_ENEMY,
            -1
    );

    private Chemister.Flasks[] flasks = new Chemister.Flasks[] { };

    public CascadeCrush() {
        super(ID, info);

        isMultiDamage = true;

        setInnate(false, true);
        setDamage(24);
        setMagic(1, 0);
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        calcInfusion();
    }

    @Override
    public void calculateCardDamage(AbstractMonster m) {
        super.calculateCardDamage(m);
        calcInfusion();
    }

    private void calcInfusion() {
        int amt = Math.max(EnergyPanel.totalCount, 0);

        if (flasks.length != amt) {
            flasks = new Chemister.Flasks[amt];
            Arrays.fill(flasks, Chemister.Flasks.AQUA);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new XCostAction(this,
                (amt, params)->{
                    amt += params[0];
                    if (amt == 0) return true;

                    AbstractRelic r = AbstractDungeon.player.getRelic(FlaskAqua.ID);
                    if (!(r instanceof FlaskAqua)) return true; //Can't infuse aqua

                    CascadeTracker tracker = new CascadeTracker();

                    for (int i = 0; i < amt; ++i) {
                        addToTop(new InfuseAction(Chemister.Flasks.AQUA));
                        addToTop(new AbstractGameAction() {
                            @Override
                            public void update() {
                                isDone = true;

                                ((FlaskAqua) r).addDrawFollowup(
                                        (cards)->{
                                            for (AbstractCard c : cards) {
                                                tracker.totalDrawnBase += ChemisterMod.getCardBase(c);

                                                if (!tracker.triggered && tracker.totalDrawnBase >= 4) {
                                                    tracker.triggered = true;
                                                    addToTop(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AttackEffect.SMASH));
                                                }
                                            }
                                        });
                            }
                        });
                    }

                    return true;
                }, magicNumber));
    }

    private static class CascadeTracker {
        int totalDrawnBase = 0;
        boolean triggered = false;
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
