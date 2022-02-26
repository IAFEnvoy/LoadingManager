package net.iafenvoy.loadingmgr.mixins;

import java.util.Set;
import java.util.stream.Stream;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.iafenvoy.loadingmgr.progress.LoadingStats;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

@Mixin(SpriteAtlasTexture.class)
public class SpriteAtlasTextureMixin {
  private int extractedSprites = 0, allToExtract = 0;;

  @Inject(method = "stitch", at = @At("HEAD"))
  public void showStitchPreparing(ResourceManager resourceManager, Stream<Identifier> idStream, Profiler profiler,
      int mipmapLevel, CallbackInfoReturnable<SpriteAtlasTexture.Data> cir) {
    LoadingStats.now = LoadingStats.PreparingStitchingTextures;
  }

  @Inject(method = "stitch", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args = {
      "ldc=extracting_frames" }))
  public void showStitchExtracting(ResourceManager resourceManager, Stream<Identifier> idStream, Profiler profiler,
      int mipmapLevel, CallbackInfoReturnable<SpriteAtlasTexture.Data> cir) {
    LoadingStats.now = LoadingStats.ExtractingTextures;
  }

  @Inject(method = "stitch", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args = {
      "ldc=stitching" }))
  public void showStitchStitching(ResourceManager resourceManager, Stream<Identifier> idStream, Profiler profiler,
      int mipmapLevel, CallbackInfoReturnable<SpriteAtlasTexture.Data> cir) {
    LoadingStats.now = LoadingStats.StitchingTextures;
  }

  @Inject(method = "stitch", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args = {
      "ldc=loading" }))
  public void showStitchLoading(ResourceManager resourceManager, Stream<Identifier> idStream, Profiler profiler,
      int mipmapLevel, CallbackInfoReturnable<SpriteAtlasTexture.Data> cir) {
    LoadingStats.now = LoadingStats.LoadingTextures;
  }

  @Inject(method = "stitch", at = @At("RETURN"))
  public void showStitchEnd(ResourceManager resourceManager, Stream<Identifier> idStream, Profiler profiler,
      int mipmapLevel, CallbackInfoReturnable<SpriteAtlasTexture.Data> cir) {
    // complete
  }

  @Inject(method = "loadSprites", at = @At("HEAD"))
  public void getSpritesToLoad(ResourceManager resourceManager, Set<Identifier> set,
      CallbackInfoReturnable<Boolean> ret) {
    allToExtract = set.size();
    LoadingStats.setValue(extractedSprites, allToExtract);
  }

  @Inject(method = "loadSprites", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;next()Ljava/lang/Object;"))
  public void countExtractedSprites(ResourceManager resourceManager, Set<Identifier> ids,
      CallbackInfoReturnable<Boolean> ret) {
    extractedSprites++;
    LoadingStats.setValue(extractedSprites, allToExtract);
  }
}
