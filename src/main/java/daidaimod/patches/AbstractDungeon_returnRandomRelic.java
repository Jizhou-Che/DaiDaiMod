package daidaimod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import daidaimod.DaiDaiMod;

import java.util.Objects;

@SpirePatch2(
        clz = AbstractDungeon.class,
        method = "returnRandomRelic"
)
public class AbstractDungeon_returnRandomRelic {
    public static SpireReturn<AbstractRelic> Prefix(AbstractRelic.RelicTier tier) {
        if (tier == AbstractRelic.RelicTier.BOSS && DaiDaiMod.getConfigBool("forceBlessingOfDai") && AbstractDungeon.bossRelicPool.contains("daidaimod:BlessingOfDai")) {
            AbstractDungeon.bossRelicPool.removeIf(e -> Objects.equals(e, "daidaimod:BlessingOfDai"));
            return SpireReturn.Return(RelicLibrary.getRelic("daidaimod:BlessingOfDai").makeCopy());
        }
        return SpireReturn.Continue();
    }
}
