package daidaimod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.powers.watcher.*;
import daidaimod.monsters.HeartOfDai;

import java.util.Objects;

public class RushdownEnthusiastPower extends AbstractPower {
    public static final String POWER_ID = "daidaimod:RushdownEnthusiast";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public RushdownEnthusiastPower(AbstractCreature owner) {
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

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (Objects.equals(card.cardID, "Adaptation")) {
            this.flash();

            AbstractDungeon.actionManager.addToBottom(new TalkAction(this.owner, HeartOfDai.DIALOG[0], 1.0F, 2.5F));

            RemoveSpecificPowerAction removeRushdownAction = new RemoveSpecificPowerAction(AbstractDungeon.player, this.owner, "Adaptation");
            removeRushdownAction.actionType = AbstractGameAction.ActionType.SPECIAL;
            AbstractDungeon.actionManager.addToBottom(removeRushdownAction);

            AbstractPower power = getRandomPower();
            while ((AbstractDungeon.player.hasPower(BarricadePower.POWER_ID) && Objects.equals(power.ID, BarricadePower.POWER_ID)) || (AbstractDungeon.player.hasPower(CorruptionPower.POWER_ID) && Objects.equals(power.ID, CorruptionPower.POWER_ID)) || (AbstractDungeon.player.hasPower(InvinciblePower.POWER_ID) && Objects.equals(power.ID, InvinciblePower.POWER_ID)) || (AbstractDungeon.player.hasPower(MasterRealityPower.POWER_ID) && Objects.equals(power.ID, MasterRealityPower.POWER_ID))) {
                power = getRandomPower();
            }
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this.owner, power, power.amount));
        }
    }

    private AbstractPower getRandomPower() {
        switch (AbstractDungeon.cardRandomRng.random(34)) {
            case 0:
                return new AfterImagePower(AbstractDungeon.player, 1);
            case 1:
                return new ArtifactPower(AbstractDungeon.player, 2);
            case 2:
                return new BarricadePower(AbstractDungeon.player);
            case 3:
                return new BerserkPower(AbstractDungeon.player, 1);
            case 4:
                return new BufferPower(AbstractDungeon.player, 1);
            case 5:
                return new CorruptionPower(AbstractDungeon.player);
            case 6:
                return new CreativeAIPower(AbstractDungeon.player, 1);
            case 7:
                return new CuriosityPower(AbstractDungeon.player, 2);
            case 8:
                return new DarkEmbracePower(AbstractDungeon.player, 1);
            case 9:
                return new DemonFormPower(AbstractDungeon.player, 1);
            case 10:
                return new DevaPower(AbstractDungeon.player);
            case 11:
                return new DevotionPower(AbstractDungeon.player, 4);
            case 12:
                return new DexterityPower(AbstractDungeon.player, 4);
            case 13:
                return new DrawPower(AbstractDungeon.player, 1);
            case 14:
                return new EchoPower(AbstractDungeon.player, 1);
            case 15:
                return new EnvenomPower(AbstractDungeon.player, 2);
            case 16:
                return new EstablishmentPower(AbstractDungeon.player, 1);
            case 17:
                return new EvolvePower(AbstractDungeon.player, 2);
            case 18:
                return new FeelNoPainPower(AbstractDungeon.player, 4);
            case 19:
                return new ForesightPower(AbstractDungeon.player, 4);
            case 20:
                return new HeatsinkPower(AbstractDungeon.player, 2);
            case 21:
                return new IntangiblePlayerPower(AbstractDungeon.player, 1);
            case 22:
                return new InvinciblePower(AbstractDungeon.player, Math.min(Math.max(AbstractDungeon.player.maxHealth / 2, 15), 80));
            case 23:
                return new MalleablePower(AbstractDungeon.player, 1);
            case 24:
                return new MasterRealityPower(AbstractDungeon.player);
            case 25:
                return new MentalFortressPower(AbstractDungeon.player, 6);
            case 26:
                return new MetallicizePower(AbstractDungeon.player, 6);
            case 27:
                return new OmegaPower(AbstractDungeon.player, 50);
            case 28:
                return new PlatedArmorPower(AbstractDungeon.player, 8);
            case 29:
                return new RegenPower(AbstractDungeon.player, 6);
            case 30:
                return new RetainCardPower(AbstractDungeon.player, 2);
            case 31:
                return new RitualPower(AbstractDungeon.player, 1, true);
            case 32:
                return new StrengthPower(AbstractDungeon.player, 4);
            case 33:
                return new StudyPower(AbstractDungeon.player, 1);
            case 34:
                return new ThornsPower(AbstractDungeon.player, 4);
            default:
                return new RushdownPower(AbstractDungeon.player, 0);
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
