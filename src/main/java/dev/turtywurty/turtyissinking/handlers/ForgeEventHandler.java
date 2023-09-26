package dev.turtywurty.turtyissinking.handlers;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.capabilities.playerbones.PlayerBones;
import dev.turtywurty.turtyissinking.capabilities.playerbones.PlayerBonesCapability;
import dev.turtywurty.turtyissinking.capabilities.playerbones.PlayerBonesProvider;
import dev.turtywurty.turtyissinking.client.screens.BonesawScreen.PlayerBone;
import dev.turtywurty.turtyissinking.init.EnchantmentInit;
import dev.turtywurty.turtyissinking.networking.PacketHandler;
import dev.turtywurty.turtyissinking.networking.clientbound.CSyncPlayerBonesPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags.Blocks;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.level.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.network.PacketDistributor;

import java.util.concurrent.ThreadLocalRandom;

@Mod.EventBusSubscriber(modid = TurtyIsSinking.MODID, bus = Bus.FORGE)
public class ForgeEventHandler {
    @SubscribeEvent
    public static void attachEntityCaps(AttachCapabilitiesEvent<Entity> event) {
        PlayerBonesProvider.attach(event);
    }

    @SubscribeEvent
    public static void attachItemCaps(AttachCapabilitiesEvent<ItemStack> event) {}

    @SubscribeEvent
    public static void breakBlock(BreakEvent event) {
        Hooks.explosiveTouch(event);
    }

    @SubscribeEvent
    public static void breakBlockSpeed(BreakSpeed event) {
        Hooks.stoneTouch(event);
    }

    @SubscribeEvent
    public static void playerClone(PlayerEvent.Clone event) {
        event.getEntity().getCapability(PlayerBonesCapability.PLAYER_BONES).invalidate();
        TurtyIsSinking.LOGGER.info("Capability invalidated");
    }

    @SubscribeEvent
    public static void playerJoined(PlayerLoggedInEvent event) {
        if (!event.getEntity().level.isClientSide) {
            event.getEntity().getCapability(PlayerBonesCapability.PLAYER_BONES)
                .ifPresent(cap -> PacketHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()),
                    new CSyncPlayerBonesPacket(cap.getSawn().toArray(PlayerBone[]::new))));
        }
    }

    @SubscribeEvent
    public static void playerJump(LivingJumpEvent event) {
        if (event.getEntity() instanceof final Player player) {
            final PlayerBones cap = player.getCapability(PlayerBonesCapability.PLAYER_BONES).orElse(null);
            if (cap == null)
                return;

            if (cap.getSawn().contains(PlayerBone.LEFT_LEG) && cap.getSawn().contains(PlayerBone.RIGHT_LEG)) {
                player.setDeltaMovement(new Vec3(0, -50f, 0));
            }
        }
    }
    
    @SubscribeEvent
    public static void playerTick(PlayerTickEvent event) {
        final Player player = event.player;
        final PlayerBones cap = player.getCapability(PlayerBonesCapability.PLAYER_BONES).orElse(null);
        if (cap == null)
            return;

        final Vec3 movement = player.getDeltaMovement();
        if (cap.getSawn().contains(PlayerBone.LEFT_LEG) || cap.getSawn().contains(PlayerBone.RIGHT_LEG)) {
            if (player.isOnGround() && (movement.x() != 0 || movement.z() != 0)) {
                player.setPos(new Vec3(player.xOld, player.getY(), player.zOld));
            }
            
            if (player.isSprinting()) {
                player.setSprinting(false);
            }

            if (player.isSwimming()) {
                player.setSwimming(false);
            }
        }
        
        if (cap.getSawn().contains(PlayerBone.LEFT_LEG) && cap.getSawn().contains(PlayerBone.RIGHT_LEG)) {
            player.eyeHeight = 1.25f;
        }
    }

    private static class Hooks {
        private static void explosiveTouch(BreakEvent event) {
            final int explosiveTouchLevel = EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.EXPLOSIVE_TOUCH.get(),
                event.getPlayer());
            if (!event.getPlayer().isCreative() && explosiveTouchLevel > 0 && !event.getLevel().isClientSide()
                && ThreadLocalRandom.current().nextInt(2, 10 / explosiveTouchLevel) == 2) {
                final ServerLevel level = (ServerLevel) event.getLevel();
                level.explode(event.getPlayer(), DamageSource.explosion(null),
                    new ExplosionDamageCalculator(), event.getPos().getX(), event.getPos().getY(),
                    event.getPos().getZ(), ThreadLocalRandom.current().nextFloat(5f, 15f), false,
                    Level.ExplosionInteraction.BLOCK);
            }
        }

        private static void stoneTouch(BreakSpeed event) {
            final int stoneTouchLevel = EnchantmentHelper.getEnchantmentLevel(EnchantmentInit.STONE_TOUCH.get(),
                event.getEntity());
            if (!event.getEntity().isCreative() && stoneTouchLevel > 0 && event.getState().is(Blocks.STONE)) {
                event.setNewSpeed(event.getOriginalSpeed() + stoneTouchLevel * 1.25f);
            }
        }
    }
}
