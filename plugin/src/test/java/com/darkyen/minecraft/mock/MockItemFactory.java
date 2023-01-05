package com.darkyen.minecraft.mock;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Objects;
import java.util.Random;
import java.util.function.UnaryOperator;

public class MockItemFactory implements ItemFactory {
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public @Nullable ItemMeta getItemMeta(@NotNull Material material) {
        return null;
    }

    @Override
    public boolean isApplicable(@Nullable ItemMeta meta, @Nullable ItemStack stack) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public boolean isApplicable(@Nullable ItemMeta meta, @Nullable Material material) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public boolean equals(@Nullable ItemMeta meta1, @Nullable ItemMeta meta2) throws IllegalArgumentException {
        return Objects.equals(meta1, meta2);
    }

    @Override
    public @Nullable ItemMeta asMetaFor(@NotNull ItemMeta meta, @NotNull ItemStack stack) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public @Nullable ItemMeta asMetaFor(@NotNull ItemMeta meta, @NotNull Material material) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public @NotNull Color getDefaultLeatherColor() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public @NotNull ItemStack createItemStack(@NotNull String input) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public @NotNull Material updateMaterial(@NotNull ItemMeta meta, @NotNull Material material) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public @NotNull ItemStack enchantWithLevels(@NotNull ItemStack itemStack, @Range(from = 1L, to = 30L) int levels, boolean allowTreasure, @NotNull Random random) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public @NotNull HoverEvent<HoverEvent.ShowItem> asHoverEvent(@NotNull ItemStack item, @NotNull UnaryOperator<HoverEvent.ShowItem> op) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public @NotNull Component displayName(@NotNull ItemStack itemStack) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public @Nullable String getI18NDisplayName(@Nullable ItemStack item) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public @NotNull ItemStack ensureServerConversions(@NotNull ItemStack item) {
        // TODO is it ok??
        return item;
    }

    @Override
    public @NotNull Content hoverContentOf(@NotNull ItemStack itemStack) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public @NotNull Content hoverContentOf(@NotNull Entity entity) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public @NotNull Content hoverContentOf(@NotNull Entity entity, @Nullable String customName) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public @NotNull Content hoverContentOf(@NotNull Entity entity, @Nullable BaseComponent customName) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public @NotNull Content hoverContentOf(@NotNull Entity entity, @NotNull BaseComponent[] customName) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public @Nullable ItemStack getSpawnEgg(@Nullable EntityType type) {
        throw new UnsupportedOperationException("Method not implemented");
    }
}
