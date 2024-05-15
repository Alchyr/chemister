package chemister.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@SpirePatch(
        clz = AbstractMonster.class,
        method = "damage"
)
public class OverkillDamagePatch {
    public static AbstractMonster overkilled = null;
    public static int overkillAmt = 0;

    @SpireInsertPatch(
            rloc = 78
    )
    public static void getOverkill(AbstractMonster m) {
        //insert inside if (currentHealth < 0) currentHealth = 0;
        overkillAmt = -m.currentHealth;
        overkilled = m;
    }
}
