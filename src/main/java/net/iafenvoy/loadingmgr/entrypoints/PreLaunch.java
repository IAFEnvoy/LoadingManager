package net.iafenvoy.loadingmgr.entrypoints;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.metadata.EntrypointMetadata;

public class PreLaunch implements PreLaunchEntrypoint {
  @Override
  public void onPreLaunch() {
    System.out.println("Start PreLaunch");
    ArrayList<ModContainer> newArrayList = Lists.newArrayList(FabricLoader.getInstance().getAllMods());
    for (int i = 0; i < newArrayList.size(); i++) {
      net.fabricmc.loader.api.ModContainer modContainer = newArrayList.get(i);
      ModMetadata metadata = modContainer.getMetadata();
      if (modContainer instanceof net.fabricmc.loader.ModContainer) {
        net.fabricmc.loader.ModContainer mod = (net.fabricmc.loader.ModContainer) modContainer;
        boolean hasMain = !mod.getInfo().getEntrypoints("main").isEmpty();
        boolean hasClient = !mod.getInfo().getEntrypoints("client").isEmpty();
        if (!hasClient && !hasMain)
          continue;
        System.out.print(metadata.getName() + " (" + metadata.getId() + ")\n");
        // if (hasMain) System.out.print("|-Main Entrypoints\n");
        for (EntrypointMetadata entrypoint : mod.getInfo().getEntrypoints("main")) {
          System.out.print("|--" + entrypoint.getValue() + "\n");
        }
        // if (hasClient) System.out.print("|-Client Entrypoints\n");
        for (EntrypointMetadata entrypoint : mod.getInfo().getEntrypoints("client")) {
          System.out.print("|--" + entrypoint.getValue() + "\n");
        }
      }
    }
    System.out.println("Done.");
  }
}
