package daidaimod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import java.util.ArrayList;

public class GenuineFallRewardAction extends AbstractGameAction {
    private boolean retrieveCard = false;
    private final boolean upgradeCard;

    public GenuineFallRewardAction(boolean upgradeCard) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.upgradeCard = upgradeCard;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.cardRewardScreen.customCombatOpen(this.generateCardChoices(), CardRewardScreen.TEXT[1], true);
        } else {
            if (!this.retrieveCard) {
                if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
                    AbstractCard chosenCard = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
                    if (upgradeCard) {
                        chosenCard.upgrade();
                    }
                    chosenCard.current_x = -1000.0F * Settings.xScale;
                    if (AbstractDungeon.player.hand.size() < 10) {
                        AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(chosenCard, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                    } else {
                        AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(chosenCard, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                    }
                    AbstractDungeon.cardRewardScreen.discoveryCard = null;
                }
                this.retrieveCard = true;
            }
        }
        this.tickDuration();
    }

    private ArrayList<AbstractCard> generateCardChoices() {
        ArrayList<AbstractCard> choices = new ArrayList<>();

        while (choices.size() != 5) {
            AbstractCard card = getRandomPowerfulCard();

            boolean duplicate = false;
            for (AbstractCard c : choices) {
                if (c.cardID.equals(card.cardID)) {
                    duplicate = true;
                    break;
                }
            }

            if (!duplicate) {
                choices.add(card.makeCopy());
            }
        }

        return choices;
    }

    private AbstractCard getRandomPowerfulCard() {
        ArrayList<String> powerfulCardPool = new ArrayList<>();

        powerfulCardPool.add("Reaper");
        powerfulCardPool.add("Grand Finale");
        powerfulCardPool.add("All For One");
        powerfulCardPool.add("Core Surge");
        powerfulCardPool.add("TalkToTheHand");
        powerfulCardPool.add("Tantrum");
        powerfulCardPool.add("Battle Trance");
        powerfulCardPool.add("Bloodletting");
        powerfulCardPool.add("Burning Pact");
        powerfulCardPool.add("Disarm");
        powerfulCardPool.add("Second Wind");
        powerfulCardPool.add("Shockwave");
        powerfulCardPool.add("Exhume");
        powerfulCardPool.add("Impervious");
        powerfulCardPool.add("Limit Break");
        powerfulCardPool.add("Offering");
        powerfulCardPool.add("Acrobatics");
        powerfulCardPool.add("PiercingWail");
        powerfulCardPool.add("Calculated Gamble");
        powerfulCardPool.add("Terror");
        powerfulCardPool.add("Adrenaline");
        powerfulCardPool.add("Bullet Time");
        powerfulCardPool.add("Burst");
        powerfulCardPool.add("Malaise");
        powerfulCardPool.add("Night Terror");
        powerfulCardPool.add("Phantasmal Killer");
        powerfulCardPool.add("Venomology");
        powerfulCardPool.add("Turbo");
        powerfulCardPool.add("Recycle");
        powerfulCardPool.add("Reprogram");
        powerfulCardPool.add("Skim");
        powerfulCardPool.add("Undo");
        powerfulCardPool.add("Amplify");
        powerfulCardPool.add("Reboot");
        powerfulCardPool.add("Seek");
        powerfulCardPool.add("Indignation");
        powerfulCardPool.add("Vengeance");
        powerfulCardPool.add("Blasphemy");
        powerfulCardPool.add("Omniscience");
        powerfulCardPool.add("Scrawl");
        powerfulCardPool.add("Vault");
        powerfulCardPool.add("Dark Shackles");
        powerfulCardPool.add("Enlightenment");
        powerfulCardPool.add("Impatience");
        powerfulCardPool.add("Madness");
        powerfulCardPool.add("Panacea");
        powerfulCardPool.add("Purity");
        powerfulCardPool.add("Apotheosis");
        powerfulCardPool.add("Master of Strategy");
        powerfulCardPool.add("Ghostly");
        powerfulCardPool.add("Dark Embrace");
        powerfulCardPool.add("Evolve");
        powerfulCardPool.add("Feel No Pain");
        powerfulCardPool.add("Barricade");
        powerfulCardPool.add("Corruption");
        powerfulCardPool.add("Demon Form");
        powerfulCardPool.add("Well Laid Plans");
        powerfulCardPool.add("After Image");
        powerfulCardPool.add("Wraith Form v2");
        powerfulCardPool.add("Buffer");
        powerfulCardPool.add("Creative AI");
        powerfulCardPool.add("Echo Form");
        powerfulCardPool.add("Adaptation");
        powerfulCardPool.add("Fasting2");
        powerfulCardPool.add("DevaForm");
        powerfulCardPool.add("Establishment");
        powerfulCardPool.add("MasterReality");
        powerfulCardPool.add("Omega");
        powerfulCardPool.add("daidaimod:PunchDai");

        if (AbstractDungeon.player.maxOrbs > 0) {
            powerfulCardPool.add("Meteor Strike");
            powerfulCardPool.add("Consume");
            powerfulCardPool.add("Fission");
            powerfulCardPool.add("Capacitor");
            powerfulCardPool.add("Biased Cognition");
        }

        return CardLibrary.getCard(powerfulCardPool.get(AbstractDungeon.cardRandomRng.random(powerfulCardPool.size() - 1))).makeCopy();
    }
}
