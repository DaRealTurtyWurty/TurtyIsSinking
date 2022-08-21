package dev.turtywurty.turtyissinking.networking.serverbound;

import java.util.function.Supplier;

import dev.turtywurty.turtyissinking.items.BackpackItem;
import dev.turtywurty.turtyissinking.menus.BackpackMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

public class SOpenBackpackPacket {
    private final int backpackSlot;

    public SOpenBackpackPacket(FriendlyByteBuf buf) {
        this(buf.readInt());
    }

    public SOpenBackpackPacket(int backpackSlot) {
        this.backpackSlot = backpackSlot;
    }
    
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.backpackSlot);
    }
    
    public static void handle(SOpenBackpackPacket pkt, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            final NetworkEvent.Context ctx = context.get();
            final MenuConstructor menuProvider = BackpackMenu.getServerContainerProvider(
                new ItemStackHandler(
                    BackpackItem.getInventory(ctx.getSender().getInventory().getItem(pkt.backpackSlot))),
                ctx.getSender().blockPosition(), pkt.backpackSlot);
            final MenuProvider provider = new SimpleMenuProvider(menuProvider, Component.empty());
            NetworkHooks.openScreen(ctx.getSender(), provider, ctx.getSender().blockPosition());
        });

        context.get().setPacketHandled(true);
    }
}
