package daidaimod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.EventHelper;

@SpirePatch2(
        clz = AbstractDungeon.class,
        method = "generateEvent"
)
public class AbstractDungeon_generateEvent {
    public static SpireReturn<AbstractEvent> Prefix() {
        if (AbstractDungeon.player.hasRelic("daidaimod:BlessingOfDai")) {
            if (AbstractDungeon.player.getRelic("daidaimod:BlessingOfDai").counter == -1) {
                // Falling event unseen.
                if (AbstractDungeon.id.equals("TheBeyond")) {
                    // This event should be Falling.
                    AbstractDungeon.player.getRelic("daidaimod:BlessingOfDai").counter = -2;
                    AbstractDungeon.player.getRelic("daidaimod:BlessingOfDai").flash();
                    AbstractDungeon.eventList.remove("Falling");
                    return SpireReturn.Return(EventHelper.getEvent("Falling"));
                }
            }
        }
        return SpireReturn.Continue();
    }
}
