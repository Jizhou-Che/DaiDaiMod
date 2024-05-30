package daidaimod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Objects;

@SpirePatch2(
        clz = AbstractPlayer.class,
        method = "applyStartOfCombatLogic"
)
public class AbstractPlayer_applyStartOfCombatLogic {
    public static void Postfix() {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            for (AbstractPower p : m.powers) {
                if (Objects.equals(p.ID, "daidaimod:FallingMaster")) {
                    // To prevent conflicts against other mods.
                    p.atStartOfTurnPostDraw();
                }
            }
        }
    }
}
