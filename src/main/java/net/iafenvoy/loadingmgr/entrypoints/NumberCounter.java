package net.iafenvoy.loadingmgr.entrypoints;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.util.registry.Registry;

public class NumberCounter implements ModInitializer {
    public static int blockCnt = 0;
    public static int itemCnt = 0;

    @Override
    public void onInitialize() {
        for (Block block : Registry.BLOCK)
            blockCnt += block.getStateManager().getStates().size();
        itemCnt = Registry.ITEM.getIds().size();
    }
}
