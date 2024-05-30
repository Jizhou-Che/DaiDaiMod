package daidaimod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class FakeFall extends CustomCard {
    public static final String ID = "daidaimod:FakeFall";
    private static final CardStrings cardStrings;
    public static final String NAME;
    public static final String DESCRIPTION;

    public FakeFall() {
        super(ID, NAME, "daidaimod/images/cards/FakeFall.png", -2, DESCRIPTION, CardType.CURSE, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);

        this.setBannerTexture("daidaimod/images/cardui/CardBannerCurseSmall.png", "daidaimod/images/cardui/CardBannerCurseLarge.png");
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    public void upgrade() {
    }

    public void onChoseThisOption() {
        this.addToBot(new ExhaustAction(1, false));
        this.addToBot(new MakeTempCardInHandAction(getRandomTrashyCard()));
        this.addToBot(new MakeTempCardInHandAction(getRandomTrashyCard()));
    }

    private AbstractCard getRandomTrashyCard() {
        ArrayList<String> trashyCardPool = new ArrayList<>();

        trashyCardPool.add("Anger");
        trashyCardPool.add("Clash");
        trashyCardPool.add("Cleave");
        trashyCardPool.add("Clothesline");
        trashyCardPool.add("Iron Wave");
        trashyCardPool.add("Perfected Strike");
        trashyCardPool.add("Thunderclap");
        trashyCardPool.add("Twin Strike");
        trashyCardPool.add("Wild Strike");
        trashyCardPool.add("Searing Blow");
        trashyCardPool.add("Whirlwind");
        trashyCardPool.add("Bane");
        trashyCardPool.add("Dagger Spray");
        trashyCardPool.add("Flying Knee");
        trashyCardPool.add("Poisoned Stab");
        trashyCardPool.add("Sucker Punch");
        trashyCardPool.add("All Out Attack");
        trashyCardPool.add("Skewer");
        trashyCardPool.add("Corpse Explosion");
        trashyCardPool.add("Melter");
        trashyCardPool.add("BowlingBash");
        trashyCardPool.add("EmptyFist");
        trashyCardPool.add("FlyingSleeves");
        trashyCardPool.add("JustLucky");
        trashyCardPool.add("SashWhip");
        trashyCardPool.add("CarveReality");
        trashyCardPool.add("Conclude");
        trashyCardPool.add("WindmillStrike");

        if (AbstractDungeon.player.maxOrbs == 0) {
            trashyCardPool.add("Barrage");
            trashyCardPool.add("Compile Driver");
            trashyCardPool.add("Blizzard");
            trashyCardPool.add("Lockon");
            trashyCardPool.add("Thunder Strike");
            trashyCardPool.add("Redo");
            trashyCardPool.add("Consume");
            trashyCardPool.add("Multi-Cast");
        }

        return CardLibrary.getCard(trashyCardPool.get(AbstractDungeon.cardRandomRng.random(trashyCardPool.size() - 1))).makeCopy();
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
