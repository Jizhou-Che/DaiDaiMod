package daidaimod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import daidaimod.util.TextureLoader;

public class BlessingOfDai extends CustomRelic {
    public static final String ID = "daidaimod:BlessingOfDai";
    private static final Texture IMG = TextureLoader.getTexture("daidaimod/images/relics/BlessingOfDai.png");
    private static final Texture OUTLINE = TextureLoader.getTexture("daidaimod/images/relics/BlessingOfDaiOutline.png");
    private static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);

    public BlessingOfDai() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
        this.energyBased = true;
    }

    @Override
    public String getUpdatedDescription() {
        return relicStrings.DESCRIPTIONS[0];
    }

    @Override
    public void onEquip() {
        ++AbstractDungeon.player.energy.energyMaster;
    }

    @Override
    public void onUnequip() {
        --AbstractDungeon.player.energy.energyMaster;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BlessingOfDai();
    }
}
