package acats.fromanotherworld.entity.interfaces;

import acats.fromanotherworld.utilities.ServerUtilities;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public interface ImportantDeathMob {
    // The name used in the death message
    default Component deathMessageName(){
        return ((LivingEntity) this).getName();
    }

    // The style used in the death message
    default Style deathMessageStyle(){
        return Style.EMPTY.withColor(0xFF0000);
    }

    // The death message
    default MutableComponent deathMessage(){
        return MutableComponent.create(this.deathMessageName()
                .getContents())
                .append(Component.translatable("fromanotherworld.announcement.defeated"))
                .withStyle(this.deathMessageStyle());
    }

    // Players within this range will receive the death message and loot
    default int deathRange(){
        return 30;
    }

    // The location for the loot table to drop on every player
    default ResourceLocation perPlayerLootTable(){
        LivingEntity entity = (LivingEntity) this;
        return BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).withPrefix("entities/per_player_loot/");
    }

    // You do not need to override this under normal circumstances
    // Call in die(DamageSource)
    default void importantDie(DamageSource damageSource){
        LivingEntity entity = (LivingEntity) this;
        ServerUtilities.forAllPlayersNearEntity(entity, this.deathRange(), player -> this.forEachNearbyPlayer(player, damageSource));
    }

    // You do not need to override this under normal circumstances
    // Run for every nearby player within the defined range when importantDie is called
    default void forEachNearbyPlayer(Player player, DamageSource damageSource){
        player.sendSystemMessage(this.deathMessage());

        LivingEntity entity = ((LivingEntity) this);
        Level level = entity.level();
        if (level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)){
            this.giveFromLootTable(player, damageSource);
        }

        player.awardKillScore(entity, 0, damageSource);
    }

    // You do not need to override this under normal circumstances
    // Drops items from the loot table
    default void giveFromLootTable(Player player, DamageSource damageSource){
        LivingEntity entity = (LivingEntity) this;
        Level level = entity.level();
        MinecraftServer server = level.getServer();


        if (server != null){
            ResourceLocation resourceLocation = this.perPlayerLootTable();
            LootTable lootTable = server.getLootData().getLootTable(resourceLocation);
            LootParams.Builder builder = (new LootParams.Builder((ServerLevel)level))
                    .withParameter(LootContextParams.THIS_ENTITY, entity)
                    .withParameter(LootContextParams.ORIGIN, entity.position())
                    .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
                    .withOptionalParameter(LootContextParams.KILLER_ENTITY, damageSource.getEntity())
                    .withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, damageSource.getDirectEntity());

            LootParams lootParams = builder.create(LootContextParamSets.ENTITY);
            lootTable.getRandomItems(lootParams, entity.getLootTableSeed(), player::addItem);
        }
    }
}
