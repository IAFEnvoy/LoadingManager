package net.iafenvoy.loadingmgr.mixins;

import com.ibm.icu.impl.Pair;
import net.iafenvoy.loadingmgr.entrypoints.NumberCounter;
import net.iafenvoy.loadingmgr.progress.LoadingStats;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.SpriteAtlasTexture.Data;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ModelLoader.class)
public class ModelManagerMixin {
    @Final
    @Shadow
    private Map<Identifier, UnbakedModel> modelsToBake;
    @Final
    @Shadow
    private Map<Identifier, Pair<SpriteAtlasTexture, Data>> spriteAtlasData;
    @Final
    @Shadow
    private Map<Identifier, BakedModel> bakedModels;

    private int itemCnt = 0, blockCnt = 0, atlasDone = 0;

    @Inject(method = "addModel", at = @At("RETURN"))
    private void getAddModelProgress(ModelIdentifier modelId, CallbackInfo info) {
        if (modelId.getVariant().equals("inventory")) {// item
            itemCnt++;
            LoadingStats.now = LoadingStats.AddItems;
            LoadingStats.setValue(itemCnt, NumberCounter.itemCnt);
        } else {// block
            blockCnt++;
            LoadingStats.now = LoadingStats.AddBlocks;
            LoadingStats.setValue(blockCnt, NumberCounter.blockCnt);
        }
    }

    @Inject(method = "upload", at = @At("HEAD"))
    private void toAtlas(TextureManager textureManager, Profiler profiler,
                         CallbackInfoReturnable<BakedModel> ci) {
        LoadingStats.now = LoadingStats.Atlas;
        LoadingStats.setValue(atlasDone, spriteAtlasData.size());
    }

    @Inject(method = "upload", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;next()Ljava/lang/Object;"))
    public void countExtractedSprites(TextureManager textureManager, Profiler profiler,
                                      CallbackInfoReturnable<Boolean> ret) {
        atlasDone++;
        LoadingStats.setValue(atlasDone, spriteAtlasData.size());
    }

    @Inject(method = "upload", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args = {
            "ldc=baking"}))
    public void startBaking(TextureManager textureManager, Profiler profiler,
                            CallbackInfoReturnable<SpriteAtlasTexture.Data> cir) {
        LoadingStats.now = LoadingStats.Baking;
        LoadingStats.setValue(bakedModels.size(), modelsToBake.keySet().size());
    }

    @Inject(method = "bake", at = @At("HEAD"))
    private void bake(Identifier id, ModelBakeSettings settings, CallbackInfoReturnable<BakedModel> ret) {
        LoadingStats.setValue(bakedModels.size(), modelsToBake.size());
    }
}
