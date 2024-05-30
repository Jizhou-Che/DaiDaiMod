package daidaimod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import daidaimod.cards.FakeFall;
import daidaimod.cards.GenuineFall;
import daidaimod.events.FallingDai;

import java.util.ArrayList;
import java.util.Objects;

public class FallingMasterAction extends AbstractGameAction {
    public FallingMasterAction() {
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            ArrayList<AbstractCard> hand = new ArrayList<>();
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if (c.type != AbstractCard.CardType.CURSE && c.type != AbstractCard.CardType.STATUS) {
                    hand.add(c);
                }
            }

            GenuineFall genuineFall = new GenuineFall();
            if (hand.isEmpty()) {
                genuineFall.canFall = false;
                genuineFall.rawDescription = GenuineFall.EXTENDED_DESCRIPTION[0];
                genuineFall.initializeDescription();
            } else {
                int maxRank = -1;
                for (AbstractCard c : hand) {
                    int rank = FallingDai.getCardRank(c);
                    if (Objects.equals(c.cardID, "Feed") || Objects.equals(c.cardID, "LessonLearned") || Objects.equals(c.cardID, "HandOfGreed")) {
                        rank = 1;
                    }
                    if (rank > maxRank) {
                        maxRank = rank;
                        genuineFall.bestCard = c;
                        genuineFall.bestCardRank = rank;
                    }
                }
                if (maxRank >= 5) {
                    genuineFall.rawDescription = GenuineFall.UPGRADE_DESCRIPTION.replaceAll("[(][?][)]", genuineFall.bestCard.name);
                } else {
                    genuineFall.rawDescription = GenuineFall.DESCRIPTION.replaceAll("[(][?][)]", genuineFall.bestCard.name);
                }
                genuineFall.initializeDescription();
            }

            ArrayList<AbstractCard> choices = new ArrayList<>();
            choices.add(genuineFall);
            choices.add(new FakeFall());
            AbstractDungeon.cardRewardScreen.chooseOneOpen(choices);
        }
        this.tickDuration();
    }
}
