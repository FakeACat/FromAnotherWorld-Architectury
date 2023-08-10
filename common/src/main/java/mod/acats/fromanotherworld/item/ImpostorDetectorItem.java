package mod.acats.fromanotherworld.item;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.memory.GlobalThingMemory;
import mod.acats.fromanotherworld.memory.ThingBaseOfOperations;
import mod.acats.fromanotherworld.spawning.SpawningManager;
import mod.acats.fromanotherworld.utilities.EntityUtilities;
import java.util.List;

import mod.acats.fromanotherworld.utilities.chunkloading.FAWChunkLoader;
import mod.acats.fromanotherworld.utilities.chunkloading.FAWChunkLoaders;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class ImpostorDetectorItem extends Item {
    public ImpostorDetectorItem(Properties settings) {
        super(settings);
    }

    private int mode = 0;

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (!world.isClientSide()) {
            if (user.isShiftKeyDown()) {
                this.mode++;
                if (this.mode == 4) {
                    this.mode = 0;
                }
                String modeName = switch (this.mode) {
                    case 0 -> "Highlight all Things within 512 blocks";
                    case 1 -> "Count all Things";
                    case 2 -> "Chunk Loader info";
                    case 3 -> "Thing base/director info";
                    default -> "guh";
                };
                msg(user, "Mode: " + modeName);
            }
            else {
                switch (this.mode) {
                    case 0 -> this.highlight(user, world);
                    case 1 -> this.count(user, world);
                    case 2 -> this.chunkLoaders(user, world);
                    case 3 -> this.bases(user, world);
                }
            }
        }
        return InteractionResultHolder.sidedSuccess(user.getItemInHand(hand), world.isClientSide());
    }

    private void highlight(Player user, Level world) {
        int entityCheckDist = 512;
        List<LivingEntity> nearbyEntities = world.getEntitiesOfClass(LivingEntity.class, new AABB(user.getX() - entityCheckDist, user.getY() - entityCheckDist, user.getZ() - entityCheckDist, user.getX() + entityCheckDist, user.getY() + entityCheckDist, user.getZ() + entityCheckDist));
        for (LivingEntity e:
                nearbyEntities) {
            if (EntityUtilities.isThing(e)){
                e.addEffect(new MobEffectInstance(MobEffects.GLOWING, 20, 0, false, false));
            }
        }
    }

    private void count(Player user, Level world) {
        List<? extends LivingEntity> allEntities = ((ServerLevel) world).getEntities(EntityTypeTest.forClass(LivingEntity.class), EntitySelector.NO_SPECTATORS);
        int things = EntityUtilities.numThingsInList(allEntities);
        SpawningManager spawningManager = SpawningManager.getSpawningManager((ServerLevel) world);
        msg(user, things + " Things");
        msg(user, spawningManager.alienThingsToSpawn + " Alien Things waiting to spawn");
    }

    private void chunkLoaders(Player user, Level world) {
        int i = 0;
        for (FAWChunkLoader l:
                FAWChunkLoaders.getChunkLoaders(((ServerLevel) world)).activeLoaders) {
            msg(user, "Chunk Loader " + i + ":");
            msg(user, " -Central Chunk X: " + l.chunkCentreX);
            msg(user, " -Central Chunk Z: " + l.chunkCentreZ);
            msg(user, " -Radius: " + l.radius + " chunks");
            msg(user, " -Minutes until deletion: " + l.ticksUntilRemoval);
            i++;
        }
        msg(user, ((ServerLevel) world).getForcedChunks().size() + " chunks forced");
    }
    private void bases(Player user, Level world) {
        int i = 0;
        for (ThingBaseOfOperations b:
                GlobalThingMemory.getGlobalThingMemory(((ServerLevel) world)).getBases()) {
            msg(user, "Thing Base of Operations " + i + ":");
            msg(user, " -X: " + b.getX());
            msg(user, " -Y: " + b.getY());
            msg(user, " -Z: " + b.getZ());
            msg(user, " -Size: " + b.getSize());
            if (FromAnotherWorld.mlDep.inDevEnv()) {
                msg(user, " -Director:");
                msg(user, "  -Aggression: " + b.director.getAggression());
                msg(user, "  -Hunger: " + b.director.getHunger());
            }
            i++;
        }
        user.sendSystemMessage(Component.literal(i + " thing bases"));
    }

    private static void msg(Player p, String message) {
        p.sendSystemMessage(Component.literal(message));
    }
}
