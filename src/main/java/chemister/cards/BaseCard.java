package chemister.cards;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomCard;
import basemod.abstracts.DynamicVariable;
import chemister.ChemisterMod;
import chemister.actions.infuse.DisplayableAction;
import chemister.actions.infuse.InfuseAction;
import chemister.character.Chemister;
import chemister.powers.FlaskFracturePower;
import chemister.relics.starter.FlaskRelic;
import chemister.util.CardStats;
import chemister.util.TriFunction;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static chemister.util.GeneralUtils.removePrefix;
import static chemister.util.TextureLoader.getCardTextureString;


public abstract class BaseCard extends CustomCard {
    final private static Map<String, DynamicVariable> customVars = new HashMap<>();

    protected static String makeID(String name) { return ChemisterMod.makeID(name); }
    protected CardStrings cardStrings;

    protected boolean upgradesDescription;

    protected int baseCost;

    protected boolean upgradeCost;
    protected int costUpgrade;

    protected boolean upgradeDamage;
    protected boolean upgradeBlock;
    protected boolean upgradeMagic;

    protected int damageUpgrade;
    protected int blockUpgrade;
    protected int magicUpgrade;

    protected boolean baseExhaust = false;
    protected boolean upgExhaust = false;
    protected boolean baseEthereal = false;
    protected boolean upgEthereal = false;
    protected boolean baseInnate = false;
    protected boolean upgInnate = false;
    protected boolean baseRetain = false;
    protected boolean upgRetain = false;

    final protected Map<String, LocalVarInfo> cardVariables = new HashMap<>();

    public BaseCard(String ID, CardStats info) {
        this(ID, info.baseCost, info.cardType, info.cardTarget, info.cardRarity, info.cardColor);
    }
    public BaseCard(String ID, CardStats info, boolean upgradesDescription) {
        this(ID, info.baseCost, info.cardType, info.cardTarget, info.cardRarity, info.cardColor, upgradesDescription);
    }
    public BaseCard(String ID, int cost, CardType cardType, CardTarget target, CardRarity rarity, CardColor color)
    {
        super(ID, getName(ID), getCardTextureString(removePrefix(ID), cardType), cost, getInitialDescription(ID), cardType, color, rarity, target);
        this.cardStrings = CardCrawlGame.languagePack.getCardStrings(cardID);
        this.originalName = cardStrings.NAME;

        this.baseCost = cost;

        this.upgradesDescription = cardStrings.UPGRADE_DESCRIPTION != null;
        this.upgradeCost = false;
        this.upgradeDamage = false;
        this.upgradeBlock = false;
        this.upgradeMagic = false;

        this.costUpgrade = cost;
        this.damageUpgrade = 0;
        this.blockUpgrade = 0;
        this.magicUpgrade = 0;
    }
    public BaseCard(String ID, int cost, CardType cardType, CardTarget target, CardRarity rarity, CardColor color, boolean upgradesDescription)
    {
        this(ID, cost, cardType, target, rarity, color);
        this.upgradesDescription = upgradesDescription;
    }

    private static String getName(String ID) {
        return CardCrawlGame.languagePack.getCardStrings(ID).NAME;
    }
    private static String getInitialDescription(String ID) {
        return CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION;
    }

    //Methods meant for constructor use
    protected final void setDamage(int damage)
    {
        this.setDamage(damage, 0);
    }
    protected final void setDamage(int damage, int damageUpgrade)
    {
        this.baseDamage = this.damage = damage;
        if (damageUpgrade != 0)
        {
            this.upgradeDamage = true;
            this.damageUpgrade = damageUpgrade;
        }
    }

    protected final void setBlock(int block)
    {
        this.setBlock(block, 0);
    }
    protected final void setBlock(int block, int blockUpgrade)
    {
        this.baseBlock = this.block = block;
        if (blockUpgrade != 0)
        {
            this.upgradeBlock = true;
            this.blockUpgrade = blockUpgrade;
        }
    }

    protected final void setMagic(int magic)
    {
        this.setMagic(magic, 0);
    }
    protected final void setMagic(int magic, int magicUpgrade)
    {
        this.baseMagicNumber = this.magicNumber = magic;
        if (magicUpgrade != 0)
        {
            this.upgradeMagic = true;
            this.magicUpgrade = magicUpgrade;
        }
    }



    protected final void setCustomVar(String key, int base) {
        this.setCustomVar(key, base, 0);
    }
    protected final void setCustomVar(String key, int base, int upgrade) {
        setCustomVarValue(key, base, upgrade);

        if (!customVars.containsKey(key)) {
            QuickDynamicVariable var = new QuickDynamicVariable(key);
            customVars.put(key, var);
            BaseMod.addDynamicVariable(var);
            initializeDescription();
        }
    }

    protected enum VariableType {
        DAMAGE,
        BLOCK,
        MAGIC
    }
    protected final void setCustomVar(String key, VariableType type, int base) {
        setCustomVar(key, type, base, 0);
    }
    protected final void setCustomVar(String key, VariableType type, int base, int upgrade) {
        setCustomVarValue(key, base, upgrade);

        switch (type) {
            case DAMAGE:
                calculateVarAsDamage(key);
                break;
            case BLOCK:
                calculateVarAsBlock(key);
                break;
        }

        if (!customVars.containsKey(key)) {
            QuickDynamicVariable var = new QuickDynamicVariable(key);
            customVars.put(key, var);
            BaseMod.addDynamicVariable(var);
            initializeDescription();
        }
    }
    protected final void setCustomVar(String key, VariableType type, int base, TriFunction<BaseCard, AbstractMonster, Integer, Integer> preCalc) {
        setCustomVar(key, type, base, 0, preCalc);
    }
    protected final void setCustomVar(String key, VariableType type, int base, int upgrade, TriFunction<BaseCard, AbstractMonster, Integer, Integer> preCalc) {
        setCustomVar(key, type, base, upgrade, preCalc, LocalVarInfo::noCalc);
    }
    protected final void setCustomVar(String key, VariableType type, int base, TriFunction<BaseCard, AbstractMonster, Integer, Integer> preCalc, TriFunction<BaseCard, AbstractMonster, Integer, Integer> postCalc) {
        setCustomVar(key, type, base, 0, preCalc, postCalc);
    }
    protected final void setCustomVar(String key, VariableType type, int base, int upgrade, TriFunction<BaseCard, AbstractMonster, Integer, Integer> preCalc, TriFunction<BaseCard, AbstractMonster, Integer, Integer> postCalc) {
        setCustomVarValue(key, base, upgrade);

        switch (type) {
            case DAMAGE:
                setVarCalculation(key, (c, m, baseVal)->{
                    boolean wasMultiDamage = c.isMultiDamage;
                    c.isMultiDamage = false;

                    int origBase = c.baseDamage, origVal = c.damage;

                    c.baseDamage = preCalc.apply(c, m, baseVal);

                    if (m != null)
                        c.calculateCardDamage(m);
                    else
                        c.applyPowers();

                    c.damage = postCalc.apply(c, m, c.damage);

                    c.baseDamage = origBase;
                    c.isMultiDamage = wasMultiDamage;

                    int result = c.damage;
                    c.damage = origVal;

                    return result;
                });
                break;
            case BLOCK:
                setVarCalculation(key, (c, m, baseVal)->{
                    int origBase = c.baseBlock, origVal = c.block;

                    c.baseBlock = preCalc.apply(c, m, baseVal);

                    if (m != null)
                        c.calculateCardDamage(m);
                    else
                        c.applyPowers();

                    c.block = postCalc.apply(c, m, c.block);

                    c.baseBlock = origBase;
                    int result = c.block;
                    c.block = origVal;
                    return result;
                });
                break;
            default:
                setVarCalculation(key, (c, m, baseVal)->{
                    int tmp = baseVal;

                    tmp = preCalc.apply(c, m, tmp);
                    tmp = postCalc.apply(c, m, tmp);

                    return tmp;
                });
                break;
        }

        if (!customVars.containsKey(key)) {
            QuickDynamicVariable var = new QuickDynamicVariable(key);
            customVars.put(key, var);
            BaseMod.addDynamicVariable(var);
            initializeDescription();
        }
    }

    public void setCustomVarValue(String key, int base, int upg) {
        cardVariables.compute(key, (k, old)->{
            if (old == null) {
                return new LocalVarInfo(base, upg);
            }
            else {
                old.base = base;
                old.upgrade = upg;
                return old;
            }
        });
    }

    protected final void colorCustomVar(String key, Color normalColor) {
        colorCustomVar(key, normalColor, Settings.GREEN_TEXT_COLOR, Settings.RED_TEXT_COLOR, Settings.GREEN_TEXT_COLOR);
    }
    protected final void colorCustomVar(String key, Color normalColor, Color increasedColor, Color decreasedColor) {
        colorCustomVar(key, normalColor, increasedColor, decreasedColor, increasedColor);
    }
    protected final void colorCustomVar(String key, Color normalColor, Color increasedColor, Color decreasedColor, Color upgradedColor) {
        LocalVarInfo var = getCustomVar(key);
        if (var == null) {
            throw new IllegalArgumentException("Attempted to set color of variable that hasn't been registered.");
        }

        var.normalColor = normalColor;
        var.increasedColor = increasedColor;
        var.decreasedColor = decreasedColor;
        var.upgradedColor = upgradedColor;
    }


    private LocalVarInfo getCustomVar(String key) {
        return cardVariables.get(key);
    }

    protected void calculateVarAsDamage(String key) {
        setVarCalculation(key, (c, m, base)->{
            boolean wasMultiDamage = c.isMultiDamage;
            c.isMultiDamage = false;

            int origBase = c.baseDamage, origVal = c.damage;

            c.baseDamage = base;
            if (m != null)
                c.calculateCardDamage(m);
            else
                c.applyPowers();

            c.baseDamage = origBase;
            c.isMultiDamage = wasMultiDamage;

            int result = c.damage;
            c.damage = origVal;

            return result;
        });
    }
    protected void calculateVarAsBlock(String key) {
        setVarCalculation(key, (c, m, base)->{
            int origBase = c.baseBlock, origVal = c.block;

            c.baseBlock = base;
            if (m != null)
                c.calculateCardDamage(m);
            else
                c.applyPowers();

            c.baseBlock = origBase;
            int result = c.block;
            c.block = origVal;
            return result;
        });
    }
    protected void setVarCalculation(String key, TriFunction<BaseCard, AbstractMonster, Integer, Integer> calculation) {
        cardVariables.get(key).calculation = calculation;
    }

    public int customVarBase(String key) {
        LocalVarInfo var = cardVariables.get(key);
        if (var == null)
            return -1;
        return var.base;
    }
    public int customVar(String key) {
        LocalVarInfo var = cardVariables == null ? null : cardVariables.get(key); //Prevents crashing when used with dynamic text
        if (var == null)
            return -1;
        return var.value;
    }
    public int[] customVarMulti(String key) {
        LocalVarInfo var = cardVariables.get(key);
        if (var == null)
            return null;
        return var.aoeValue;
    }
    public boolean isCustomVarModified(String key) {
        LocalVarInfo var = cardVariables.get(key);
        if (var == null)
            return false;
        return var.isModified();
    }
    public boolean customVarUpgraded(String key) {
        LocalVarInfo var = cardVariables.get(key);
        if (var == null)
            return false;
        return var.upgraded;
    }


    protected final void setCostUpgrade(int costUpgrade)
    {
        this.costUpgrade = costUpgrade;
        this.upgradeCost = true;
    }
    protected final void setExhaust(boolean exhaust) { this.setExhaust(exhaust, exhaust); }
    protected final void setEthereal(boolean ethereal) { this.setEthereal(ethereal, ethereal); }
    protected final void setInnate(boolean innate) {this.setInnate(innate, innate); }
    protected final void setSelfRetain(boolean retain) {this.setSelfRetain(retain, retain); }
    protected final void setExhaust(boolean baseExhaust, boolean upgExhaust)
    {
        this.baseExhaust = baseExhaust;
        this.upgExhaust = upgExhaust;
        this.exhaust = baseExhaust;
    }
    protected final void setEthereal(boolean baseEthereal, boolean upgEthereal)
    {
        this.baseEthereal = baseEthereal;
        this.upgEthereal = upgEthereal;
        this.isEthereal = baseEthereal;
    }
    protected void setInnate(boolean baseInnate, boolean upgInnate)
    {
        this.baseInnate = baseInnate;
        this.upgInnate = upgInnate;
        this.isInnate = baseInnate;
    }
    protected void setSelfRetain(boolean baseRetain, boolean upgRetain)
    {
        this.baseRetain = baseRetain;
        this.upgRetain = upgRetain;
        this.selfRetain = baseRetain;
    }


    //Extra effect rendering for infusion cards
    @Override
    public void render(SpriteBatch sb) {
        if (this instanceof InfuseCard && (boolean)ReflectionHacks.getPrivate(this, AbstractCard.class, "hovered")
            && AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom() != null
            && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT)
        {
            if (specialRender(sb)) {
                return;
            }
        }
        super.render(sb);
    }
    public boolean specialRender(SpriteBatch sb) {
        return false;
    }


    //Infuse card methods
    private static final Color renderColor = Color.WHITE.cpy();
    private static final List<Chemister.Flasks> tempInfusedTypesForTurn = new ArrayList<>();
    private static final List<List<Chemister.Flasks>> tempInfusedTurnHistory = new ArrayList<>();
    private static final List<List<DisplayableAction>> flaskActions = new ArrayList<>();
    private static final List<Chemister.Flasks> additionalInfusions = new ArrayList<>();
    private static final List<FlaskRelic> flasks = new ArrayList<>();
    private static final Vector2 offset = new Vector2(0, 0);
    private static final float NUM_X_OFFSET = 16f, NUM_Y_OFFSET = -16f;
    public static <T extends AbstractCard & InfuseCard> boolean renderInfuseEffects(T card, boolean cumulative, SpriteBatch sb) {
        Chemister.Flasks[] infused = card.getFlasks();
        if (infused.length == 0) return false;

        additionalInfusions.clear();
        int tempInfusedCountThisTurn = ChemisterMod.infusedCountThisTurn;
        tempInfusedTurnHistory.clear();
        tempInfusedTurnHistory.addAll(ChemisterMod.infusedTurnHistory);
        tempInfusedTypesForTurn.clear();
        tempInfusedTypesForTurn.addAll(ChemisterMod.infusedTypesThisTurn);

        if (!tempInfusedTurnHistory.isEmpty() && ChemisterMod.infusedTypesThisTurn.equals(tempInfusedTurnHistory.get(tempInfusedTurnHistory.size() - 1))) {
            //infuse history contains current turn, which can be modified in this process. Replace with the temp list for turn.
            tempInfusedTurnHistory.set(tempInfusedTurnHistory.size() - 1, tempInfusedTypesForTurn);
        }
        else {
            //Infuse history contains nothing for this turn. This turn list is probably empty; add it anyways.
            tempInfusedTurnHistory.add(tempInfusedTypesForTurn);
        }

        float rowY = 208f + (Settings.SHOW_CARD_HOTKEYS ? 72f : 0f);
        float scale = Settings.scale * card.drawScale;

        renderColor.a = card.transparency;
        sb.setColor(renderColor);

        flaskActions.clear();
        flasks.clear();

        AbstractPower fracture = AbstractDungeon.player.getPower(FlaskFracturePower.POWER_ID);
        int skip = fracture == null ? 0 : fracture.amount;

        List<DisplayableAction> singleInfuseActions = new ArrayList<>();
        Chemister.Flasks toInfuse;
        boolean canChain;

        for (int i = 0; i < infused.length + additionalInfusions.size(); ++i) {
            if (i < infused.length) {
                toInfuse = infused[i];
                canChain = true;
            }
            else {
                toInfuse = additionalInfusions.get(i - infused.length);
                canChain = false;
            }

            if (skip > 0) {
                if (cumulative) --skip;
                continue;
            }

            for (AbstractRelic r : AbstractDungeon.player.relics) {
                if (r instanceof FlaskRelic && toInfuse.equals(((FlaskRelic) r).flaskType())) {
                    List<DisplayableAction> actions = null;

                    for (int j = 0; j < flasks.size(); ++j) {
                        if (r.equals(flasks.get(j))) {
                            //Flask is being infused a second time
                            actions = flaskActions.get(j);
                        }
                    }

                    if (actions == null) {
                        actions = new ArrayList<>();
                    }

                    singleInfuseActions.clear();
                    ((FlaskRelic) r).getInfuseActions(singleInfuseActions, tempInfusedCountThisTurn, tempInfusedTypesForTurn, tempInfusedTurnHistory, canChain);

                    outer:
                    for (DisplayableAction action : singleInfuseActions) {
                        if (action instanceof InfuseAction) {
                            additionalInfusions.add(((InfuseAction) action).flask);
                        }

                        for (DisplayableAction existing : actions) {
                            if (existing.merge(action)) {
                                continue outer;
                            }
                        }
                        //no merge
                        actions.add(action);
                    }

                    if (!flasks.contains(r) && !actions.isEmpty()) {
                        flasks.add((FlaskRelic) r);
                        flaskActions.add(actions);
                    }
                    break; //This was the relic for this flask type
                }
            }

            if (cumulative) {
                ++tempInfusedCountThisTurn;
                tempInfusedTypesForTurn.add(toInfuse);
            }
        }


        rowY += 54f * flaskActions.size();

        for (int i = 0; i < flaskActions.size(); ++i) {
            FlaskRelic flask = flasks.get(i);
            List<DisplayableAction> actions = flaskActions.get(i);

            //Render relic, followed by action icons
            offset.set(0, rowY);
            offset.x += -24f * actions.size();
            offset.scl(scale);

            drawOnCardCentered(sb, card, flask.imgRegion, card.current_x + offset.x, card.current_y + offset.y, 0.75f);

            for (DisplayableAction action : actions) {
                offset.x += 48f * Settings.scale;
                TextureRegion t = action.getIcon();
                if (t == null) {
                    ChemisterMod.logger.warn("NULL ICON FOR " + action.getClass().getName());
                    continue;
                }
                float x = card.current_x + offset.x;
                float y = card.current_y + offset.y;
                drawOnCardCentered(sb, card, t, x, y, action.getIconScale());
                if (action.getAction().amount != 0) {
                    FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, String.valueOf(action.getAction().amount), x + NUM_X_OFFSET, y + NUM_Y_OFFSET, renderColor, scale);
                }
            }

            rowY -= 54;
        }

        return false;
    }

    private static void processFlaskPreview(Chemister.Flasks toInfuse, boolean cumulative, List<DisplayableAction> singleInfuseActions, int skip) {
    }

    private static void drawOnCardCentered(SpriteBatch sb, AbstractCard card, TextureRegion img, float drawX, float drawY, float scaleModifier) {
        float scale = card.drawScale * Settings.scale * scaleModifier;
        sb.draw(img, drawX - img.getRegionWidth() / 2.0F, drawY - img.getRegionHeight() / 2.0F, img.getRegionWidth() / 2.0F, img.getRegionHeight() / 2.0F, img.getRegionWidth(), img.getRegionHeight(), scale, scale, card.angle);
    }



    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();

        if (card instanceof BaseCard)
        {
            card.rawDescription = this.rawDescription;
            ((BaseCard) card).upgradesDescription = this.upgradesDescription;

            ((BaseCard) card).baseCost = this.baseCost;

            ((BaseCard) card).upgradeCost = this.upgradeCost;
            ((BaseCard) card).upgradeDamage = this.upgradeDamage;
            ((BaseCard) card).upgradeBlock = this.upgradeBlock;
            ((BaseCard) card).upgradeMagic = this.upgradeMagic;

            ((BaseCard) card).costUpgrade = this.costUpgrade;
            ((BaseCard) card).damageUpgrade = this.damageUpgrade;
            ((BaseCard) card).blockUpgrade = this.blockUpgrade;
            ((BaseCard) card).magicUpgrade = this.magicUpgrade;

            ((BaseCard) card).baseExhaust = this.baseExhaust;
            ((BaseCard) card).upgExhaust = this.upgExhaust;
            ((BaseCard) card).baseEthereal = this.baseEthereal;
            ((BaseCard) card).upgEthereal = this.upgEthereal;
            ((BaseCard) card).baseInnate = this.baseInnate;
            ((BaseCard) card).upgInnate = this.upgInnate;
            ((BaseCard) card).baseRetain = this.baseRetain;
            ((BaseCard) card).upgRetain = this.upgRetain;

            for (Map.Entry<String, LocalVarInfo> varEntry : cardVariables.entrySet()) {
                LocalVarInfo target = ((BaseCard) card).getCustomVar(varEntry.getKey()),
                        current = varEntry.getValue();
                if (target == null) {
                    ((BaseCard) card).setCustomVar(varEntry.getKey(), current.base, current.upgrade);
                    target = ((BaseCard) card).getCustomVar(varEntry.getKey());
                }
                target.base = current.base;
                target.value = current.value;
                target.aoeValue = current.aoeValue;
                target.upgrade = current.upgrade;
                target.calculation = current.calculation;
            }
        }

        return card;
    }

    @Override
    public void upgrade()
    {
        if (!upgraded)
        {
            this.upgradeName();

            if (this.upgradesDescription)
            {
                if (cardStrings.UPGRADE_DESCRIPTION == null)
                {
                    ChemisterMod.logger.error("Card " + cardID + " upgrades description and has null upgrade description.");
                }
                else
                {
                    this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
                }
            }

            if (upgradeCost)
            {
                if (isCostModified && this.cost < this.baseCost && this.cost >= 0) {
                    int diff = this.costUpgrade - this.baseCost; //how the upgrade alters cost
                    this.upgradeBaseCost(this.cost + diff);
                    if (this.cost < 0)
                        this.cost = 0;
                }
                else {
                    upgradeBaseCost(costUpgrade);
                }
            }

            if (upgradeDamage)
                this.upgradeDamage(damageUpgrade);

            if (upgradeBlock)
                this.upgradeBlock(blockUpgrade);

            if (upgradeMagic)
                this.upgradeMagicNumber(magicUpgrade);

            for (LocalVarInfo var : cardVariables.values()) {
                if (var.upgrade != 0) {
                    var.base += var.upgrade;
                    var.value = var.base;
                    var.upgraded = true;
                }
            }

            if (baseExhaust ^ upgExhaust)
                this.exhaust = upgExhaust;

            if (baseInnate ^ upgInnate)
                this.isInnate = upgInnate;

            if (baseEthereal ^ upgEthereal)
                this.isEthereal = upgEthereal;

            if (baseRetain ^ upgRetain)
                this.selfRetain = upgRetain;


            this.initializeDescription();
        }
    }

    boolean inCalc = false;
    @Override
    public void applyPowers() {
        if (!inCalc) {
            inCalc = true;
            for (LocalVarInfo var : cardVariables.values()) {
                var.value = var.calculation.apply(this, null, var.base);
            }
            if (isMultiDamage) {
                ArrayList<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters;
                AbstractMonster m;
                for (LocalVarInfo var : cardVariables.values()) {
                    if (var.aoeValue == null || var.aoeValue.length != monsters.size())
                        var.aoeValue = new int[monsters.size()];

                    for (int i = 0; i < monsters.size(); ++i) {
                        m = monsters.get(i);
                        var.aoeValue[i] = var.calculation.apply(this, m, var.base);
                    }
                }
            }
            inCalc = false;
        }

        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster m) {
        if (!inCalc) {
            inCalc = true;
            for (LocalVarInfo var : cardVariables.values()) {
                var.value = var.calculation.apply(this, m, var.base);
            }
            if (isMultiDamage) {
                ArrayList<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters;
                for (LocalVarInfo var : cardVariables.values()) {
                    if (var.aoeValue == null || var.aoeValue.length != monsters.size())
                        var.aoeValue = new int[monsters.size()];

                    for (int i = 0; i < monsters.size(); ++i) {
                        m = monsters.get(i);
                        var.aoeValue[i] = var.calculation.apply(this, m, var.base);
                    }
                }
            }
            inCalc = false;
        }

        super.calculateCardDamage(m);
    }

    @Override
    public void resetAttributes() {
        super.resetAttributes();

        for (LocalVarInfo var : cardVariables.values()) {
            var.value = var.base;
        }
    }


    //Shortcut methods
    protected void gainEnergy(int amount) {
        addToBot(new GainEnergyAction(amount));
    }
    protected void drawCards(int amount) {
        addToBot(new DrawCardAction(amount));
    }
    protected void drawCards(int amount, AbstractGameAction followup) {
        addToBot(new DrawCardAction(amount, followup));
    }
    protected void block() {
        block(this.block);
    }
    protected void block(int amt) {
        addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, amt));
    }
    protected AbstractGameAction getBlockAction(int amount) {
        return new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, amount);
    }
    protected void giveBlock(AbstractCreature target, int amount) {
        addToBot(new GainBlockAction(target, AbstractDungeon.player, amount));
    }
    protected void damageSingle(AbstractCreature target, AbstractGameAction.AttackEffect effect)
    {
        addToBot(new DamageAction(target, new DamageInfo(AbstractDungeon.player, this.damage, this.damageTypeForTurn), effect));
    }
    protected void damageSingle(AbstractCreature target, AbstractGameAction.AttackEffect effect, boolean isFast)
    {
        addToBot(new DamageAction(target, new DamageInfo(AbstractDungeon.player, this.damage, this.damageTypeForTurn), effect, isFast));
    }
    protected void damageSingle(AbstractCreature target, int amount, AbstractGameAction.AttackEffect effect)
    {
        addToBot(new DamageAction(target, new DamageInfo(AbstractDungeon.player, amount, this.damageTypeForTurn), effect));
    }
    protected void damageSingle(AbstractCreature target, int amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect)
    {
        addToBot(new DamageAction(target, new DamageInfo(AbstractDungeon.player, amount, type), effect));
    }
    protected AbstractGameAction getDamageSingle(AbstractCreature target, int amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect)
    {
        return new DamageAction(target, new DamageInfo(AbstractDungeon.player, amount, type), effect);
    }
    protected void damageRandom(AbstractGameAction.AttackEffect effect)
    {
        addToBot(new AttackDamageRandomEnemyAction(this, effect));
    }
    protected void damageAll(AbstractGameAction.AttackEffect effect)
    {
        addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, this.multiDamage, this.damageTypeForTurn, effect));
    }
    protected void damageAll(int amount, AbstractGameAction.AttackEffect effect)
    {
        addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, amount, this.damageTypeForTurn, effect));
    }
    protected void damageAll(int amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect)
    {
        if (type != DamageInfo.DamageType.NORMAL)
        {
            addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, DamageInfo.createDamageMatrix(amount, true), type, effect));
        }
        else
        {
            addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, amount, type, effect));
        }
    }
    protected AbstractGameAction getDamageAll(int amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect)
    {
        return getDamageAll(amount, type, effect, false);
    }
    protected AbstractGameAction getDamageAll(int amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect, boolean isFast)
    {
        if (type != DamageInfo.DamageType.NORMAL)
        {
            return new DamageAllEnemiesAction(AbstractDungeon.player, DamageInfo.createDamageMatrix(amount, true), type, effect, isFast);
        }
        else
        {
            return new DamageAllEnemiesAction(AbstractDungeon.player, amount, type, effect);
        }
    }

    protected void vfx(AbstractGameEffect effect) {
        addToBot(new VFXAction(effect));
    }
    protected void vfx(AbstractGameEffect effect, float duration) {
        addToBot(new VFXAction(effect, duration));
    }

    protected void infuse() {
        addToBot(new InfuseAction());
    }
    protected void infuse(Chemister.Flasks flask) {
        addToBot(new InfuseAction(flask));
    }
    protected void infuseTop(Chemister.Flasks flask) {
        addToTop(new InfuseAction(flask));
    }

    protected ApplyPowerAction applySingle(AbstractCreature c, AbstractPower power)
    {
        return applySingle(c, power, false);
    }
    protected ApplyPowerAction applySingle(AbstractCreature c, AbstractPower power, boolean isFast)
    {
        ApplyPowerAction action = new ApplyPowerAction(c, AbstractDungeon.player, power, power.amount, isFast);
        addToBot(action);
        return action;
    }

    protected ApplyPowerAction applySelf(AbstractPower power)
    {
        ApplyPowerAction action = new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, power, power.amount);
        addToBot(action);
        return action;
    }

    protected void applyAll(Function<AbstractMonster, AbstractPower> power, AbstractGameAction.AttackEffect effect)
    {
        applyAll(power, false, effect);
    }
    protected void applyAll(Function<AbstractMonster, AbstractPower> power, boolean isFast, AbstractGameAction.AttackEffect effect)
    {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
        {
            AbstractPower toApply = power.apply(m);
            addToBot(new ApplyPowerAction(m, AbstractDungeon.player, toApply, toApply.amount, isFast, effect));
        }
    }

    protected WeakPower getWeak(AbstractCreature c, int amount)
    {
        return new WeakPower(c, amount, false);
    }
    protected VulnerablePower getVuln(AbstractCreature c, int amount)
    {
        return new VulnerablePower(c, amount, false);
    }


    private static class QuickDynamicVariable extends DynamicVariable {
        final String localKey, key;

        private BaseCard current = null;

        public QuickDynamicVariable(String key) {
            this.localKey = key;
            this.key = makeID(key);
        }

        @Override
        public String key() {
            return key;
        }

        @Override
        public void setIsModified(AbstractCard c, boolean v) {
            if (c instanceof BaseCard) {
                LocalVarInfo var = ((BaseCard) c).getCustomVar(localKey);
                if (var != null)
                    var.forceModified = v;
            }
        }

        @Override
        public boolean isModified(AbstractCard c) {
            return c instanceof BaseCard && (current = (BaseCard) c).isCustomVarModified(localKey);
        }

        @Override
        public int value(AbstractCard c) {
            return c instanceof BaseCard ? ((BaseCard) c).customVar(localKey) : 0;
        }

        @Override
        public int baseValue(AbstractCard c) {
            return c instanceof BaseCard ? ((BaseCard) c).customVarBase(localKey) : 0;
        }

        @Override
        public boolean upgraded(AbstractCard c) {
            return c instanceof BaseCard && ((BaseCard) c).customVarUpgraded(localKey);
        }

        public Color getNormalColor() {
            LocalVarInfo var;
            if (current == null || (var = current.getCustomVar(localKey)) == null)
                return Settings.CREAM_COLOR;

            return var.normalColor;
        }

        public Color getUpgradedColor() {
            LocalVarInfo var;
            if (current == null || (var = current.getCustomVar(localKey)) == null)
                return Settings.GREEN_TEXT_COLOR;

            return var.upgradedColor;
        }

        public Color getIncreasedValueColor() {
            LocalVarInfo var;
            if (current == null || (var = current.getCustomVar(localKey)) == null)
                return Settings.GREEN_TEXT_COLOR;

            return var.increasedColor;
        }

        public Color getDecreasedValueColor() {
            LocalVarInfo var;
            if (current == null || (var = current.getCustomVar(localKey)) == null)
                return Settings.RED_TEXT_COLOR;

            return var.decreasedColor;
        }
    }

    protected static class LocalVarInfo {
        int base, value, upgrade;
        int[] aoeValue = null;
        boolean upgraded = false;
        boolean forceModified = false;
        Color normalColor = Settings.CREAM_COLOR;
        Color upgradedColor = Settings.GREEN_TEXT_COLOR;
        Color increasedColor = Settings.GREEN_TEXT_COLOR;
        Color decreasedColor = Settings.RED_TEXT_COLOR;

        TriFunction<BaseCard, AbstractMonster, Integer, Integer> calculation = LocalVarInfo::noCalc;

        public LocalVarInfo(int base, int upgrade) {
            this.base = this.value = base;
            this.upgrade = upgrade;
        }

        private static int noCalc(BaseCard c, AbstractMonster m, int base) {
            return base;
        }

        public boolean isModified() {
            return forceModified || base != value;
        }

        public void applyCalc(BaseCard card, AbstractMonster target) {
            value = calculation.apply(card, target, base);
        }
    }
}