package net.iafenvoy.loadingmgr.mixins;

import net.iafenvoy.loadingmgr.progress.LoadingStats;
import net.minecraft.client.texture.Sprite.Info;
import net.minecraft.client.texture.TextureStitcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureStitcher.class)
public class TextureStitcherMixin {
    private int totalHolders = 0, nowHolders = 0;

    @Inject(method = "add", at = @At("RETURN"))
    private void add(Info info, CallbackInfo info_1) {
        totalHolders++;
    }

    @Inject(method = "stitch", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;next()Ljava/lang/Object;"))
    public void countExtractedSprites(CallbackInfo info) {
        nowHolders++;
        LoadingStats.setValue(nowHolders, totalHolders);
    }
}
