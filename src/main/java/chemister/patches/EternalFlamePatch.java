package chemister.patches;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class EternalFlamePatch {
    @SpirePatch2(
            clz = ReducePowerAction.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = { AbstractCreature.class, AbstractCreature.class, String.class, int.class }
    )
    public static class ReduceID {
        @SpirePrefixPatch
        public static void adjustReduceAmount(String power, @ByRef int[] amount) {
            EternalFlamePatch.onReduce(power, amount);
        }
    }

    @SpirePatch2(
            clz = ReducePowerAction.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = { AbstractCreature.class, AbstractCreature.class, AbstractPower.class, int.class }
    )
    public static class ReduceInstance {
        @SpirePrefixPatch
        public static void adjustReduceAmount(AbstractPower powerInstance, @ByRef int[] amount) {
            EternalFlamePatch.onReduce(powerInstance.ID, amount);
        }
    }

    private static void onReduce(String power, int[] amount) {
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof OnReducePowerRelic) {
                amount[0] = ((OnReducePowerRelic) r).changeReduceAmount(power, amount[0]);
            }
        }
    }

    public interface OnReducePowerRelic {
        int changeReduceAmount(String powerID, int amt);
    }
}
