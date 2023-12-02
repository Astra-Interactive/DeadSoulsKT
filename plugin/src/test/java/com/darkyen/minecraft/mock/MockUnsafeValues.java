package com.darkyen.minecraft.mock;

import com.destroystokyo.paper.util.VersionFetcher;
import com.google.common.collect.Multimap;
import io.papermc.paper.inventory.ItemRarity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.CreativeCategory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import org.jetbrains.annotations.Nullable;

public class MockUnsafeValues implements UnsafeValues {

    @Override
    public Entity deserializeEntity(byte[] data, World world) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public ComponentFlattener componentFlattener() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public PlainComponentSerializer plainComponentSerializer() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public PlainTextComponentSerializer plainTextSerializer() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public GsonComponentSerializer gsonComponentSerializer() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public GsonComponentSerializer colorDownsamplingGsonComponentSerializer() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public LegacyComponentSerializer legacyComponentSerializer() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public Component resolveWithContext(Component component, CommandSender context, Entity scoreboardSubject, boolean bypassPermissions) throws IOException {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public void reportTimings() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public Material toLegacy(Material material) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public Material fromLegacy(Material material) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public Material fromLegacy(MaterialData material) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public Material fromLegacy(MaterialData material, boolean itemPriority) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public BlockData fromLegacy(Material material, byte data) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public Material getMaterial(String material, int version) {
        return Material.getMaterial(material);
    }

    @Override
    public int getDataVersion() {
        return 1234;
    }

    @Override
    public ItemStack modifyItemStack(ItemStack stack, String arguments) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public void checkSupported(PluginDescriptionFile pdf) throws InvalidPluginException {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public byte[] processClass(PluginDescriptionFile pdf, String path, byte[] clazz) {
        throw new UnsupportedOperationException("Method not implemented");
//        return new byte[0];
    }

    @Override
    public Advancement loadAdvancement(NamespacedKey key, String advancement) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public boolean removeAdvancement(NamespacedKey key) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(Material material, EquipmentSlot slot) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public CreativeCategory getCreativeCategory(Material material) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public String getBlockTranslationKey(Material material) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public String getItemTranslationKey(Material material) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public String getTimingsServerName() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public VersionFetcher getVersionFetcher() {
        return UnsafeValues.super.getVersionFetcher();
    }

    @Override
    public byte[] serializeItem(ItemStack item) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public ItemStack deserializeItem(byte[] data) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public byte[] serializeEntity(Entity entity) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public Entity deserializeEntity(byte[] data, World world, boolean preserveUUID) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public String getTranslationKey(EntityType type) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public String getTranslationKey(ItemStack itemStack) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public @Nullable FeatureFlag getFeatureFlag(@NotNull NamespacedKey key) {
        return null;
    }

    @Override
    public PotionType.InternalPotionData getInternalPotionData(NamespacedKey key) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public boolean isSupportedApiVersion(String apiVersion) {
        return false;
    }

    @Override
    public int nextEntityId() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public @NotNull String getMainLevelName() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public ItemRarity getItemRarity(Material material) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public ItemRarity getItemStackRarity(ItemStack itemStack) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public boolean isValidRepairItemStack(@NotNull ItemStack itemToBeRepaired, @NotNull ItemStack repairMaterial) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getItemAttributes(@NotNull Material material, @NotNull EquipmentSlot equipmentSlot) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public int getProtocolVersion() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public boolean hasDefaultEntityAttributes(@NotNull NamespacedKey entityKey) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public @NotNull Attributable getDefaultEntityAttributes(@NotNull NamespacedKey entityKey) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public boolean isCollidable(@NotNull Material material) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public @NotNull NamespacedKey getBiomeKey(RegionAccessor accessor, int x, int y, int z) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public void setBiomeKey(RegionAccessor accessor, int x, int y, int z, NamespacedKey biomeKey) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public String getStatisticCriteriaKey(@NotNull Statistic statistic) {
        throw new UnsupportedOperationException("Method not implemented");
    }
}
