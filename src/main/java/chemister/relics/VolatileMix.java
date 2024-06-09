package chemister.relics;

import chemister.actions.infuse.DisplayableAction;
import chemister.actions.infuse.InfuseAction;
import chemister.cards.ReagentCard;
import chemister.cards.special.InfuseChoiceCard;
import chemister.character.Chemister;
import chemister.infuse.InfuseEffect;
import chemister.relics.starter.FlaskRelic;
import chemister.screens.RelicSelectScreen;
import chemister.util.GeneralUtils;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.ArrayList;
import java.util.List;

import static chemister.ChemisterMod.logger;
import static chemister.ChemisterMod.makeID;

public class VolatileMix extends BaseRelic implements FlaskRelic.InfuseEffectRelic {
    private static final String NAME = VolatileMix.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private RelicSelectScreen relicSelectScreen;
    private int selectionRemaining = 0;
    private boolean fakeHover = false;

    private final Chemister.Flasks[] flasks = new Chemister.Flasks[2];

    public VolatileMix() {
        super(ID, NAME, Chemister.Meta.CARD_COLOR, RelicTier.BOSS, LandingSound.SOLID);
        counter = 0;

        flasks[0] = Chemister.Flasks.IGNIS;
        flasks[1] = Chemister.Flasks.TERRA;
    }

    @Override
    public void setCounter(int counter) {
        super.setCounter(counter);
        if (this.counter != 0) {
            int flaskIndex = 0;
            for (int i = 0; i < Chemister.Flasks.values().length; ++i) {
                if ((counter & (1 << i)) != 0) {
                    flasks[flaskIndex] = Chemister.Flasks.values()[i];
                    ++flaskIndex;
                    if (flaskIndex >= flasks.length) break;
                }
            }
            refreshDescriptionAndTips();
        }
    }

    @Override
    public String getUpdatedDescription() {
        if (flasks == null || counter <= 0) return DESCRIPTIONS[0];
        return String.format(DESCRIPTIONS[1], InfuseChoiceCard.getInfuseName(flasks[0]), InfuseChoiceCard.getInfuseName(flasks[1]));
    }

    @Override
    public void renderCounter(SpriteBatch sb, boolean inTopPanel) {
    }

    @Override
    public void onEquip() {
        ArrayList<AbstractRelic> flasks = new ArrayList<>();
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof FlaskRelic) {
                AbstractRelic selectionCopy = r.makeCopy();
                selectionCopy.counter = -1;
                flasks.add(selectionCopy);
            }
        }

        if (flasks.size() < 2) return;

        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }

        if (AbstractDungeon.currMapNode == null || AbstractDungeon.getCurrRoom() == null ||
                AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
        }

        openRelicSelect(flasks);
    }

    private void openRelicSelect(ArrayList<AbstractRelic> flasks)
    {
        selectionRemaining = 2;

        relicSelectScreen = new RelicSelectScreen();
        relicSelectScreen.open(flasks);
    }


    @Override
    public void update()
    {
        super.update();

        if (selectionRemaining > 0) {
            if (relicSelectScreen.doneSelecting()) {
                --selectionRemaining;

                AbstractRelic selected = relicSelectScreen.getSelectedRelics().get(0);
                selected.flash();

                if (selected instanceof FlaskRelic) {
                    Chemister.Flasks flask = ((FlaskRelic) selected).flaskType();

                    counter |= 1 << flask.ordinal();
                    relicSelectScreen.relics.removeIf((r)->r instanceof FlaskRelic && ((FlaskRelic) r).flaskType() == flask);

                    if (selectionRemaining < flasks.length) {
                        flasks[selectionRemaining] = flask;
                    }
                    else {
                        logger.warn("Making more selections for Volatile Mix than is supported.");
                    }
                }


                if (selectionRemaining > 0) {
                    relicSelectScreen.open(relicSelectScreen.relics);
                }
                else {
                    if (flasks[0].ordinal() > flasks[1].ordinal()) {
                        Chemister.Flasks temp = flasks[0];
                        flasks[0] = flasks[1];
                        flasks[1] = temp;
                    }

                    if (AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom() != null &&
                            AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
                        AbstractDungeon.onModifyPower();
                    }
                    else {
                        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                    }
                    refreshDescriptionAndTips();
                }
            } else {
                relicSelectScreen.update();
                if (!hb.hovered) {
                    fakeHover = true;
                }
                hb.hovered = true;
            }
        }
    }

    @Override
    public void renderTip(SpriteBatch sb)
    {
        if (selectionRemaining > 0 && fakeHover) {
            relicSelectScreen.render(sb);
        }
        if (fakeHover) {
            fakeHover = false;
            hb.hovered = false;
        } else {
            super.renderTip(sb);
        }
    }

    @Override
    public void renderInTopPanel(SpriteBatch sb)
    {
        super.renderInTopPanel(sb);

        if (selectionRemaining > 0 && !fakeHover) {
            relicSelectScreen.render(sb);
        }
    }


    @Override
    public InfuseEffect infuseEffect(FlaskRelic flaskRelic) {
        Chemister.Flasks flask = flaskRelic.flaskType();
        if (GeneralUtils.arrContains(flasks, flask)) {
            Chemister.Flasks other = flasks[0] == flask ? flasks[1] : flasks[0];

            return new ReagentCard.ReagentEffect(ID, 16, 1) {
                @Override
                protected int getAmount(int infusedCount, List<Chemister.Flasks> infusedThisTurn, List<List<Chemister.Flasks>> infusedThisCombat) {
                    return 1;
                }

                @Override
                public DisplayableAction createAction(int amt) {
                    return new InfuseAction(other, true);
                }

                @Override
                public PowerTip getTip() {
                    return new PowerTip(text[0], String.format(text[1], InfuseChoiceCard.getInfuseName(other)));
                }
            };
        }
        return null;
    }
}
