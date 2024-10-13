package com.darkyen.minecraft;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import com.darkyen.minecraft.database.SoulDatabase;
import com.darkyen.minecraft.models.Soul;
import com.darkyen.minecraft.utils.channels.ByteBufferChannel;
import com.darkyen.minecraft.utils.channels.DataInputChannel;
import com.darkyen.minecraft.utils.channels.DataOutputChannel;
import com.darkyen.minecraft.utils.serialization.Serialization;
import io.papermc.paper.ServerBuildInfo;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.UnsafeValues;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.After;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;


/**
 *
 */
public final class SoulDatabaseTest {

    UnsafeValues createMockUnsafeValues() {
        UnsafeValues unsafeValues = mock(UnsafeValues.class);
        when(unsafeValues.getDataVersion()).thenReturn(1234);
//        when(unsafeValues.getFeatureFlag(any())).thenReturn(null);
        when(unsafeValues.isSupportedApiVersion(anyString())).thenReturn(false);
        when(unsafeValues.getMaterial(any(), anyInt())).thenAnswer(
                (Answer) invocation -> {
                    Object[] arguments = invocation.getArguments();
                    if (arguments == null) return null;
                    if (arguments[0] == null) return null;
                    else return Material.getMaterial((String) arguments[0]);
                }
        );
        return unsafeValues;
    }

    ItemFactory createMockItemFactory() {
        ItemFactory itemFactory = mock(ItemFactory.class);
        when(itemFactory.getItemMeta(any())).thenReturn(null);
        when(itemFactory.equals(any(), any())).thenAnswer(
                (Answer) invocation -> {
                    Object[] arguments = invocation.getArguments();
                    @Nullable ItemMeta meta1 = (ItemMeta) arguments[0];
                    @Nullable ItemMeta meta2 = (ItemMeta) arguments[1];
                    return Objects.equals(meta1, meta2);
                }
        );

        when(itemFactory.ensureServerConversions(any())).thenAnswer(
                (Answer) invocation -> {
                    return invocation.getArgument(0);
                }
        );
        return itemFactory;
    }

    Server createMockServer() {
        Server server = mock(Server.class);
        when(server.getLogger()).thenReturn(Logger.getLogger("ServerStub"));
        when(server.getName()).thenReturn("TEST");
        when(server.getVersion()).thenReturn("TEST_VER");
        when(server.getBukkitVersion()).thenReturn("BUKKIT_TEST_VER");
        when(server.getOnlinePlayers()).thenReturn(Collections.emptyList());
        when(server.getWhitelistedPlayers()).thenReturn(Collections.emptySet());
        when(server.getIp()).thenReturn("0.0.0.0");
        when(server.getUpdateFolder()).thenReturn("server-stub-update-folder");
        when(server.createExplorerMap(any(), any(), any())).thenReturn(null);

        UnsafeValues unsafeValues = createMockUnsafeValues();
        when(server.getUnsafe()).thenReturn(unsafeValues);

        ItemFactory itemFactory = createMockItemFactory();
        when(server.getItemFactory()).thenReturn(itemFactory);
        return server;
    }

    //    @Before
    public void setup() {
        ServerBuildInfo serverBuildInfo = mock(ServerBuildInfo.class);

        try (MockedStatic<ServerBuildInfo> mockedStatic = mockStatic(ServerBuildInfo.class)) {
            mockedStatic.when(ServerBuildInfo::buildInfo).thenReturn(serverBuildInfo);
            assertEquals(ServerBuildInfo.buildInfo(), serverBuildInfo);
            Bukkit.setServer(createMockServer());
        }
    }

    private ServerMock server;

    private DeadSouls plugin;

    @Before
    public void setUp() {
        this.server = MockBukkit.mock();
        this.plugin = MockBukkit.load(DeadSouls.class);
    }

    @After
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void brokenItemTest() throws IOException, Serialization.Exception {
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
            assertTrue(SoulDatabase.serializeSoul(soul, channel));
        }
        byteBufferChannel.position(0L);

        final Soul deserializedSoul = SoulDatabase.deserializeSoul(new DataInputChannel(byteBufferChannel), SoulDatabase.CURRENT_DB_VERSION);

        assertEquals(soul.getOwner(), deserializedSoul.getOwner());
        assertEquals(soul.getWorld(), deserializedSoul.getWorld());
        assertEquals(soul.getLocationX(), deserializedSoul.getLocationX(), 0.0000001);
        assertEquals(soul.getLocationY(), deserializedSoul.getLocationY(), 0.0000001);
        assertEquals(soul.getLocationZ(), deserializedSoul.getLocationZ(), 0.0000001);
        assertEquals(soul.getExperiencePoints(), deserializedSoul.getExperiencePoints());
        assertEquals(soul.getCreationTimestamp(), deserializedSoul.getCreationTimestamp());
        System.out.println(Arrays.toString(goodItems));
        System.out.println(Arrays.toString(deserializedSoul.getItems()));
        assertArrayEquals(goodItems, deserializedSoul.getItems());

        assertEquals(byteBufferChannel.size(), byteBufferChannel.position());
    }

}
