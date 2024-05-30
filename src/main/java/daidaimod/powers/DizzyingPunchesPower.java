package daidaimod.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import daidaimod.monsters.HeartOfDai;

public class DizzyingPunchesPower extends AbstractPower {
    public static final String POWER_ID = "daidaimod:DizzyingPunches";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public DizzyingPunchesPower(AbstractCreature owner) {
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

    public void onInflictDamage(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageAmount > 0 && info.type != DamageInfo.DamageType.THORNS) {
            if (HeartOfDai.attackType == 0) {
                this.addToBot(new MakeTempCardInDrawPileAction(new Dazed(), 1, true, true));
            } else if (HeartOfDai.attackType == 1) {
                this.addToBot(new MakeTempCardInDrawPileAction(new Dazed(), 3, true, true));
            }
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
