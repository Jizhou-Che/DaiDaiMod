package daidaimod.cards;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import daidaimod.monsters.HeartOfDai;
import daidaimod.vfx.HeavyPunchEffect;

import java.util.Objects;

public class PunchDai extends CustomCard {
    public static final String ID = "daidaimod:PunchDai";
    private static final CardStrings cardStrings;
    public static final String NAME;
    public static final String DESCRIPTION;

    public PunchDai() {
        super(ID, NAME, "daidaimod/images/cards/PunchDai.png", 1, DESCRIPTION, CardType.ATTACK, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ENEMY);

        this.setBannerTexture("daidaimod/images/cardui/CardBannerRareSmall.png", "daidaimod/images/cardui/CardBannerRareLarge.png");

        this.exhaust = true;
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
        if (Objects.equals(abstractMonster.id, HeartOfDai.ID) && abstractMonster.intent != AbstractMonster.Intent.STUN) {
            this.addToBot(new VFXAction(new HeavyPunchEffect(abstractMonster.hb.cX, abstractMonster.hb.cY, Color.GOLD), 0.5F));
            this.addToBot(new SFXAction("BLUNT_HEAVY"));

            abstractMonster.setMove((byte) 5, AbstractMonster.Intent.STUN);
            abstractMonster.createIntent();
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
