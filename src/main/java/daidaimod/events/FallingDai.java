package daidaimod.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import java.util.HashMap;

public class FallingDai extends AbstractImageEvent {
    public static final String ID = "daidaimod:FallingDai";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final HashMap<String, Integer> cardRank = new HashMap<>();
    private boolean skill;
    private boolean power;
    private boolean attack;
    private AbstractCard skillCard;
    private AbstractCard powerCard;
    private AbstractCard attackCard;
    private CurrentScreen screen;

    public FallingDai() {
        super(NAME, DESCRIPTIONS[0], "daidaimod/images/events/FallingDai.png");
        this.screen = CurrentScreen.INTRO;
        this.setCards();
        this.imageEventText.setDialogOption(OPTIONS[0]);
    }

    public void onEnterRoom() {
        if (Settings.AMBIANCE_ON) {
            CardCrawlGame.sound.play("EVENT_FALLING");
        }
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (this.screen) {
            case INTRO:
                this.screen = CurrentScreen.CHOICE;
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                this.imageEventText.clearAllDialogs();
                if (!this.skill && !this.power && !this.attack) {
                    this.imageEventText.setDialogOption(OPTIONS[8]);
                } else {
                    if (this.skill) {
                        this.imageEventText.setDialogOption(OPTIONS[1] + FontHelper.colorString(this.skillCard.name, "r"), this.skillCard.makeStatEquivalentCopy());
                    } else {
                        this.imageEventText.setDialogOption(OPTIONS[2], true);
                    }

                    if (this.power) {
                        this.imageEventText.setDialogOption(OPTIONS[3] + FontHelper.colorString(this.powerCard.name, "r"), this.powerCard.makeStatEquivalentCopy());
                    } else {
                        this.imageEventText.setDialogOption(OPTIONS[4], true);
                    }

                    if (this.attack) {
                        this.imageEventText.setDialogOption(OPTIONS[5] + FontHelper.colorString(this.attackCard.name, "r"), this.attackCard.makeStatEquivalentCopy());
                    } else {
                        this.imageEventText.setDialogOption(OPTIONS[6], true);
                    }
                }
                break;
            case CHOICE:
                this.screen = CurrentScreen.RESULT;
                this.imageEventText.clearAllDialogs();
                this.imageEventText.setDialogOption(OPTIONS[7]);
                switch (buttonPressed) {
                    case 0:
                        if (!this.skill && !this.power && !this.attack) {
                            this.imageEventText.updateBodyText(DESCRIPTIONS[5]);
                            boolean potionBroken = false;
                            for (AbstractPotion p : AbstractDungeon.player.potions) {
                                if (!(p instanceof PotionSlot)) {
                                    potionBroken = true;
                                    AbstractDungeon.player.removePotion(p);
                                }
                            }
                            if (potionBroken) {
                                CardCrawlGame.sound.play("RELIC_DROP_CLINK");
                            }
                            logMetricIgnored("Falling");
                        } else {
                            this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                            AbstractDungeon.effectList.add(new PurgeCardEffect(this.skillCard));
                            AbstractDungeon.player.masterDeck.removeCard(this.skillCard);
                            logMetricCardRemoval("Falling", "Removed Skill", this.skillCard);
                        }

                        return;
                    case 1:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        AbstractDungeon.effectList.add(new PurgeCardEffect(this.powerCard));
                        AbstractDungeon.player.masterDeck.removeCard(this.powerCard);
                        logMetricCardRemoval("Falling", "Removed Power", this.powerCard);
                        return;
                    case 2:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                        AbstractDungeon.effectList.add(new PurgeCardEffect(this.attackCard));
                        logMetricCardRemoval("Falling", "Removed Attack", this.attackCard);
                        AbstractDungeon.player.masterDeck.removeCard(this.attackCard);
                        return;
                    default:
                        return;
                }
            default:
                this.openMap();
        }
    }

    private void setCards() {
        this.skill = CardHelper.hasCardWithType(AbstractCard.CardType.SKILL);
        this.power = CardHelper.hasCardWithType(AbstractCard.CardType.POWER);
        this.attack = CardHelper.hasCardWithType(AbstractCard.CardType.ATTACK);

        if (this.skill) {
            int rank = -1;
            for (AbstractCard c : CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck).group) {
                if (c.type == AbstractCard.CardType.SKILL) {
                    int currentRank = getCardRank(c);
                    if (currentRank >= rank) {
                        rank = currentRank;
                        this.skillCard = c;
                    }
                }
            }
        }

        if (this.power) {
            int rank = -1;
            for (AbstractCard c : CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck).group) {
                if (c.type == AbstractCard.CardType.POWER) {
                    int currentRank = getCardRank(c);
                    if (currentRank >= rank) {
                        rank = currentRank;
                        this.powerCard = c;
                    }
                }
            }
        }

        if (this.attack) {
            int rank = -1;
            for (AbstractCard c : CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck).group) {
                if (c.type == AbstractCard.CardType.ATTACK) {
                    int currentRank = getCardRank(c);
                    if (currentRank >= rank) {
                        rank = currentRank;
                        this.attackCard = c;
                    }
                }
            }
        }
    }

    private static int getRarityRank(AbstractCard c) {
        switch (c.rarity) {
            case COMMON:
            case SPECIAL:
                return 2;
            case UNCOMMON:
                return 4;
            case RARE:
                return 6;
            default:
                return 0;
        }
    }

    public static int getCardRank(AbstractCard c) {
        if (c.color != AbstractCard.CardColor.RED && c.color != AbstractCard.CardColor.GREEN && c.color != AbstractCard.CardColor.BLUE && c.color != AbstractCard.CardColor.PURPLE && c.color != AbstractCard.CardColor.COLORLESS) {
            // This card belongs to a mod character.
            return getRarityRank(c) + c.timesUpgraded;
        } else {
            if (cardRank.get(c.cardID) == null) {
                return c.timesUpgraded;
            } else {
                return cardRank.get(c.cardID) + c.timesUpgraded;
            }
        }
    }

    private static void setCardRank() {
        // Cards are ranked on a 0-8 basis, by their un-upgraded versions.

        // IRONCLAD ATTACK.
        cardRank.put("Bash", 1);
        cardRank.put("Anger", 1);
        cardRank.put("Body Slam", 6);
        cardRank.put("Clash", 1);
        cardRank.put("Cleave", 1);
        cardRank.put("Clothesline", 1);
        cardRank.put("Headbutt", 2);
        cardRank.put("Heavy Blade", 3);
        cardRank.put("Iron Wave", 1);
        cardRank.put("Perfected Strike", 1);
        cardRank.put("Pommel Strike", 4);
        cardRank.put("Sword Boomerang", 3);
        cardRank.put("Thunderclap", 1);
        cardRank.put("Twin Strike", 1);
        cardRank.put("Wild Strike", 1);
        cardRank.put("Blood for Blood", 3);
        cardRank.put("Carnage", 1);
        cardRank.put("Dropkick", 3);
        cardRank.put("Hemokinesis", 2);
        cardRank.put("Pummel", 2);
        cardRank.put("Rampage", 3);
        cardRank.put("Reckless Charge", 4);
        cardRank.put("Searing Blow", 0);
        cardRank.put("Sever Soul", 4);
        cardRank.put("Uppercut", 1);
        cardRank.put("Whirlwind", 3);
        cardRank.put("Bludgeon", 2);
        cardRank.put("Feed", 5);
        cardRank.put("Fiend Fire", 4);
        cardRank.put("Immolate", 2);
        cardRank.put("Reaper", 6);

        // SILENT ATTACK.
        cardRank.put("Neutralize", 2);
        cardRank.put("Bane", 1);
        cardRank.put("Dagger Spray", 1);
        cardRank.put("Dagger Throw", 3);
        cardRank.put("Flying Knee", 1);
        cardRank.put("Poisoned Stab", 1);
        cardRank.put("Quick Slash", 1);
        cardRank.put("Slice", 2);
        cardRank.put("Sucker Punch", 1);
        cardRank.put("Underhanded Strike", 5);
        cardRank.put("All Out Attack", 1);
        cardRank.put("Backstab", 1);
        cardRank.put("Choke", 2);
        cardRank.put("Dash", 1);
        cardRank.put("Endless Agony", 2);
        cardRank.put("Eviscerate", 6);
        cardRank.put("Finisher", 3);
        cardRank.put("Flechettes", 2);
        cardRank.put("Heel Hook", 2);
        cardRank.put("Masterful Stab", 3);
        cardRank.put("Predator", 3);
        cardRank.put("Riddle With Holes", 1);
        cardRank.put("Skewer", 1);
        cardRank.put("Die Die Die", 2);
        cardRank.put("Glass Knife", 1);
        cardRank.put("Grand Finale", 7);
        cardRank.put("Unload", 2);

        // DEFECT ATTACK.
        cardRank.put("Ball Lightning", 2);
        cardRank.put("Barrage", 3);
        cardRank.put("Beam Cell", 2);
        cardRank.put("Cold Snap", 3);
        cardRank.put("Compile Driver", 4);
        cardRank.put("Gash", 3);
        cardRank.put("Go for the Eyes", 4);
        cardRank.put("Rebound", 2);
        cardRank.put("Streamline", 2);
        cardRank.put("Sweeping Beam", 1);
        cardRank.put("Blizzard", 4);
        cardRank.put("Doom and Gloom", 2);
        cardRank.put("FTL", 1);
        cardRank.put("Lockon", 1);
        cardRank.put("Melter", 1);
        cardRank.put("Rip and Tear", 1);
        cardRank.put("Scrape", 3);
        cardRank.put("Sunder", 2);
        cardRank.put("All For One", 6);
        cardRank.put("Core Surge", 6);
        cardRank.put("Hyperbeam", 4);
        cardRank.put("Meteor Strike", 6);
        cardRank.put("Thunder Strike", 5);

        // WATCHER ATTACK.
        cardRank.put("Eruption", 7);
        cardRank.put("BowlingBash", 3);
        cardRank.put("Consecrate", 1);
        cardRank.put("CrushJoints", 4);
        cardRank.put("CutThroughFate", 3);
        cardRank.put("EmptyFist", 1);
        cardRank.put("FlurryOfBlows", 2);
        cardRank.put("FlyingSleeves", 1);
        cardRank.put("FollowUp", 1);
        cardRank.put("JustLucky", 1);
        cardRank.put("SashWhip", 1);
        cardRank.put("CarveReality", 1);
        cardRank.put("Conclude", 3);
        cardRank.put("FearNoEvil", 7);
        cardRank.put("ReachHeaven", 1);
        cardRank.put("SandsOfTime", 1);
        cardRank.put("SignatureMove", 5);
        cardRank.put("TalkToTheHand", 6);
        cardRank.put("Tantrum", 8);
        cardRank.put("Wallop", 6);
        cardRank.put("Weave", 1);
        cardRank.put("WheelKick", 3);
        cardRank.put("WindmillStrike", 1);
        cardRank.put("Brilliance", 4);
        cardRank.put("LessonLearned", 5);
        cardRank.put("Ragnarok", 5);

        // COLORLESS ATTACK.
        cardRank.put("Dramatic Entrance", 1);
        cardRank.put("Flash of Steel", 4);
        cardRank.put("Mind Blast", 1);
        cardRank.put("Swift Strike", 2);
        cardRank.put("HandOfGreed", 3);
        cardRank.put("Bite", 1);
        cardRank.put("RitualDagger", 4);
        cardRank.put("Expunger", 3);
        cardRank.put("Shiv", 1);
        cardRank.put("Smite", 1);
        cardRank.put("ThroughViolence", 2);
        cardRank.put("daidaimod:PunchDai", 8);

        // IRONCLAD SKILL.
        cardRank.put("Armaments", 4);
        cardRank.put("Flex", 2);
        cardRank.put("Havoc", 3);
        cardRank.put("Shrug It Off", 3);
        cardRank.put("True Grit", 3);
        cardRank.put("Warcry", 1);
        cardRank.put("Battle Trance", 5);
        cardRank.put("Bloodletting", 6);
        cardRank.put("Burning Pact", 7);
        cardRank.put("Disarm", 4);
        cardRank.put("Dual Wield", 3);
        cardRank.put("Entrench", 4);
        cardRank.put("Flame Barrier", 3);
        cardRank.put("Ghostly Armor", 1);
        cardRank.put("Infernal Blade", 1);
        cardRank.put("Intimidate", 1);
        cardRank.put("Power Through", 5);
        cardRank.put("Rage", 4);
        cardRank.put("Second Wind", 7);
        cardRank.put("Seeing Red", 2);
        cardRank.put("Sentinel", 2);
        cardRank.put("Shockwave", 5);
        cardRank.put("Spot Weakness", 3);
        cardRank.put("Double Tap", 2);
        cardRank.put("Exhume", 4);
        cardRank.put("Impervious", 5);
        cardRank.put("Limit Break", 6);
        cardRank.put("Offering", 8);

        // SILENT SKILL.
        cardRank.put("Survivor", 3);
        cardRank.put("Acrobatics", 8);
        cardRank.put("Backflip", 4);
        cardRank.put("Blade Dance", 4);
        cardRank.put("Cloak And Dagger", 2);
        cardRank.put("Deadly Poison", 1);
        cardRank.put("Deflect", 3);
        cardRank.put("Dodge and Roll", 2);
        cardRank.put("Outmaneuver", 3);
        cardRank.put("PiercingWail", 5);
        cardRank.put("Prepared", 4);
        cardRank.put("Blur", 4);
        cardRank.put("Bouncing Flask", 4);
        cardRank.put("Calculated Gamble", 8);
        cardRank.put("Catalyst", 6);
        cardRank.put("Concentrate", 6);
        cardRank.put("Crippling Poison", 1);
        cardRank.put("Distraction", 1);
        cardRank.put("Escape Plan", 2);
        cardRank.put("Expertise", 5);
        cardRank.put("Leg Sweep", 1);
        cardRank.put("Reflex", 7);
        cardRank.put("Setup", 3);
        cardRank.put("Tactician", 7);
        cardRank.put("Terror", 5);
        cardRank.put("Adrenaline", 5);
        cardRank.put("Bullet Time", 4);
        cardRank.put("Burst", 5);
        cardRank.put("Corpse Explosion", 5);
        cardRank.put("Doppelganger", 1);
        cardRank.put("Malaise", 4);
        cardRank.put("Night Terror", 6);
        cardRank.put("Phantasmal Killer", 5);
        cardRank.put("Storm of Steel", 2);
        cardRank.put("Venomology", 5);

        // DEFECT SKILL.
        cardRank.put("Dualcast", 1);
        cardRank.put("Zap", 2);
        cardRank.put("Conserve Battery", 2);
        cardRank.put("Coolheaded", 6);
        cardRank.put("Hologram", 5);
        cardRank.put("Leap", 1);
        cardRank.put("Redo", 3);
        cardRank.put("Stack", 1);
        cardRank.put("Steam", 1);
        cardRank.put("Turbo", 6);
        cardRank.put("Aggregate", 2);
        cardRank.put("Auto Shields", 1);
        cardRank.put("BootSequence", 2);
        cardRank.put("Chaos", 2);
        cardRank.put("Chill", 3);
        cardRank.put("Consume", 5);
        cardRank.put("Darkness", 3);
        cardRank.put("Double Energy", 3);
        cardRank.put("Force Field", 1);
        cardRank.put("Fusion", 3);
        cardRank.put("Genetic Algorithm", 5);
        cardRank.put("Glacier", 6);
        cardRank.put("Recycle", 5);
        cardRank.put("Reinforced Body", 4);
        cardRank.put("Reprogram", 6);
        cardRank.put("Skim", 7);
        cardRank.put("Steam Power", 2);
        cardRank.put("Tempest", 3);
        cardRank.put("Undo", 4);
        cardRank.put("White Noise", 3);
        cardRank.put("Amplify", 3);
        cardRank.put("Fission", 5);
        cardRank.put("Multi-Cast", 3);
        cardRank.put("Rainbow", 2);
        cardRank.put("Reboot", 4);
        cardRank.put("Seek", 5);

        // WATCHER SKILL.
        cardRank.put("Vigilance", 5);
        cardRank.put("ClearTheMind", 2);
        cardRank.put("Crescendo", 3);
        cardRank.put("EmptyBody", 1);
        cardRank.put("Evaluate", 1);
        cardRank.put("Halt", 5);
        cardRank.put("PathToVictory", 2);
        cardRank.put("Prostrate", 3);
        cardRank.put("Protect", 1);
        cardRank.put("ThirdEye", 2);
        cardRank.put("Collect", 1);
        cardRank.put("DeceiveReality", 1);
        cardRank.put("EmptyMind", 7);
        cardRank.put("ForeignInfluence", 1);
        cardRank.put("Indignation", 7);
        cardRank.put("InnerPeace", 7);
        cardRank.put("Meditate", 6);
        cardRank.put("Perseverance", 1);
        cardRank.put("Pray", 4);
        cardRank.put("Sanctity", 1);
        cardRank.put("Swivel", 2);
        cardRank.put("Vengeance", 5);
        cardRank.put("WaveOfTheHand", 2);
        cardRank.put("Worship", 2);
        cardRank.put("WreathOfFlame", 3);
        cardRank.put("Alpha", 1);
        cardRank.put("Blasphemy", 4);
        cardRank.put("ConjureBlade", 1);
        cardRank.put("DeusExMachina", 6);
        cardRank.put("Judgement", 1);
        cardRank.put("Omniscience", 6);
        cardRank.put("Scrawl", 8);
        cardRank.put("SpiritShield", 3);
        cardRank.put("Vault", 7);
        cardRank.put("Wish", 5);

        // COLORLESS SKILL.
        cardRank.put("Bandage Up", 1);
        cardRank.put("Blind", 1);
        cardRank.put("Dark Shackles", 5);
        cardRank.put("Deep Breath", 3);
        cardRank.put("Discovery", 3);
        cardRank.put("Enlightenment", 5);
        cardRank.put("Finesse", 4);
        cardRank.put("Forethought", 2);
        cardRank.put("Good Instincts", 2);
        cardRank.put("Impatience", 5);
        cardRank.put("Jack Of All Trades", 1);
        cardRank.put("Madness", 5);
        cardRank.put("Panacea", 6);
        cardRank.put("PanicButton", 3);
        cardRank.put("Purity", 7);
        cardRank.put("Trip", 2);
        cardRank.put("Apotheosis", 8);
        cardRank.put("Chrysalis", 2);
        cardRank.put("Master of Strategy", 5);
        cardRank.put("Metamorphosis", 1);
        cardRank.put("Secret Technique", 3);
        cardRank.put("Secret Weapon", 2);
        cardRank.put("The Bomb", 3);
        cardRank.put("Thinking Ahead", 1);
        cardRank.put("Transmutation", 1);
        cardRank.put("Violence", 3);
        cardRank.put("Ghostly", 6);
        cardRank.put("J.A.X.", 2);
        cardRank.put("Beta", 2);
        cardRank.put("Insight", 2);
        cardRank.put("Miracle", 2);
        cardRank.put("Safety", 1);
        cardRank.put("daidaimod:GiftOfDai", 5);

        // IRONCLAD POWER.
        cardRank.put("Combust", 1);
        cardRank.put("Dark Embrace", 8);
        cardRank.put("Evolve", 6);
        cardRank.put("Feel No Pain", 7);
        cardRank.put("Fire Breathing", 2);
        cardRank.put("Inflame", 2);
        cardRank.put("Metallicize", 1);
        cardRank.put("Rupture", 5);
        cardRank.put("Barricade", 8);
        cardRank.put("Berserk", 5);
        cardRank.put("Brutality", 2);
        cardRank.put("Corruption", 7);
        cardRank.put("Demon Form", 3);
        cardRank.put("Juggernaut", 1);

        // SILENT POWER.
        cardRank.put("Accuracy", 5);
        cardRank.put("Caltrops", 1);
        cardRank.put("Footwork", 5);
        cardRank.put("Infinite Blades", 1);
        cardRank.put("Noxious Fumes", 2);
        cardRank.put("Well Laid Plans", 6);
        cardRank.put("A Thousand Cuts", 2);
        cardRank.put("After Image", 7);
        cardRank.put("Envenom", 2);
        cardRank.put("Tools of the Trade", 6);
        cardRank.put("Wraith Form v2", 8);

        // DEFECT POWER.
        cardRank.put("Capacitor", 6);
        cardRank.put("Defragment", 5);
        cardRank.put("Heatsinks", 5);
        cardRank.put("Hello World", 1);
        cardRank.put("Loop", 3);
        cardRank.put("Self Repair", 4);
        cardRank.put("Static Discharge", 1);
        cardRank.put("Storm", 3);
        cardRank.put("Biased Cognition", 7);
        cardRank.put("Buffer", 4);
        cardRank.put("Creative AI", 6);
        cardRank.put("Echo Form", 8);
        cardRank.put("Electrodynamics", 7);
        cardRank.put("Machine Learning", 2);

        // WATCHER POWER.
        cardRank.put("Adaptation", 8);
        cardRank.put("BattleHymn", 1);
        cardRank.put("Fasting2", 6);
        cardRank.put("LikeWater", 1);
        cardRank.put("MentalFortress", 6);
        cardRank.put("Nirvana", 1);
        cardRank.put("Study", 4);
        cardRank.put("Wireheading", 2);
        cardRank.put("DevaForm", 6);
        cardRank.put("Devotion", 2);
        cardRank.put("Establishment", 5);
        cardRank.put("MasterReality", 2);

        // COLORLESS POWER.
        cardRank.put("Magnetism", 2);
        cardRank.put("Mayhem", 2);
        cardRank.put("Panache", 4);
        cardRank.put("Sadistic Nature", 4);
        cardRank.put("Omega", 3);
    }

    private enum CurrentScreen {
        INTRO,
        CHOICE,
        RESULT;

        CurrentScreen() {
        }
    }

    static {
        setCardRank();
    }
}
