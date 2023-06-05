package acats.fromanotherworld.item;

import acats.fromanotherworld.spawning.SpawningManager;
import acats.fromanotherworld.utilities.EntityUtilities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class ImpostorDetectorItem extends Item {
    public ImpostorDetectorItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        boolean count = user.isSneaking();

        if (count){
            if (!world.isClient()){
                List<? extends LivingEntity> allEntities = ((ServerWorld) world).getEntitiesByType(TypeFilter.instanceOf(LivingEntity.class), EntityPredicates.EXCEPT_SPECTATOR);
                int things = EntityUtilities.numThingsInList(allEntities);
                SpawningManager spawningManager = SpawningManager.getSpawningManager((ServerWorld) world);
                user.sendMessage(Text.literal(things + " Things"));
                user.sendMessage(Text.literal(spawningManager.alienThingsToSpawn + " Alien Things waiting to spawn"));
            }
        }
        else{
            int entityCheckDist = 512;
            List<LivingEntity> nearbyEntities = world.getNonSpectatingEntities(LivingEntity.class, new Box(user.getX() - entityCheckDist, user.getY() - entityCheckDist, user.getZ() - entityCheckDist, user.getX() + entityCheckDist, user.getY() + entityCheckDist, user.getZ() + entityCheckDist));
            for (LivingEntity e:
                    nearbyEntities) {
                if (EntityUtilities.isThing(e)){
                    e.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 20, 0, false, false));
                }
            }
        }
        return TypedActionResult.success(user.getStackInHand(hand), world.isClient());
    }
}
