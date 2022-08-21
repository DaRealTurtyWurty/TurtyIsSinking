package dev.turtywurty.turtyissinking.init;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.entities.BossBaby;
import dev.turtywurty.turtyissinking.entities.Wheelchair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES,
        TurtyIsSinking.MODID);

    public static final RegistryObject<EntityType<BossBaby>> BOSS_BABY = ENTITIES.register("boss_baby",
        () -> EntityType.Builder.of(BossBaby::new, MobCategory.AMBIENT).sized(.55f, 0.87f)
            .build(new ResourceLocation(TurtyIsSinking.MODID, "boss_baby").toString()));

    public static final RegistryObject<EntityType<Wheelchair>> WHEELCHAIR = ENTITIES.register("wheelchair",
        () -> EntityType.Builder.of(Wheelchair::new, MobCategory.MISC).sized(1f, 1.5f)
            .build(new ResourceLocation(TurtyIsSinking.MODID, "wheelchair").toString()));
}
