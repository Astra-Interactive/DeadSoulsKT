package com.darkyen.minecraft.api;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

/**
 * The Dead Souls plugin implements this interface and it is recommended to only interact with it through it.
 * Methods in this interface are considered stable and will not change across versions.
 *
 * Most methods are designed to be efficient and allocation free.
 *
 * Unless specified otherwise, the methods are not thread safe and MUST be called
 * from the main thread (the one from which standard Spigot callbacks are called).
 *
 * Reference of this interface when the plugin is not present on the server will result
 * in a {@link NoClassDefFoundError}, so be prepared to handle that.
 *
 * @since 1.6
 */
@SuppressWarnings("unused")
public interface DeadSoulsAPI {

	/**
	 * Entry point for this API.
	 * @return instance of the plugin
	 * @throws NoClassDefFoundError when the plugin is not installed (actually, Java throws that when it can't load this interface)
	 * @throws IllegalStateException when DeadSouls plugin exists, but is not loaded
	 */
	@NotNull
	public static DeadSoulsAPI instance() throws NoClassDefFoundError, IllegalStateException {
		final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("DeadSouls");
		if (plugin == null) {
			throw new IllegalStateException("DeadSouls plugin classes are loaded, but the plugin is not");
		}
		return (DeadSoulsAPI) plugin;
	}

	/** Get all souls which exist.
	 * @param out a collection into which all souls will be added (after being cleared).
	 * This method does zero allocations and is thread safe. */
	public void getSouls(@NotNull Collection<@NotNull ISoul> out);

	/** Same as {@link #getSouls(Collection)}, but only return souls which currently belong to a certain player.
	 * @param playerUUID null means find all souls which belong to on one. */
	public void getSoulsByPlayer(@NotNull Collection<@NotNull ISoul> out, @Nullable UUID playerUUID);

	/** Same as {@link #getSouls(Collection)}, but only return souls which belong to a certain world. */
	public void getSoulsByWorld(@NotNull Collection<@NotNull ISoul> out, @NotNull UUID worldUUID);

	/** Same as {@link #getSouls(Collection)}, but only return souls which belong to a certain world.
	 * @param playerUUID null means find all souls which belong to on one. */
	public void getSoulsByPlayerAndWorld(@NotNull Collection<@NotNull ISoul> out, @Nullable UUID playerUUID, @NotNull UUID worldUUID);

	/** Similar to {@link #getSouls(Collection)}, but only return souls which belong to a certain world and are located inside
	 * a cylinder of infinite height, centered at (x, z) and having the given radius.
	 * NOTE: For efficiency, returned souls might actually be outside of the radius.
	 * If you care about the exact distance, compute it yourself.
	 * NOTE: This method is NOT thread safe. */
	public void getSoulsByLocation(@NotNull Collection<@NotNull ISoul> out, @NotNull UUID worldUUID, int x, int z, int radius);

	/** Free the soul (remove its owner), if not free yet. */
	public void freeSoul(@NotNull ISoul soul);

	/** Set the items of the soul, replaces any old ones.
	 * The passed in array is used as is and therefore MUST NOT be further modified, including any item modifications. */
	public void setSoulItems(@NotNull ISoul soul, @NotNull ItemStack @NotNull[] items);

	/** Set the amount of experience point stored, replacing the old one. */
	public void setSoulExperiencePoints(@NotNull ISoul soul, int xp);

	/** Remove the soul from the world. */
	public void removeSoul(@NotNull ISoul soul);

	/** Return whether the soul still exists.
	 * Soul may disappear, for example, by player collecting it, it fading away or explicit {@link #removeSoul(ISoul)}.
	 * Note that all other methods still work correctly even if the soul does not exist anymore. */
	public boolean soulExists(@NotNull ISoul soul);

	/** Create a new soul and add it into the world. Parameters correspond to the getters of {@link ISoul}.
	 * @param contents similarly to {@link #setSoulItems(ISoul, ItemStack[])}, DO NOT MODIFY the contents of the array after it is passed in */
	public @NotNull ISoul createSoul(@Nullable UUID owner, @NotNull UUID world, double x, double y, double z, @Nullable ItemStack[] contents, int xp);



}
