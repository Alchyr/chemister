package chemister.patches;

import chemister.ChemisterMod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

import java.util.ArrayList;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "applyStartOfTurnRelics"
)
public class ResetInfusePerTurn {
    @SpirePrefixPatch
    public static void reset(AbstractPlayer __instance)
    {
        ChemisterMod.infusedCountThisTurn = 0;
        ChemisterMod.infusedTypesThisTurn = new ArrayList<>();
        ChemisterMod.infusedTurnHistory.add(ChemisterMod.infusedTypesThisTurn);
    }
}
