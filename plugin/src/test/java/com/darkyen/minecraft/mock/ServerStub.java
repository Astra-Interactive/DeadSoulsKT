package com.darkyen.minecraft.mock;

import com.destroystokyo.paper.entity.ai.MobGoals;
import io.papermc.paper.datapack.DatapackManager;
import io.papermc.paper.math.Position;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Logger;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.BanList;
import org.bukkit.GameMode;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Registry;
import org.bukkit.Server;
import org.bukkit.StructureType;
import org.bukkit.Tag;
import org.bukkit.UnsafeValues;
import org.bukkit.Warning;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemCraftResult;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.Recipe;
import org.bukkit.loot.LootTable;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapView;
import org.bukkit.packs.DataPackManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.potion.PotionBrewer;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.structure.StructureManager;
import org.bukkit.util.CachedServerIcon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 */
public class ServerStub implements Server {

	@Override
	public @NotNull File getPluginsFolder() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull String getName() { return "TEST"; }

	@Override
	public @NotNull String getVersion() { return "TEST_VER"; }

	@Override
	public @NotNull String getBukkitVersion() { return "BUKKIT_TEST_VER"; }

	@Override
	public @NotNull String getMinecraftVersion() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull Collection<? extends Player> getOnlinePlayers() {
		throw new UnsupportedOperationException("Method not implemented");
//		return Collections.emptyList();
	}

	@Override
	public int getMaxPlayers() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public void setMaxPlayers(int maxPlayers) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public int getPort() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public int getViewDistance() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public int getSimulationDistance() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull String getIp() {
		return "0.0.0.0";
	}

	@Override
	public @NotNull String getWorldType() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public boolean getGenerateStructures() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public int getMaxWorldSize() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public boolean getAllowEnd() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public boolean getAllowNether() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull List<String> getInitialEnabledPacks() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull List<String> getInitialDisabledPacks() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull DataPackManager getDataPackManager() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull String getResourcePack() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull String getResourcePackHash() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull String getResourcePackPrompt() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public boolean isResourcePackRequired() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public boolean hasWhitelist() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public void setWhitelist(boolean value) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public boolean isWhitelistEnforced() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public void setWhitelistEnforced(boolean value) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull Set<OfflinePlayer> getWhitelistedPlayers() {
		throw new UnsupportedOperationException("Method not implemented");
//		return Collections.emptySet();
	}

	@Override
	public void reloadWhitelist() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public int broadcastMessage(@NotNull String message) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull String getUpdateFolder() {
		return "server-stub-update-folder";
	}

	@Override
	public @NotNull File getUpdateFolderFile() {
		throw new UnsupportedOperationException("Method not implemented");
//		return new File(getUpdateFolder());
	}

	@Override
	public long getConnectionThrottle() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public int getTicksPerAnimalSpawns() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public int getTicksPerMonsterSpawns() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public int getTicksPerWaterSpawns() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public int getTicksPerWaterAmbientSpawns() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public int getTicksPerWaterUndergroundCreatureSpawns() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public int getTicksPerAmbientSpawns() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public int getTicksPerSpawns(@NotNull SpawnCategory spawnCategory) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @Nullable Player getPlayer(@NotNull String name) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @Nullable Player getPlayerExact(@NotNull String name) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull List<Player> matchPlayer(@NotNull String name) {
		throw new UnsupportedOperationException("Method not implemented");
//		return Collections.emptyList();
	}

	@Override
	public @Nullable Player getPlayer(@NotNull UUID id) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @Nullable UUID getPlayerUniqueId(@NotNull String playerName) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull PluginManager getPluginManager() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull BukkitScheduler getScheduler() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull ServicesManager getServicesManager() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull List<World> getWorlds() {
		throw new UnsupportedOperationException("Method not implemented");
//		return Collections.emptyList();
	}

	@Override
	public boolean isTickingWorlds() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @Nullable World createWorld(@NotNull WorldCreator creator) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public boolean unloadWorld(@NotNull String name, boolean save) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public boolean unloadWorld(@NotNull World world, boolean save) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @Nullable World getWorld(@NotNull String name) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @Nullable World getWorld(@NotNull UUID uid) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @Nullable World getWorld(@NotNull NamespacedKey worldKey) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull WorldBorder createWorldBorder() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @Nullable MapView getMap(int id) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull MapView createMap(@NotNull World world) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull ItemStack createExplorerMap(@NotNull World world, @NotNull Location location, @NotNull StructureType structureType) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull ItemStack createExplorerMap(@NotNull World world, @NotNull Location location, @NotNull StructureType structureType, int radius, boolean findUnexplored) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @Nullable ItemStack createExplorerMap(@NotNull World world, @NotNull Location location, org.bukkit.generator.structure.@NotNull StructureType structureType, MapCursor.@NotNull Type mapIcon, int radius, boolean findUnexplored) {
		return null;
	}

	@Override
	public void reload() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public void reloadData() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public void updateResources() {

	}

	@Override
	public void updateRecipes() {

	}

	private final Logger logger = Logger.getLogger("ServerStub");

	@Override
	public @NotNull Logger getLogger() {
		return logger;
	}

	@Override
	public @Nullable PluginCommand getPluginCommand(@NotNull String name) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public void savePlayers() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public boolean dispatchCommand(@NotNull CommandSender sender, @NotNull String commandLine) throws CommandException {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public boolean addRecipe(@Nullable Recipe recipe) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public boolean addRecipe(@Nullable Recipe recipe, boolean resendRecipes) {
		return false;
	}

	@Override
	public @NotNull List<Recipe> getRecipesFor(@NotNull ItemStack result) {
		throw new UnsupportedOperationException("Method not implemented");
//		return Collections.emptyList();
	}

	@Override
	public @Nullable Recipe getRecipe(@NotNull NamespacedKey namespacedKey) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @Nullable Recipe getCraftingRecipe(@NotNull ItemStack[] craftingMatrix, @NotNull World world) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull ItemStack craftItem(@NotNull ItemStack[] craftingMatrix, @NotNull World world, @NotNull Player player) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull ItemStack craftItem(@NotNull ItemStack[] craftingMatrix, @NotNull World world) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull ItemCraftResult craftItemResult(@NotNull ItemStack[] craftingMatrix, @NotNull World world, @NotNull Player player) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull ItemCraftResult craftItemResult(@NotNull ItemStack[] craftingMatrix, @NotNull World world) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull Iterator<Recipe> recipeIterator() {
		throw new UnsupportedOperationException("Method not implemented");
//		return Collections.emptyIterator();
	}

	@Override
	public void clearRecipes() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public void resetRecipes() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public boolean removeRecipe(@NotNull NamespacedKey namespacedKey) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public boolean removeRecipe(@NotNull NamespacedKey key, boolean resendRecipes) {
		return false;
	}

	@Override
	public @NotNull Map<String, String[]> getCommandAliases() {
		throw new UnsupportedOperationException("Method not implemented");
//		return Collections.emptyMap();
	}

	@Override
	public int getSpawnRadius() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public void setSpawnRadius(int value) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public boolean shouldSendChatPreviews() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public boolean isEnforcingSecureProfiles() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public boolean getHideOnlinePlayers() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public boolean getOnlineMode() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public boolean getAllowFlight() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public boolean isHardcore() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public void shutdown() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public int broadcast(@NotNull String message, @NotNull String permission) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public int broadcast(@NotNull Component message) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public int broadcast(@NotNull Component message, @NotNull String permission) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull OfflinePlayer getOfflinePlayer(@NotNull String name) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @Nullable OfflinePlayer getOfflinePlayerIfCached(@NotNull String name) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull OfflinePlayer getOfflinePlayer(@NotNull UUID id) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull PlayerProfile createPlayerProfile(@Nullable UUID uniqueId, @Nullable String name) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull PlayerProfile createPlayerProfile(@NotNull UUID uniqueId) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull PlayerProfile createPlayerProfile(@NotNull String name) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull Set<String> getIPBans() {
		throw new UnsupportedOperationException("Method not implemented");
//		return Collections.emptySet();
	}

	@Override
	public void banIP(@NotNull String address) {

		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public void unbanIP(@NotNull String address) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public void banIP(@NotNull InetAddress address) {

	}

	@Override
	public void unbanIP(@NotNull InetAddress address) {

	}

	@Override
	public @NotNull Set<OfflinePlayer> getBannedPlayers() {
		throw new UnsupportedOperationException("Method not implemented");
//		return Collections.emptySet();
	}

	@Override
	public @NotNull BanList getBanList(BanList.@NotNull Type type) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull Set<OfflinePlayer> getOperators() {
		throw new UnsupportedOperationException("Method not implemented");
//		return Collections.emptySet();
	}

	@Override
	public @NotNull GameMode getDefaultGameMode() {
		throw new UnsupportedOperationException("Method not implemented");
//		return GameMode.SURVIVAL;
	}

	@Override
	public void setDefaultGameMode(@NotNull GameMode mode) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull ConsoleCommandSender getConsoleSender() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull CommandSender createCommandSender(@NotNull Consumer<? super Component> feedback) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull File getWorldContainer() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@NotNull
	@Override
	public OfflinePlayer[] getOfflinePlayers() {
		throw new UnsupportedOperationException("Method not implemented");
//		return new OfflinePlayer[0];
	}

	@Override
	public @NotNull Messenger getMessenger() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull HelpMap getHelpMap() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull Inventory createInventory(@Nullable InventoryHolder owner, @NotNull InventoryType type) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull Inventory createInventory(@Nullable InventoryHolder owner, @NotNull InventoryType type, @NotNull Component title) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull Inventory createInventory(@Nullable InventoryHolder owner, @NotNull InventoryType type, @NotNull String title) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull Inventory createInventory(@Nullable InventoryHolder owner, int size) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull Inventory createInventory(@Nullable InventoryHolder owner, int size, @NotNull Component title) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull Inventory createInventory(@Nullable InventoryHolder owner, int size, @NotNull String title) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull Merchant createMerchant(@Nullable Component title) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull Merchant createMerchant(@Nullable String title) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public int getMaxChainedNeighborUpdates() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public int getMonsterSpawnLimit() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public int getAnimalSpawnLimit() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public int getWaterAnimalSpawnLimit() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public int getWaterAmbientSpawnLimit() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public int getWaterUndergroundCreatureSpawnLimit() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public int getAmbientSpawnLimit() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public int getSpawnLimit(@NotNull SpawnCategory spawnCategory) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public boolean isPrimaryThread() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull Component motd() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public void motd(@NotNull Component motd) {

	}

	@Override
	public @NotNull String getMotd() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public void setMotd(@NotNull String motd) {

	}

	@Override
	public @Nullable Component shutdownMessage() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @Nullable String getShutdownMessage() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public Warning.@NotNull WarningState getWarningState() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull ItemFactory getItemFactory() {
//		throw new UnsupportedOperationException("Method not implemented");
		return new MockItemFactory();
	}

	@Override
	public @Nullable ScoreboardManager getScoreboardManager() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull Criteria getScoreboardCriteria(@NotNull String name) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @Nullable CachedServerIcon getServerIcon() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull CachedServerIcon loadServerIcon(@NotNull File file) throws IllegalArgumentException, Exception {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull CachedServerIcon loadServerIcon(@NotNull BufferedImage image) throws IllegalArgumentException, Exception {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public void setIdleTimeout(int threshold) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public int getIdleTimeout() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public ChunkGenerator.@NotNull ChunkData createChunkData(@NotNull World world) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public ChunkGenerator.@NotNull ChunkData createVanillaChunkData(@NotNull World world, int x, int z) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull BossBar createBossBar(@Nullable String title, @NotNull BarColor color, @NotNull BarStyle style, @NotNull BarFlag... flags) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull KeyedBossBar createBossBar(@NotNull NamespacedKey key, @Nullable String title, @NotNull BarColor color, @NotNull BarStyle style, @NotNull BarFlag... flags) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull Iterator<KeyedBossBar> getBossBars() {
		throw new UnsupportedOperationException("Method not implemented");
//		return Collections.emptyIterator();
	}

	@Override
	public @Nullable KeyedBossBar getBossBar(@NotNull NamespacedKey key) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public boolean removeBossBar(@NotNull NamespacedKey key) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @Nullable Entity getEntity(@NotNull UUID uuid) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull double[] getTPS() {
		throw new UnsupportedOperationException("Method not implemented");
//		return new double[0];
	}

	@Override
	public @NotNull long[] getTickTimes() {
		throw new UnsupportedOperationException("Method not implemented");
//		return new long[0];
	}

	@Override
	public double getAverageTickTime() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull CommandMap getCommandMap() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @Nullable Advancement getAdvancement(@NotNull NamespacedKey key) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull Iterator<Advancement> advancementIterator() {
		throw new UnsupportedOperationException("Method not implemented");
//		return Collections.emptyIterator();
	}

	@Override
	public @NotNull BlockData createBlockData(@NotNull Material material) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull BlockData createBlockData(@NotNull Material material, @Nullable Consumer<? super BlockData> consumer) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull BlockData createBlockData(@NotNull String data) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull BlockData createBlockData(@Nullable Material material, @Nullable String data) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @Nullable <T extends Keyed> Tag<T> getTag(@NotNull String registry, @NotNull NamespacedKey tag, @NotNull Class<T> clazz) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull <T extends Keyed> Iterable<Tag<T>> getTags(@NotNull String registry, @NotNull Class<T> clazz) {
		throw new UnsupportedOperationException("Method not implemented");
//		return Collections.emptyList();
	}

	@Override
	public @Nullable LootTable getLootTable(@NotNull NamespacedKey key) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull List<Entity> selectEntities(@NotNull CommandSender sender, @NotNull String selector) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Method not implemented");
//		return Collections.emptyList();
	}

	@Override
	public @NotNull StructureManager getStructureManager() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @Nullable <T extends Keyed> Registry<T> getRegistry(@NotNull Class<T> tClass) {
		throw new UnsupportedOperationException("Method not implemented");
	}


	@Override
	public @NotNull UnsafeValues getUnsafe() {
		return new MockUnsafeValues();
	}

	@Override
	public @NotNull Spigot spigot() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public void reloadPermissions() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public boolean reloadCommandAliases() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public boolean suggestPlayerNamesWhenNullTabCompletions() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull String getPermissionMessage() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull Component permissionMessage() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public com.destroystokyo.paper.profile.@NotNull PlayerProfile createProfile(@NotNull UUID uuid) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public com.destroystokyo.paper.profile.@NotNull PlayerProfile createProfile(@NotNull String name) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public com.destroystokyo.paper.profile.@NotNull PlayerProfile createProfile(@Nullable UUID uuid, @Nullable String name) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public com.destroystokyo.paper.profile.@NotNull PlayerProfile createProfileExact(@Nullable UUID uuid, @Nullable String name) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public int getCurrentTick() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public boolean isStopping() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull MobGoals getMobGoals() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull DatapackManager getDatapackManager() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull PotionBrewer getPotionBrewer() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull RegionScheduler getRegionScheduler() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull AsyncScheduler getAsyncScheduler() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull GlobalRegionScheduler getGlobalRegionScheduler() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public boolean isOwnedByCurrentRegion(@NotNull World world, @NotNull Position position) {
		return false;
	}

	@Override
	public boolean isOwnedByCurrentRegion(@NotNull World world, @NotNull Position position, int squareRadiusChunks) {
		return false;
	}

	@Override
	public boolean isOwnedByCurrentRegion(@NotNull Location location) {
		return false;
	}

	@Override
	public boolean isOwnedByCurrentRegion(@NotNull Location location, int squareRadiusChunks) {
		return false;
	}

	@Override
	public boolean isOwnedByCurrentRegion(@NotNull World world, int chunkX, int chunkZ) {
		return false;
	}

	@Override
	public boolean isOwnedByCurrentRegion(@NotNull World world, int chunkX, int chunkZ, int squareRadiusChunks) {
		return false;
	}

	@Override
	public boolean isOwnedByCurrentRegion(@NotNull Entity entity) {
		return false;
	}

	@Override
	public void sendPluginMessage(@NotNull Plugin source, @NotNull String channel, @NotNull byte[] message) {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull Set<String> getListeningPluginChannels() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	@Override
	public @NotNull Iterable<? extends Audience> audiences() {
		throw new UnsupportedOperationException("Method not implemented");
	}
}
