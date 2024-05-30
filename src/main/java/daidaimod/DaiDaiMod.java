package daidaimod;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.eventUtil.AddEventParams;
import basemod.eventUtil.EventUtils;
import basemod.eventUtil.util.ConditionalEvent;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.*;
import daidaimod.cards.FakeFall;
import daidaimod.cards.GenuineFall;
import daidaimod.cards.GiftOfDai;
import daidaimod.cards.PunchDai;
import daidaimod.events.FallingDai;
import daidaimod.monsters.HeartOfDai;
import daidaimod.powers.CaptainRewardsPower;
import daidaimod.powers.DizzyingPunchesPower;
import daidaimod.powers.FallingMasterPower;
import daidaimod.powers.RushdownEnthusiastPower;
import daidaimod.relics.BlessingOfDai;
import daidaimod.util.TextureLoader;
import daidaimod.vfx.ConsecutivePunchesEffect;
import daidaimod.vfx.HeavyPunchEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@SpireInitializer
public class DaiDaiMod implements EditStringsSubscriber, EditKeywordsSubscriber, EditCardsSubscriber, EditRelicsSubscriber, PostInitializeSubscriber {
    public static final Logger logger = LogManager.getLogger(DaiDaiMod.class.getName());
    private static SpireConfig config;

    public DaiDaiMod() {
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        new DaiDaiMod();

        initializeConfig();
    }

    @Override
    public void receiveEditStrings() {
        logger.info("DaiDaiMod | Initializing strings...");

        loadLocalizationFiles(Settings.language);
    }

    @Override
    public void receiveEditKeywords() {
        logger.info("DaiDaiMod | Initializing keywords...");

        if (Settings.language == Settings.GameLanguage.ZHS) {
            BaseMod.addKeyword(new String[]{"击晕"}, "被击晕的敌人下一回合不能行动。");
        } else {
            BaseMod.addKeyword(new String[]{"stun"}, "A stunned enemy cannot move on its next turn.");
        }
    }

    @Override
    public void receiveEditCards() {
        logger.info("DaiDaiMod | Initializing cards...");

        BaseMod.addCard(new FakeFall());
        BaseMod.addCard(new GenuineFall());
        BaseMod.addCard(new GiftOfDai());
        BaseMod.addCard(new PunchDai());
    }

    @Override
    public void receiveEditRelics() {
        logger.info("DaiDaiMod | Initializing relics...");

        BaseMod.addRelic(new BlessingOfDai(), RelicType.SHARED);
    }

    @Override
    public void receivePostInitialize() {
        initializeConfigPanel();

        initializeEvents();

        initializeMonsters();

        initializeTextures();
    }

    private static void initializeConfig() {
        logger.info("DaiDaiMod | Initializing config...");

        try {
            config = new SpireConfig("DaiDaiMod", "DaiDaiModConfig");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean getConfigBool(String key) {
        return config.getBool(key);
    }

    private void setConfigBool(String key, boolean b) {
        config.setBool(key, b);

        try {
            config.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeConfigPanel() {
        logger.info("DaiDaiMod | Initializing config panel...");

        ModPanel configPanel = new ModPanel();
        BaseMod.registerModBadge(TextureLoader.getTexture("daidaimod/images/ui/Badge.png"), "DaiDaiMod", "瓶装易碎柯妮法", "呆呆的假摔mod！", configPanel);

        UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("daidaimod:Config");

        ModLabeledToggleButton toggleButton1 = new ModLabeledToggleButton(uiStrings.TEXT[0], uiStrings.TEXT[1], 400f, 700f, Settings.CREAM_COLOR, FontHelper.buttonLabelFont, getConfigBool("forceBlessingOfDai"), configPanel,
                (me) -> {
                    // Label update function.
                },
                (me) -> {
                    // Toggle function.
                    setConfigBool("forceBlessingOfDai", me.enabled);
                });
        ModLabeledToggleButton toggleButton2 = new ModLabeledToggleButton(uiStrings.TEXT[2], uiStrings.TEXT[3], 400f, 600f, Settings.CREAM_COLOR, FontHelper.buttonLabelFont, getConfigBool("forceFallingDai"), configPanel,
                (me) -> {
                    // Label update function.
                },
                (me) -> {
                    // Toggle function.
                    setConfigBool("forceFallingDai", me.enabled);
                    for (ConditionalEvent<? extends AbstractEvent> conditionalEvent : EventUtils.overrideEvents.get("Falling")) {
                        if (conditionalEvent.eventClass == FallingDai.class) {
                            // The conditions below should be consistent with the bonus conditions of the FallingDai event.
                            if (getConfigBool("forceFallingDai")) {
                                EventUtils.overrideBonusConditions.put(conditionalEvent, () -> true);
                            } else {
                                EventUtils.overrideBonusConditions.put(conditionalEvent, () -> AbstractDungeon.player.hasRelic("daidaimod:BlessingOfDai"));
                            }
                        }
                    }
                });
        ModLabeledToggleButton toggleButton3 = new ModLabeledToggleButton(uiStrings.TEXT[4], uiStrings.TEXT[5], 400f, 500f, Settings.CREAM_COLOR, FontHelper.buttonLabelFont, getConfigBool("challengeHeartOfDai"), configPanel,
                (me) -> {
                    // Label update function.
                },
                (me) -> {
                    // Toggle function.
                    setConfigBool("challengeHeartOfDai", me.enabled);
                });
        configPanel.addUIElement(toggleButton1);
        configPanel.addUIElement(toggleButton2);
        configPanel.addUIElement(toggleButton3);

        if (Settings.language == Settings.GameLanguage.ZHS) {
            ModLabeledToggleButton toggleButton4 = new ModLabeledToggleButton(uiStrings.TEXT[6], uiStrings.TEXT[7], 400f, 400f, Settings.CREAM_COLOR, FontHelper.buttonLabelFont, getConfigBool("useFunnyText"), configPanel,
                    (me) -> {
                        // Label update function.
                    },
                    (me) -> {
                        // Toggle function.
                        setConfigBool("useFunnyText", me.enabled);
                    });
            configPanel.addUIElement(toggleButton4);
        }
    }

    private void initializeEvents() {
        logger.info("DaiDaiMod | Initializing events...");

        if (getConfigBool("forceFallingDai")) {
            // Replace the original Falling event unconditionally.
            BaseMod.addEvent(new AddEventParams.Builder(FallingDai.ID, FallingDai.class).dungeonID(TheBeyond.ID).overrideEvent("Falling").bonusCondition(() -> true).create());
        } else {
            BaseMod.addEvent(new AddEventParams.Builder(FallingDai.ID, FallingDai.class).dungeonID(TheBeyond.ID).overrideEvent("Falling").bonusCondition(() -> {
                // The original Falling event is replaced only if the condition is met.
                // In Endless runs, future Falling events will also be replaced.
                return AbstractDungeon.player.hasRelic("daidaimod:BlessingOfDai");
            }).create());
        }
    }

    private void initializeMonsters() {
        logger.info("DaiDaiMod | Initializing monsters...");

        BaseMod.addMonster(HeartOfDai.ID, CardCrawlGame.languagePack.getMonsterStrings("daidaimod:HeartOfDai").NAME, HeartOfDai::new);
        BaseMod.addBoss(TheEnding.ID, HeartOfDai.ID, "daidaimod/images/map/HeartMapIcon.png", "daidaimod/images/map/HeartMapIconOutline.png");
    }

    private void initializeTextures() {
        logger.info("DaiDaiMod | Initializing textures...");

        com.megacrit.cardcrawl.powers.AbstractPower.atlas.addRegion("48/" + CaptainRewardsPower.POWER_ID, TextureLoader.getTexture("daidaimod/images/powers/CaptainRewardsPower48.png"), 0, 0, 48, 48);
        com.megacrit.cardcrawl.powers.AbstractPower.atlas.addRegion("128/" + CaptainRewardsPower.POWER_ID, TextureLoader.getTexture("daidaimod/images/powers/CaptainRewardsPower128.png"), 0, 0, 128, 128);
        com.megacrit.cardcrawl.powers.AbstractPower.atlas.addRegion("48/" + DizzyingPunchesPower.POWER_ID, TextureLoader.getTexture("daidaimod/images/powers/DizzyingPunchesPower48.png"), 0, 0, 48, 48);
        com.megacrit.cardcrawl.powers.AbstractPower.atlas.addRegion("128/" + DizzyingPunchesPower.POWER_ID, TextureLoader.getTexture("daidaimod/images/powers/DizzyingPunchesPower128.png"), 0, 0, 128, 128);
        com.megacrit.cardcrawl.powers.AbstractPower.atlas.addRegion("48/" + FallingMasterPower.POWER_ID, TextureLoader.getTexture("daidaimod/images/powers/FallingMasterPower48.png"), 0, 0, 48, 48);
        com.megacrit.cardcrawl.powers.AbstractPower.atlas.addRegion("128/" + FallingMasterPower.POWER_ID, TextureLoader.getTexture("daidaimod/images/powers/FallingMasterPower128.png"), 0, 0, 128, 128);
        com.megacrit.cardcrawl.powers.AbstractPower.atlas.addRegion("48/" + RushdownEnthusiastPower.POWER_ID, TextureLoader.getTexture("daidaimod/images/powers/RushdownEnthusiastPower48.png"), 0, 0, 48, 48);
        com.megacrit.cardcrawl.powers.AbstractPower.atlas.addRegion("128/" + RushdownEnthusiastPower.POWER_ID, TextureLoader.getTexture("daidaimod/images/powers/RushdownEnthusiastPower128.png"), 0, 0, 128, 128);

        ImageMaster.vfxAtlas.addRegion("combat/" + ConsecutivePunchesEffect.EFFECT_ID + "1", TextureLoader.getTexture("daidaimod/images/vfx/ConsecutivePunchesEffect1.png"), 0, 0, 48, 40);
        ImageMaster.vfxAtlas.addRegion("combat/" + ConsecutivePunchesEffect.EFFECT_ID + "2", TextureLoader.getTexture("daidaimod/images/vfx/ConsecutivePunchesEffect2.png"), 0, 0, 48, 40);
        ImageMaster.vfxAtlas.addRegion("combat/" + HeavyPunchEffect.EFFECT_ID, TextureLoader.getTexture("daidaimod/images/vfx/HeavyPunchEffect.png"), 0, 0, 216, 261);
    }

    private void loadLocalizationFiles(Settings.GameLanguage language) {
        if (language == Settings.GameLanguage.ZHS) {
            BaseMod.loadCustomStringsFile(CardStrings.class, "daidaimod/localization/zhs/cards.json");
            BaseMod.loadCustomStringsFile(EventStrings.class, "daidaimod/localization/zhs/events.json");
            BaseMod.loadCustomStringsFile(MonsterStrings.class, "daidaimod/localization/zhs/monsters.json");
            BaseMod.loadCustomStringsFile(PowerStrings.class, "daidaimod/localization/zhs/powers.json");
            BaseMod.loadCustomStringsFile(RelicStrings.class, "daidaimod/localization/zhs/relics.json");
            BaseMod.loadCustomStringsFile(UIStrings.class, "daidaimod/localization/zhs/ui.json");
            if (getConfigBool("useFunnyText")) {
                BaseMod.loadCustomStringsFile(AchievementStrings.class, "daidaimod/localization/zhs/achievements_funny.json");
                BaseMod.loadCustomStringsFile(CardStrings.class, "daidaimod/localization/zhs/cards_funny.json");
                BaseMod.loadCustomStringsFile(CharacterStrings.class, "daidaimod/localization/zhs/characters_funny.json");
                BaseMod.loadCustomStringsFile(EventStrings.class, "daidaimod/localization/zhs/events_funny.json");
                BaseMod.loadCustomStringsFile(MonsterStrings.class, "daidaimod/localization/zhs/monsters_funny.json");
                BaseMod.loadCustomStringsFile(PotionStrings.class, "daidaimod/localization/zhs/potions_funny.json");
                BaseMod.loadCustomStringsFile(PowerStrings.class, "daidaimod/localization/zhs/powers_funny.json");
                BaseMod.loadCustomStringsFile(RelicStrings.class, "daidaimod/localization/zhs/relics_funny.json");
                BaseMod.loadCustomStringsFile(UIStrings.class, "daidaimod/localization/zhs/ui_funny.json");
            }
        } else {
            BaseMod.loadCustomStringsFile(CardStrings.class, "daidaimod/localization/eng/cards.json");
            BaseMod.loadCustomStringsFile(EventStrings.class, "daidaimod/localization/eng/events.json");
            BaseMod.loadCustomStringsFile(MonsterStrings.class, "daidaimod/localization/eng/monsters.json");
            BaseMod.loadCustomStringsFile(PowerStrings.class, "daidaimod/localization/eng/powers.json");
            BaseMod.loadCustomStringsFile(RelicStrings.class, "daidaimod/localization/eng/relics.json");
            BaseMod.loadCustomStringsFile(UIStrings.class, "daidaimod/localization/eng/ui.json");
        }
    }
}
