package daidaimod.patches;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import daidaimod.DaiDaiMod;
import daidaimod.monsters.HeartOfDai;

import java.util.Objects;

@SpirePatch2(
        clz = AbstractDungeon.class,
        method = "setBoss"
)
public class AbstractDungeon_setBoss {
    public static SpireReturn<Void> Prefix(@ByRef String[] key) {
        if (Objects.equals(AbstractDungeon.id, TheEnding.ID)) {
            if (DaiDaiMod.getConfigBool("challengeHeartOfDai")) {
                key[0] = HeartOfDai.ID;
            } else if (Objects.equals(key[0], HeartOfDai.ID)) {
                key[0] = "The Heart";
            }
        }
        return SpireReturn.Continue();
    }
}
