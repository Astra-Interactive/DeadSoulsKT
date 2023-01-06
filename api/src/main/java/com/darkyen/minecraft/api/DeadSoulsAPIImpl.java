package com.darkyen.minecraft.api;

import com.darkyen.minecraft.database.SoulDatabase;
import com.darkyen.minecraft.models.ISoul;
import com.darkyen.minecraft.models.Soul;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DeadSoulsAPIImpl implements DeadSoulsAPI {
    @NotNull
    public static final ItemStack[] NO_ITEM_STACKS = new ItemStack[0];
    @Nullable
    private SoulDatabase soulDatabase;
    long soulFreeAfterMs;
    boolean refreshNearbySoulCache;

    public DeadSoulsAPIImpl(@Nullable SoulDatabase soulDatabase, long soulFreeAfterMs, boolean refreshNearbySoulCache){
        this.soulDatabase = soulDatabase;
        this.soulFreeAfterMs = soulFreeAfterMs;
        this.refreshNearbySoulCache = refreshNearbySoulCache;
    }

    @Override
    public void getSouls(Collection<ISoul> out) {
        out.clear();
        final SoulDatabase soulDatabase = this.soulDatabase;
        if (soulDatabase == null) {
            return;
        }
        final List<@Nullable Soul> souls = soulDatabase.getSoulsById();
        synchronized (souls) {
            final int size = souls.size();
            //noinspection ForLoopReplaceableByForEach
            for (int i = 0; i < size; i++) {
                final Soul soul = souls.get(i);
                if (soul == null) continue;
                out.add(soul);
            }
        }
    }

    @Override
    public void getSoulsByPlayer(@NotNull Collection<@NotNull ISoul> out, @Nullable UUID playerUUID) {
        out.clear();
        final SoulDatabase soulDatabase = this.soulDatabase;
        if (soulDatabase == null) {
            return;
        }
        final List<@Nullable Soul> souls = soulDatabase.getSoulsById();
        synchronized (souls) {
            final int size = souls.size();
            //noinspection ForLoopReplaceableByForEach
            for (int i = 0; i < size; i++) {
                final Soul soul = souls.get(i);
                if (soul == null || !Objects.equals(playerUUID, soul.getOwner())) continue;
                out.add(soul);
            }
        }
    }

    @Override
    public void getSoulsByWorld(@NotNull Collection<@NotNull ISoul> out, @NotNull UUID worldUUID) {
        out.clear();
        final SoulDatabase soulDatabase = this.soulDatabase;
        if (soulDatabase == null) {
            return;
        }
        final List<@Nullable Soul> souls = soulDatabase.getSoulsById();
        synchronized (souls) {
            final int size = souls.size();
            //noinspection ForLoopReplaceableByForEach
            for (int i = 0; i < size; i++) {
                final Soul soul = souls.get(i);
                if (soul == null || !soul.getWorld().equals(worldUUID)) continue;
                out.add(soul);
            }
        }
    }

    @Override
    public void getSoulsByPlayerAndWorld(@NotNull Collection<@NotNull ISoul> out, @Nullable UUID playerUUID, @NotNull UUID worldUUID) {
        out.clear();
        final SoulDatabase soulDatabase = this.soulDatabase;
        if (soulDatabase == null) {
            return;
        }
        final List<@Nullable Soul> souls = soulDatabase.getSoulsById();
        synchronized (souls) {
            final int size = souls.size();
            //noinspection ForLoopReplaceableByForEach
            for (int i = 0; i < size; i++) {
                final Soul soul = souls.get(i);
                if (soul == null || !soul.getWorld().equals(worldUUID) || !Objects.equals(playerUUID, soul.getOwner()))
                    continue;
                out.add(soul);
            }
        }
    }

    @Override
    public void getSoulsByLocation(@NotNull Collection<@NotNull ISoul> out, @NotNull UUID worldUUID, int x, int z, int radius) {
        out.clear();
        final SoulDatabase soulDatabase = this.soulDatabase;
        if (soulDatabase == null) {
            return;
        }
        //noinspection unchecked
        soulDatabase.findSouls(worldUUID, x, z, radius, (Collection<Soul>) (Object) out);
    }

    @Override
    public void freeSoul(@NotNull ISoul soul) {
        if (((Soul) soul).freeSoul(System.currentTimeMillis(), soulFreeAfterMs)) {
            final SoulDatabase soulDatabase = this.soulDatabase;
            if (soulDatabase == null) {
                return;
            }
            soulDatabase.markDirty();
        }
    }

    @Override
    public void setSoulItems(@NotNull ISoul soul, @NotNull ItemStack @NotNull [] items) {
        ((Soul) soul).setItems(items);
        final SoulDatabase soulDatabase = this.soulDatabase;
        if (soulDatabase == null) {
            return;
        }
        soulDatabase.markDirty();
    }

    @Override
    public void setSoulExperiencePoints(@NotNull ISoul soul, int xp) {
        ((Soul) soul).setExperiencePoints(xp);
        final SoulDatabase soulDatabase = this.soulDatabase;
        if (soulDatabase == null) {
            return;
        }
        soulDatabase.markDirty();
    }

    @Override
    public void removeSoul(@NotNull ISoul soul) {
        final SoulDatabase soulDatabase = this.soulDatabase;
        if (soulDatabase == null) {
            return;
        }
        soulDatabase.removeSoul((Soul) soul);
        refreshNearbySoulCache = true;
    }

    @Override
    public boolean soulExists(@NotNull ISoul soul) {
        final Soul ssoul = (Soul) soul;
        if (ssoul.getId() < 0) {
            return false;
        }
        final SoulDatabase soulDatabase = this.soulDatabase;
        if (soulDatabase == null) {
            return false;
        }

        final ArrayList<@Nullable Soul> souls = soulDatabase.getSoulsById();
        synchronized (souls) {
            return ssoul.getId() < souls.size() && souls.get(ssoul.getId()) == soul;
        }
    }

    @Override
    public @NotNull ISoul createSoul(@Nullable UUID owner, @NotNull UUID world, double x, double y, double z, @Nullable ItemStack[] contents, int xp) {
        ItemStack[] nnContents = contents == null ? NO_ITEM_STACKS : contents;
        final SoulDatabase soulDatabase = this.soulDatabase;
        if (soulDatabase == null) {
            // Sad, but better than returning null which would probably cause crash. This situation can be tested through soulExists.
            return new Soul(owner, world, x, y, z, System.currentTimeMillis(), nnContents, xp);
        }
        refreshNearbySoulCache = true;
        return soulDatabase.addSoul(owner, world, x, y, z, nnContents, xp);
    }
}
