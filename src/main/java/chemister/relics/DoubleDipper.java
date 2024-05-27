package chemister.relics;

import chemister.actions.infuse.DisplayableAction;
import chemister.actions.infuse.InfuseAction;
import chemister.cards.ReagentCard;
import chemister.character.Chemister;
import chemister.infuse.InfuseEffect;
import chemister.relics.starter.FlaskRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.List;

import static chemister.ChemisterMod.makeID;

public class DoubleDipper extends BaseRelic implements FlaskRelic.InfuseEffectRelic {
    private static final String NAME = DoubleDipper.class.getSimpleName();
    public static final String ID = makeID(NAME);

    public DoubleDipper() {
        super(ID, NAME, Chemister.Meta.CARD_COLOR, RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public void atTurnStart() {
        beginLongPulse();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onEquip() {
        if (AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom() != null &&
                AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractDungeon.onModifyPower();
        }
    }

    @Override
    public InfuseEffect infuseEffect(FlaskRelic flaskRelic) {
        Chemister.Flasks flask = flaskRelic.flaskType();
        return new ReagentCard.ReagentEffect(ID, 15, 1) {
            @Override
            protected int getAmount(int infusedCount, List<Chemister.Flasks> infusedThisTurn, List<List<Chemister.Flasks>> infusedThisCombat) {
                return maxAmount - infusedThisTurn.size();
            }

            @Override
            public DisplayableAction createAction(int amt) {
                return new InfuseAction(flask, true) {
                    @Override
                    public void update() {
                        super.update();
                        if (isDone) {
                            DoubleDipper.this.stopPulse();
                            DoubleDipper.this.flash();
                        }
                    }
                };
            }

            @Override
            public PowerTip getTip() {
                return new PowerTip(text[0], maxAmount == 1 ? text[1] : String.format(text[2], maxAmount));
            }
        };
    }
}
