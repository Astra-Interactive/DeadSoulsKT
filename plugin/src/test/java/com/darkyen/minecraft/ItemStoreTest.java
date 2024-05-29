package com.darkyen.minecraft;

import com.darkyen.minecraft.database.SoulDatabase;
import com.darkyen.minecraft.models.Soul;
import com.darkyen.minecraft.utils.channels.ByteBufferChannel;
import com.darkyen.minecraft.utils.channels.DataInputChannel;
import com.darkyen.minecraft.utils.channels.DataOutputChannel;
import com.darkyen.minecraft.utils.serialization.Serialization;
import junit.framework.AssertionFailedError;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 */
class ItemStoreTest {

    private static final Logger LOG = Logger.getLogger(ItemStoreTest.class.getName());
    private static final Random random = new Random();

    private static void testSerialization(ItemStack item) throws Exception {
        final Soul soul = new Soul(
                random.nextBoolean() ? Bukkit.getOfflinePlayers()[0].getUniqueId() : null,
                Bukkit.getWorlds().get(0).getUID(),
                (double) random.nextInt(5000) - 2500,
                (double) random.nextInt(250),
                (double) random.nextInt(5000) - 2500,
                random.nextLong(),
                new ItemStack[]{item, item},
                random.nextInt(1000)
        );

        final ByteBufferChannel byteBufferChannel = new ByteBufferChannel();
        try (DataOutputChannel channel = new DataOutputChannel(byteBufferChannel)) {
            assertTrue(SoulDatabase.serializeSoul(soul, channel));
        }
        byteBufferChannel.position(0L);
        final Soul soulFromHell = SoulDatabase.deserializeSoul(new DataInputChannel(byteBufferChannel), SoulDatabase.CURRENT_DB_VERSION);

        assertEquals(soul.getWorld(), soulFromHell.getWorld());
        assertEquals(soul.getLocationY(), soulFromHell.getLocationY(),0.0001);
        assertEquals(soul.getLocationX(), soulFromHell.getLocationX(),0.0001);
        assertEquals(soul.getLocationZ(), soulFromHell.getLocationZ(),0.0001);
        assertEquals(soul.getCreationTimestamp(), soulFromHell.getCreationTimestamp());
        assertEquals(soul.getExperiencePoints(), soulFromHell.getExperiencePoints());
        assertEquals(soul.getOwner(), soulFromHell.getOwner());
        assertArrayEquals(soul.getItems(), soulFromHell.getItems());
    }

    private static void savingTest() throws IOException, Serialization.Exception {
        final Path temporaryDatabaseFile = Paths.get("testingdb.bin").toAbsolutePath();
        final SoulDatabase soulDatabase = new SoulDatabase(null, temporaryDatabaseFile);
        final Random random = new Random();

        for (int iteration = 0; iteration < 200; iteration++) {
            final ItemStack[] itemStacks = new ItemStack[random.nextInt(100)];
            for (int item = 0; item < itemStacks.length; item++) {
                itemStacks[item] = new ItemStack(Material.DIAMOND_SWORD, 1);
                itemStacks[item].addEnchantment(Enchantment.SHARPNESS, 3);
                final ItemMeta itemMeta = itemStacks[item].getItemMeta();
                assert itemMeta != null;
                itemMeta.setDisplayName("BLAH" + item);
                itemMeta.setUnbreakable(true);
                itemStacks[item].setItemMeta(itemMeta);
            }
            soulDatabase.addSoul(UUID.randomUUID(), UUID.randomUUID(), random.nextDouble(), random.nextDouble(), random.nextDouble(), itemStacks, random.nextInt(100000));
            assertTrue(soulDatabase.save());

            final List<Soul> loaded = SoulDatabase.load(temporaryDatabaseFile);
            final List<@Nullable Soul> expected = soulDatabase.getSoulsById();

            if (loaded.size() != expected.size()) {
                throw new AssertionError(loaded + " " + expected);
            }

            for (int i = 0; i < loaded.size(); i++) {
                final Soul loadedSoul = loaded.get(i);
                final Soul expectedSoul = expected.get(i);

                if (!expectedSoul.equals(loadedSoul)) {
                    throw new AssertionError(loadedSoul + " " + expectedSoul);
                }
            }
        }
        Files.delete(temporaryDatabaseFile);
    }

    public static void runLiveTest(Plugin plugin) throws Exception {
        // Known bad
        {
            final ItemStack item = new ItemStack(Material.COMPASS);
            final ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("foo");
            item.setItemMeta(meta);
            testSerialization(sanitize(item));
        }

        // Everything
        for (Material value : Material.values()) {
            if (!value.isItem() || value.getMaxStackSize() <= 0) {
                continue;
            }

            final ItemStack item = new ItemStack(value, Math.min(random.nextInt(64) + 1, value.getMaxStackSize()));

            while (random.nextBoolean()) {
                try {
                    for (Enchantment enchantment : Enchantment.values()) {
                        if (enchantment.canEnchantItem(item)) {
                            item.addUnsafeEnchantment(enchantment, 1 + random.nextInt(enchantment.getMaxLevel()));
                        }
                    }
                } catch (Exception ignored) {
                }
            }

            final ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                if (random.nextBoolean()) {
                    for (ItemFlag flag : ItemFlag.values()) {
                        if (random.nextBoolean()) {
                            meta.addItemFlags(flag);
                        }
                    }
                }

                if (random.nextBoolean()) {
                    meta.setDisplayName("Nombre: " + random.nextInt());
                }

                if (random.nextBoolean()) {
                    meta.addAttributeModifier(Attribute.GENERIC_LUCK, new AttributeModifier("foo", 3.14, AttributeModifier.Operation.ADD_NUMBER));
                }

                if (random.nextBoolean()) {
                    meta.setUnbreakable(random.nextBoolean());
                }

                if (random.nextBoolean()) {
                    meta.setLocalizedName("Localized Nombre: " + random.nextInt());
                }

                if (random.nextBoolean()) {
                    meta.setLore(Arrays.asList("Hello", "This is lore"));
                }

                item.setItemMeta(meta);
            }

            try {
                try {
                    testSerialization(item);
                } catch (AssertionFailedError e) {
                    testSerialization(sanitize(item));
                }
            } catch (Throwable t) {
                System.out.println("Failed: " + item.getType());
            }
        }

        try {
            savingTest();
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Development test failed (sync)", e);
        }

        Bukkit.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                savingTest();
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Development test failed (async)", e);
            }
        });
    }

    /**
     * Spigot adds some fields on serialization round-trip, which break tests.
     * Therefore we must do one extra round trip, to ensure that there is no problem.
     */
    @SuppressWarnings("unchecked")
    @NotNull
    private static ItemStack sanitize(@NotNull ItemStack item) {
        final Map<String, Object> serialized = item.serialize();
        try {
            final Map<String, Object> sanitized = (Map<String, Object>) sanitize(serialized);
            return ItemStack.deserialize(sanitized);
        } catch (Exception e) {
            throw new RuntimeException("Failed to sanitize " + item, e);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Object sanitize(Object object) throws ClassNotFoundException {
        if (object instanceof Map) {
            final HashMap<String, Object> newMap = new HashMap<>();
            for (Map.Entry<String, Object> entry : ((Map<String, Object>) object).entrySet()) {
                newMap.put(entry.getKey(), sanitize(entry.getValue()));
            }
            return newMap;
        } else if (object instanceof List) {
            final ArrayList<Object> newList = new ArrayList<>();
            for (Object o : (List<Object>) object) {
                newList.add(sanitize(o));
            }
            return newList;
        } else if (object instanceof ConfigurationSerializable) {
            final String alias = ConfigurationSerialization.getAlias((Class) object.getClass());
            final Map<String, Object> sanitized = (Map<String, Object>) sanitize(((ConfigurationSerializable) object).serialize());

            Class<? extends ConfigurationSerializable> serializedClass = ConfigurationSerialization.getClassByAlias(alias);
            if (serializedClass == null) {
                //noinspection unchecked
                serializedClass = (Class<? extends ConfigurationSerializable>) Class.forName(alias);
            }

            return ConfigurationSerialization.deserializeObject(sanitized, serializedClass);
        } else return object;
    }
}
