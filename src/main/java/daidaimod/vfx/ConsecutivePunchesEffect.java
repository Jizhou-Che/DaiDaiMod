package daidaimod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class ConsecutivePunchesEffect extends AbstractGameEffect {
    public static final String EFFECT_ID = "daidaimod:ConsecutivePunches";
    private static TextureAtlas.AtlasRegion img1;
    private static TextureAtlas.AtlasRegion img2;
    private final float sX;
    private final float sY;
    private final float tX;
    private final float tY;
    private int count;
    private boolean alternateImg = true;
    private float timer = 0.0F;

    public ConsecutivePunchesEffect(float sX, float sY, float tX, float tY, int count) {
        if (img1 == null) {
            img1 = ImageMaster.vfxAtlas.findRegion("combat/" + EFFECT_ID + "1");
        }
        if (img2 == null) {
            img2 = ImageMaster.vfxAtlas.findRegion("combat/" + EFFECT_ID + "2");
        }

        this.sX = sX - 20.0F * Settings.scale;
        this.sY = sY + 80.0F * Settings.scale;
        this.tX = tX;
        this.tY = tY;
        this.count = count;
    }

    public void update() {
        this.timer -= Gdx.graphics.getDeltaTime();
        if (this.timer < 0.0F) {
            this.timer += MathUtils.random(0.05F, 0.15F);
            if (alternateImg) {
                AbstractDungeon.effectsQueue.add(new ConsecutivePunchesParticleEffect(this.sX, this.sY, this.tX, this.tY, img1));
            } else {
                AbstractDungeon.effectsQueue.add(new ConsecutivePunchesParticleEffect(this.sX, this.sY, this.tX, this.tY, img2));
            }
            alternateImg = !alternateImg;
            --this.count;
            if (this.count == 0) {
                this.isDone = true;
            }
        }
    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}
