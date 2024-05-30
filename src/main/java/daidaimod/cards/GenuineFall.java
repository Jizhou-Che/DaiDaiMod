package daidaimod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import daidaimod.actions.GenuineFallRewardAction;

public class GenuineFall extends CustomCard {
    public static final String ID = "daidaimod:GenuineFall";
    private static final CardStrings cardStrings;
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION;
    public boolean canFall = true;
    public AbstractCard bestCard = null;
    public int bestCardRank = 0;

    public GenuineFall() {
        super(ID, NAME, "daidaimod/images/cards/GenuineFall.png", -2, DESCRIPTION, CardType.CURSE, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);

        this.setBannerTexture("daidaimod/images/cardui/CardBannerCurseSmall.png", "daidaimod/images/cardui/CardBannerCurseLarge.png");
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    public void upgrade() {
    }

    public void onChoseThisOption() {
        if (this.canFall) {
            this.addToBot(new ExhaustSpecificCardAction(this.bestCard, AbstractDungeon.player.hand));
            this.addToBot(new GenuineFallRewardAction(bestCardRank >= 5));
        } else {
            this.addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, 5), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
        EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    }
}
