package daidaimod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineParticleEffect;
import com.megacrit.cardcrawl.vfx.combat.DamageImpactCurvyEffect;

public class HeavyPunchEffect extends AbstractGameEffect {
    public static final String EFFECT_ID = "daidaimod:HeavyPunch";
    private static TextureAtlas.AtlasRegion img;
    private final float x;
    private float y;
    private final float targetY;
    private boolean impactHook = false;
    private boolean soundPlayed = false;

    public HeavyPunchEffect(float x, float y, Color color) {
        if (img == null) {
            img = ImageMaster.vfxAtlas.findRegion("combat/" + EFFECT_ID);
        }

        this.scale = Settings.scale;
        this.x = x - (float) img.packedWidth / 2.0F;
        this.y = (float) Settings.HEIGHT - (float) img.packedHeight / 2.0F;
        this.duration = 1.0F;
        this.targetY = y - 180.0F * Settings.scale;
        this.rotation = MathUtils.random(-1.0F, 1.0F);
        this.color = color;
    }

    public void update() {
        this.y = Interpolation.fade.apply((float) Settings.HEIGHT, this.targetY, 1.0F - this.duration);
        this.scale += Gdx.graphics.getDeltaTime();
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        } else if (this.duration < 0.2F) {
            if (!this.impactHook) {
                this.impactHook = true;
                AbstractDungeon.effectsQueue.add(new BorderFlashEffect(color));
                CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.MED, true);

                int i;
                for (i = 0; i < 5; ++i) {
                    AbstractDungeon.effectsQueue.add(new DamageImpactCurvyEffect(this.x + (float) img.packedWidth / 2.0F, this.y + (float) img.packedWidth / 2.0F));
                }

                for (i = 0; i < 30; ++i) {
                    AbstractDungeon.effectsQueue.add(new UpgradeShineParticleEffect(this.x + MathUtils.random(-100.0F, 100.0F) * Settings.scale + (float) img.packedWidth / 2.0F, this.y + MathUtils.random(-50.0F, 120.0F) * Settings.scale + (float) img.packedHeight / 2.0F));
                }
            }
            this.color.a = Interpolation.fade.apply(0.0F, 0.5F, 0.2F / this.duration);
        } else {
            if (!soundPlayed) {
                soundPlayed = true;
                CardCrawlGame.sound.playA("ATTACK_MAGIC_FAST_3", -0.4F);
            }
            this.color.a = Interpolation.pow2Out.apply(0.6F, 0.0F, this.duration);
        }
    }

    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        this.color.g = 1.0F;
        sb.setColor(this.color);
        sb.draw(img, this.x, this.y + 140.0F * Settings.scale, (float) img.packedWidth / 2.0F, (float) img.packedHeight / 2.0F, (float) img.packedWidth, (float) img.packedHeight * (this.duration + 0.2F) * 5.0F, this.scale * MathUtils.random(0.99F, 1.01F) * 0.7F, this.scale * MathUtils.random(0.99F, 1.01F) * 2.0F * (this.duration + 0.8F), this.rotation);
        this.color.g = 0.6F;
        sb.setColor(this.color);
        sb.draw(img, this.x, this.y + 40.0F * Settings.scale, (float) img.packedWidth / 2.0F, (float) img.packedHeight / 2.0F, (float) img.packedWidth, (float) img.packedHeight * (this.duration + 0.2F) * 5.0F, this.scale * MathUtils.random(0.99F, 1.01F) * 0.7F, this.scale * MathUtils.random(0.99F, 1.01F) * 1.3F * (this.duration + 0.8F), this.rotation);
        this.color.g = 0.2F;
        sb.setColor(this.color);
        sb.draw(img, this.x, this.y, (float) img.packedWidth / 2.0F, (float) img.packedHeight / 2.0F, (float) img.packedWidth, (float) img.packedHeight * (this.duration + 0.2F) * 5.0F, this.scale * MathUtils.random(0.99F, 1.01F), this.scale * MathUtils.random(0.99F, 1.01F) * (this.duration + 0.8F), this.rotation);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}
