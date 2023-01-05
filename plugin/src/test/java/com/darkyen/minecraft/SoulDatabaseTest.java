package com.darkyen.minecraft;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import com.darkyen.minecraft.api.ISoul;
import com.darkyen.minecraft.api.Soul;
import com.darkyen.minecraft.mock.ServerStub;
import com.darkyen.minecraft.utils.channels.ByteBufferChannel;
import com.darkyen.minecraft.utils.channels.DataInputChannel;
import com.darkyen.minecraft.utils.channels.DataOutputChannel;
import com.darkyen.minecraft.utils.serialization.Serialization;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 */
public class SoulDatabaseTest {
	private static ServerMock server;
	private static DeadSouls plugin;

    @BeforeAll
    static void setup() {
//		server = MockBukkit.mock();
//		plugin = MockBukkit.load(DeadSouls.class);
        Bukkit.setServer(new ServerStub() {
        });
    }

    @AfterAll
    public static void tearDown() {
//		MockBukkit.unmock();
    }

    @Test
    void brokenItemTest() throws IOException, Serialization.Exception {
        final ItemStack goodItem1 = new ItemStack(Material.DIRT, 5);
        final ItemStack goodItem2 = new ItemStack(Material.COBBLESTONE, 50);

        final ItemStack badItem = new ItemStack(Material.DIAMOND_SWORD, 1) {
            @Override
            public @NotNull Map<String, Object> serialize() {
                throw new RuntimeException("this item is not serializable, sorry (not error)");
            }
        };

        final ItemStack[] brokenItems = {goodItem1, badItem, goodItem2};
        final ItemStack[] goodItems = {goodItem1, goodItem2};
        final Soul soul = new Soul(UUID.randomUUID(), UUID.randomUUID(), 1.0, 2.0, 3.0, 1234567890L, brokenItems, 98765);

        final ByteBufferChannel byteBufferChannel = new ByteBufferChannel();
        try (DataOutputChannel channel = new DataOutputChannel(byteBufferChannel)) {
            Assertions.assertTrue(SoulDatabase.serializeSoul(soul, channel));
        }
        byteBufferChannel.position(0L);

        final Soul deserializedSoul = SoulDatabase.deserializeSoul(new DataInputChannel(byteBufferChannel), SoulDatabase.CURRENT_DB_VERSION);

        assertEquals(soul.getOwner(), deserializedSoul.getOwner());
        assertEquals(soul.getWorld(), deserializedSoul.getWorld());
        assertEquals(soul.getLocationX(), deserializedSoul.getLocationX());
        assertEquals(soul.getLocationY(), deserializedSoul.getLocationY());
        assertEquals(soul.getLocationZ(), deserializedSoul.getLocationZ());
        assertEquals(soul.getExperiencePoints(), deserializedSoul.getExperiencePoints());
        assertEquals(soul.getCreationTimestamp(), deserializedSoul.getCreationTimestamp());
        System.out.println(Arrays.toString(goodItems));
        System.out.println(Arrays.toString(deserializedSoul.getItems()));
        assertArrayEquals(goodItems, deserializedSoul.getItems());

        assertEquals(byteBufferChannel.size(), byteBufferChannel.position());
    }

}
