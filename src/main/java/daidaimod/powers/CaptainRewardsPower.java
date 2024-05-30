package daidaimod.powers;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import daidaimod.cards.GiftOfDai;
import daidaimod.monsters.HeartOfDai;

public class CaptainRewardsPower extends AbstractPower {
    public static final String POWER_ID = "daidaimod:CaptainRewards";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public CaptainRewardsPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.updateDescription();
        this.loadRegion(POWER_ID);
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    public void atStartOfTurn() {
        if (AbstractDungeon.player.gold >= 138) {
            this.flash();

            AbstractDungeon.actionManager.addToBottom(new TalkAction(this.owner, HeartOfDai.DIALOG[1], 1.0F, 2.5F));

            int giftAmount = AbstractDungeon.player.gold / 138;
            CardCrawlGame.sound.play("GOLD_JINGLE");
            AbstractDungeon.player.loseGold(138 * giftAmount);
            for (int i = 0; i < 138 * giftAmount; i++) {
                AbstractDungeon.effectList.add(new GainPennyEffect(this.owner, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.owner.hb.cX, this.owner.hb.cY, false));
            }
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new GiftOfDai(), giftAmount, false));
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
