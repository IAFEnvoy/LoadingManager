package net.iafenvoy.loadingmgr.mixins;

import net.iafenvoy.loadingmgr.TextRender;
import net.iafenvoy.loadingmgr.progress.LoadingStats;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.BackgroundHelper.ColorMixer;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.SplashScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author Indigo Amann
 */
@Mixin(SplashScreen.class)
public abstract class SplashMixin extends Overlay {

  @Shadow
  private MinecraftClient client;
  @Shadow
  private float progress;

  private void renderProgressBar(MatrixStack matrices, int x1, int y1, int x2, int y2, float opacity,
      float loadingProgress, String text) {
    if (loadingProgress > 1)
      loadingProgress = 1;
    if (loadingProgress < 0)
      loadingProgress = 0;

    int m = MathHelper.ceil((float) (x2 - x1 - 2) * loadingProgress);
    int o = ColorMixer.getArgb(Math.round(opacity * 255.0F), 255, 255, 255);

    if (TextRender.textRenderer == null)
      TextRender.init();
    TextRender.textRenderer.draw(matrices, text, x1, y1 - TextRender.textRenderer.fontHeight,
        ColorMixer.getArgb(Math.round(opacity * 255.0F), 0, 0, 0));

    fill(matrices, x1 + 1, y1, x2 - 1, y1 + 1, o);
    fill(matrices, x1 + 1, y2, x2 - 1, y2 - 1, o);
    fill(matrices, x1, y1, x1 + 1, y2, o);
    fill(matrices, x2, y1, x2 - 1, y2, o);
    fill(matrices, x1 + 2, y1 + 2, x1 + m, y2 - 2, o);
  }

  @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/SplashScreen;renderProgressBar(Lnet/minecraft/client/util/math/MatrixStack;IIIIF)V"))
  private void render(SplashScreen dis, MatrixStack matrices, int i, int j, int k, int l, float opacity) {
    int width = this.client.getWindow().getScaledWidth();
    int offset = (int) (Math.min((double) this.client.getWindow().getScaledWidth() * 0.75D,
        (double) this.client.getWindow().getScaledHeight()) * 0.5D);
    int t1 = (int) ((double) this.client.getWindow().getScaledHeight() * 0.7D);
    int t2 = (int) ((double) this.client.getWindow().getScaledHeight() * 0.8D);
    int t3 = (int) ((double) this.client.getWindow().getScaledHeight() * 0.9D);
    matrices.push();
    renderProgressBar(matrices, width / 2 - offset, t1 - 5, width / 2 + offset, t1 + 5, 1.0F, progress,
        "Total Progress");
    renderProgressBar(matrices, width / 2 - offset, t2 - 5, width / 2 + offset, t2 + 5, 1.0F,
        LoadingStats.now.getValue1(), LoadingStats.now.getText1());
    renderProgressBar(matrices, width / 2 - offset, t3 - 5, width / 2 + offset, t3 + 5, 1.0F,
        LoadingStats.getValue(), LoadingStats.now.getText2());
    matrices.pop();
  }
}
