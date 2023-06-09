package acats.fromanotherworld.item;

import acats.fromanotherworld.spawning.SpawningManager;
import acats.fromanotherworld.utilities.EntityUtilities;
import java.util.List;
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

public class ImpostorDetectorItem extends Item {
    public ImpostorDetectorItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        boolean count = user.isShiftKeyDown();

        if (count){
            if (!world.isClientSide()){
                List<? extends LivingEntity> allEntities = ((ServerLevel) world).getEntities(EntityTypeTest.forClass(LivingEntity.class), EntitySelector.NO_SPECTATORS);
                int things = EntityUtilities.numThingsInList(allEntities);
                SpawningManager spawningManager = SpawningManager.getSpawningManager((ServerLevel) world);
                user.sendSystemMessage(Component.literal(things + " Things"));
                user.sendSystemMessage(Component.literal(spawningManager.alienThingsToSpawn + " Alien Things waiting to spawn"));
            }
        }
        else{
            int entityCheckDist = 512;
            List<LivingEntity> nearbyEntities = world.getEntitiesOfClass(LivingEntity.class, new AABB(user.getX() - entityCheckDist, user.getY() - entityCheckDist, user.getZ() - entityCheckDist, user.getX() + entityCheckDist, user.getY() + entityCheckDist, user.getZ() + entityCheckDist));
            for (LivingEntity e:
                    nearbyEntities) {
                if (EntityUtilities.isThing(e)){
                    e.addEffect(new MobEffectInstance(MobEffects.GLOWING, 20, 0, false, false));
                }
            }
        }
        return InteractionResultHolder.sidedSuccess(user.getItemInHand(hand), world.isClientSide());
    }
}
