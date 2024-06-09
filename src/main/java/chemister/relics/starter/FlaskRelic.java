package chemister.relics.starter;

import chemister.ChemisterMod;
import chemister.actions.infuse.DisplayableAction;
import chemister.actions.infuse.InfuseAction;
import chemister.cards.InfuseCard;
import chemister.cards.ReagentCard;
import chemister.character.Chemister;
import chemister.infuse.InfuseEffect;
import chemister.relics.BaseRelic;
import chemister.util.GeneralUtils;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.*;

public abstract class FlaskRelic extends BaseRelic {
    private static final Map<Chemister.Flasks, Texture> flaskTextures = new HashMap<>();
    public static void register(FlaskRelic relic) {
        flaskTextures.put(relic.flaskType(), relic.img);
    }

    public static Texture getFlaskTexture(Chemister.Flasks flask) {
        return flaskTextures.getOrDefault(flask, ImageMaster.getRelicImg("Derp Rock"));
    }

    public TextureRegion imgRegion;

    public FlaskRelic(String id, String imageName, AbstractCard.CardColor pool, RelicTier tier, LandingSound sfx) {
        super(id, imageName, pool, tier, sfx);
        imgRegion = new TextureRegion(img, 0, 0, img.getWidth(), img.getHeight());
    }

    @Override
    public void setCounter(int counter) {
        super.setCounter(counter);
        refreshDescriptionAndTips();
    }

    public abstract Chemister.Flasks flaskType();
    public void infuse(boolean canChain) {
        List<DisplayableAction> actions = new ArrayList<>();
        getInfuseActions(actions, ChemisterMod.infusedCountThisTurn, ChemisterMod.infusedTypesThisTurn, ChemisterMod.infusedTurnHistory, canChain);
        for (int i = actions.size() - 1; i >= 0; --i) {
            addToTop(actions.get(i).getAction());
        }
    }
    public abstract void resetCounter();

    @Override
    public void update() {
        updateFlaskFlash(flaskType());
        super.update();
    }

    @Override
    protected void initializeTips() {
        updateFlask();
        super.initializeTips();
    }

    @Override
    public void onMasterDeckChange() {
        refreshDescriptionAndTips();
    }

    @Override
    public void atPreBattle() {
        refreshDescriptionAndTips();
    }

    @Override
    public void onVictory() {
        refreshDescriptionAndTips(); //Clear temp value changes from combat.
    }

    public void updateFlaskFlash(Chemister.Flasks flaskType) {
        if (this.flashTimer <= 0.0F && AbstractDungeon.player != null && AbstractDungeon.player.hoveredCard instanceof InfuseCard
                && GeneralUtils.arrContains(((InfuseCard) AbstractDungeon.player.hoveredCard).getFlasks(), flaskType)) {
            this.flashTimer = 1.0F;
        }
    }

    protected final List<PowerTip> reagentTips = new ArrayList<>();
    protected final List<ReagentCard.ReagentEffect> reagentEffects = new ArrayList<>();
    protected final List<InfuseEffect> infuseEffects = new ArrayList<>(); //Slightly different as reagentEffects is more for tooltip purposes.
    public void updateFlask() {
        //Called when master deck changes/on initialization.
        //Not intended for "active" changes.
        //Only permanent fixed value changes should modify counter in getEffect.
        if (reagentTips != null && CardCrawlGame.isInARun()) {
            resetCounter();
            tips.removeAll(reagentTips);
            reagentTips.clear();
            this.reagentEffects.clear();

            Map<String, ReagentCard.ReagentEffect> reagentEffects = new HashMap<>();

            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c instanceof ReagentCard) {
                    ReagentCard.ReagentEffect effect = ((ReagentCard) c).getEffect(this);
                    if (effect != null) {
                        reagentEffects.merge(effect.ID, effect, ReagentCard.ReagentEffect::merge);
                    }
                }
            }

            this.reagentEffects.addAll(reagentEffects.values());
            this.reagentEffects.sort(Comparator.comparingInt((effect)->effect.priority));
            for (ReagentCard.ReagentEffect effect : this.reagentEffects) {
                PowerTip tip = effect.getTip();
                if (tip != null) {
                    reagentTips.add(tip);
                    tips.add(tip);
                }
            }

            refreshDescription(); //Update main tooltip for possibly changed counter value
        }
    }

    //Called when powers are applied
    public void applyReagents() {
        infuseEffects.clear();
        resetCounter();
        InfuseEffect baseEffect = getBaseEffect();
        if (baseEffect != null) infuseEffects.add(getBaseEffect());

        outer:
        for (ReagentCard.ReagentEffect reagentEffect : reagentEffects) {
            for (InfuseEffect effect : infuseEffects) { //Merge if already existing
                if (effect.ID.equals(reagentEffect.ID)) {
                    effect.merge(reagentEffect);
                    continue outer;
                }
            } //Otherwise add
            infuseEffects.add(reagentEffect);
        }

        outer:
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof InfuseEffectRelic) {
                InfuseEffect relicEffect = ((InfuseEffectRelic) r).infuseEffect(this);

                if (relicEffect != null) {
                    for (InfuseEffect effect : infuseEffects) { //Merge if already existing
                        if (effect.ID.equals(relicEffect.ID)) {
                            effect.merge(relicEffect);
                            continue outer;
                        }
                    } //Otherwise add
                    infuseEffects.add(relicEffect);
                }
            }
        }

        infuseEffects.sort(Comparator.comparingInt((effect)->effect.priority));

        for (InfuseEffect effect : infuseEffects) {
            effect.applyValueChange(this);
        }

        for (AbstractPower p : AbstractDungeon.player.powers) {
            if (p instanceof FlaskValuePower) {
                ((FlaskValuePower) p).modifyFlaskValue(this);
            }
        }
    }

    public abstract InfuseEffect getBaseEffect();

    public void getInfuseActions(List<DisplayableAction> actions, int infusedCount, List<Chemister.Flasks> infusedThisTurn, List<List<Chemister.Flasks>> infusedThisCombat, boolean canChain) {
        outer:
        for (InfuseEffect effect : infuseEffects) {
            DisplayableAction action = effect.getAction(actions, infusedCount, infusedThisTurn, infusedThisCombat);
            if (action != null) {
                if (action instanceof InfuseAction && !canChain) {
                    continue outer;
                }
                for (DisplayableAction existingAction : actions) {
                    if (existingAction.merge(action)) {
                        continue outer;
                    }
                }
                actions.add(action);
            }
        }
    }

    public interface FlaskValuePower {
        void modifyFlaskValue(FlaskRelic r);
    }

    public interface InfuseEffectRelic {
        InfuseEffect infuseEffect(FlaskRelic flaskRelic);
    }

    /*
    Infuse cards:
    Must know what will happen when played.
    Must be able to "get" effects without actually advancing state.
    However, it should be efficient.

    On master deck change, "reagent effects" are updated.
    So, on applyPowers, those reagents should generate their infuse effects in a form that can be easily queried.

    Given some current "state" (infusion count, infusedThisTurn, infusedThisCombat), there are only a few possibilities
    for the result of next infusion. So, generate this.

    Combine effects of same type, with "conditions" for their numeric value to be applied.
    These tests occur when getInfuseActions is called, and if it results in 0, no value is applied.
     */
}
