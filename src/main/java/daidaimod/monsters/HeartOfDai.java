package daidaimod.monsters;

import com.badlogic.gdx.graphics.Color;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.HeartAnimListener;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.HeartBuffEffect;
import com.megacrit.cardcrawl.vfx.combat.HeartMegaDebuffEffect;
import daidaimod.powers.CaptainRewardsPower;
import daidaimod.powers.DizzyingPunchesPower;
import daidaimod.powers.FallingMasterPower;
import daidaimod.powers.RushdownEnthusiastPower;
import daidaimod.vfx.ConsecutivePunchesEffect;
import daidaimod.vfx.HeavyPunchEffect;

public class HeartOfDai extends AbstractMonster {
    public static final String ID = "daidaimod:HeartOfDai";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    public static int attackType = -1;
    private final HeartAnimListener animListener = new HeartAnimListener();
    private final int bloodHitCount;
    private boolean isFirstMove = true;
    private int moveCount = 0;
    private int buffCount = 0;

    public HeartOfDai() {
        super(NAME, ID, 750, 30.0F, -30.0F, 476.0F, 410.0F, null, -50.0F, 30.0F);
        this.loadAnimation("daidaimod/images/monsters/HeartOfDai.atlas", "daidaimod/images/monsters/HeartOfDai.json", 1.0F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        e.setTimeScale(1.5F);
        this.state.addListener(this.animListener);
        this.type = EnemyType.BOSS;

        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(800);
        } else {
            this.setHp(750);
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.damage.add(new DamageInfo(this, 45));
            this.damage.add(new DamageInfo(this, 2));
            this.bloodHitCount = 15;
        } else {
            this.damage.add(new DamageInfo(this, 40));
            this.damage.add(new DamageInfo(this, 2));
            this.bloodHitCount = 12;
        }
    }

    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_ENDING");

        int invincibleAmount = 300;
        int beatAmount = 1;
        if (AbstractDungeon.ascensionLevel >= 19) {
            invincibleAmount -= 100;
            ++beatAmount;
        }

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new RushdownEnthusiastPower(this)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new FallingMasterPower(this)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new CaptainRewardsPower(this)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new InvinciblePower(this, invincibleAmount), invincibleAmount));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BeatOfDeathPower(this, beatAmount), beatAmount));
    }

    public void takeTurn() {
        attackType = -1;
        switch (this.nextMove) {
            case 1:
                attackType = 0;
                if (Settings.FAST_MODE) {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new ConsecutivePunchesEffect(this.hb.cX, this.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.bloodHitCount), 0.25F));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new ConsecutivePunchesEffect(this.hb.cX, this.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.bloodHitCount), 0.6F));
                }
                for (int i = 0; i < this.bloodHitCount; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AttackEffect.BLUNT_HEAVY, true));
                }
                break;
            case 2:
                attackType = 1;
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new HeavyPunchEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, Color.PURPLE), 0.5F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AttackEffect.BLUNT_HEAVY));
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new HeartMegaDebuffEffect()));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, 2, true), 2));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, 2, true), 2));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, 2, true), 2));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Dazed(), 1, true, false, false, (float) Settings.WIDTH * 0.2F, (float) Settings.HEIGHT / 2.0F));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Slimed(), 1, true, false, false, (float) Settings.WIDTH * 0.35F, (float) Settings.HEIGHT / 2.0F));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Wound(), 1, true, false, false, (float) Settings.WIDTH * 0.5F, (float) Settings.HEIGHT / 2.0F));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Burn(), 1, true, false, false, (float) Settings.WIDTH * 0.65F, (float) Settings.HEIGHT / 2.0F));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new VoidCard(), 1, true, false, false, (float) Settings.WIDTH * 0.8F, (float) Settings.HEIGHT / 2.0F));
                break;
            case 4:
                int additionalAmount = 0;
                if (this.hasPower("Strength") && this.getPower("Strength").amount < 0) {
                    additionalAmount = -this.getPower("Strength").amount;
                }

                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderFlashEffect(new Color(0.8F, 0.5F, 1.0F, 1.0F))));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new HeartBuffEffect(this.hb.cX, this.hb.cY)));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, additionalAmount + 2), additionalAmount + 2));
                switch (this.buffCount) {
                    case 0:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 2), 2));
                        break;
                    case 1:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BeatOfDeathPower(this, 1), 1));
                        break;
                    case 2:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new DizzyingPunchesPower(this)));
                        break;
                    case 3:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, 10), 10));
                        break;
                    default:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, 50), 50));
                }
                ++this.buffCount;
                break;
            case 5:
                AbstractDungeon.actionManager.addToBottom(new TextAboveCreatureAction(this, TextAboveCreatureAction.TextType.STUNNED));
                break;
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    protected void getMove(int num) {
        if (this.isFirstMove) {
            this.setMove(MOVES[0], (byte) 3, Intent.STRONG_DEBUFF);
            this.isFirstMove = false;
        } else if (this.lastMove((byte) 5)) {
            if (this.lastMoveBefore((byte) 1)) {
                this.setMove(MOVES[2], (byte) 1, Intent.ATTACK, this.damage.get(1).base, this.bloodHitCount, true);
            } else if (this.lastMoveBefore((byte) 2)) {
                this.setMove(MOVES[1], (byte) 2, Intent.ATTACK, this.damage.get(0).base);
            } else if (this.lastMoveBefore((byte) 3)) {
                this.setMove(MOVES[0], (byte) 3, Intent.STRONG_DEBUFF);
            } else if (this.lastMoveBefore((byte) 4)) {
                this.setMove((byte) 4, Intent.BUFF);
            }
        } else {
            switch (this.moveCount % 3) {
                case 0:
                    if (AbstractDungeon.aiRng.randomBoolean()) {
                        this.setMove(MOVES[2], (byte) 1, Intent.ATTACK, this.damage.get(1).base, this.bloodHitCount, true);
                    } else {
                        this.setMove(MOVES[1], (byte) 2, Intent.ATTACK, this.damage.get(0).base);
                    }
                    break;
                case 1:
                    if (this.lastMove((byte) 1)) {
                        this.setMove(MOVES[1], (byte) 2, Intent.ATTACK, this.damage.get(0).base);
                    } else {
                        this.setMove(MOVES[2], (byte) 1, Intent.ATTACK, this.damage.get(1).base, this.bloodHitCount, true);
                    }
                    break;
                default:
                    this.setMove((byte) 4, Intent.BUFF);
            }
            ++this.moveCount;
        }
    }

    public void die() {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            super.die();
            this.state.removeListener(this.animListener);
            this.onBossVictoryLogic();
            this.onFinalBossVictoryLogic();
            CardCrawlGame.stopClock = true;
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}
