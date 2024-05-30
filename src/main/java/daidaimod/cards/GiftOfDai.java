package daidaimod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import daidaimod.vfx.GiftOfDaiEffect;

public class GiftOfDai extends CustomCard {
    public static final String ID = "daidaimod:GiftOfDai";
    private static final CardStrings cardStrings;
    public static final String NAME;
    public static final String DESCRIPTION;

    public GiftOfDai() {
        super(ID, NAME, "daidaimod/images/cards/GiftOfDai.png", 1, DESCRIPTION, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);

        this.setBannerTexture("daidaimod/images/cardui/CardBannerRareSmall.png", "daidaimod/images/cardui/CardBannerRareLarge.png");

        this.exhaust = true;
        this.selfRetain = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(0);
        }
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        this.addToBot(new VFXAction(new GiftOfDaiEffect(), 0.15F));

        int gainEnergyAmount = AbstractDungeon.cardRandomRng.random(2);
        if (gainEnergyAmount != 0) {
            this.addToBot(new GainEnergyAction(gainEnergyAmount));
        }

        int drawCardAmount = AbstractDungeon.cardRandomRng.random(2);
        if (drawCardAmount != 0) {
            this.addToBot(new DrawCardAction(abstractPlayer, drawCardAmount));
        }

        int gainBlockAmount = AbstractDungeon.cardRandomRng.random(20);
        if (gainBlockAmount != 0) {
            this.addToBot(new GainBlockAction(abstractPlayer, abstractPlayer, gainBlockAmount));
        }

        int healAmount = AbstractDungeon.cardRandomRng.random(10);
        if (healAmount != 0) {
            this.addToBot(new HealAction(abstractPlayer, abstractPlayer, healAmount));
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
