package mod.acats.fromanotherworld.entity.misc;

import mod.acats.fromanotherworld.block.interfaces.AssimilatedSculk;
import mod.acats.fromanotherworld.registry.BlockRegistry;
import mod.acats.fromanotherworld.registry.EntityRegistry;
import mod.acats.fromanotherworld.utilities.BlockUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SculkRevealer extends Entity {
    public SculkRevealer(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public static void create(Level level, BlockPos blockPos, float blocksExpansionPerTick, int ticksUntilDeletion, float strength) {
        if (!level.isClientSide()) {
            SculkRevealer sculkRevealer = EntityRegistry.SCULK_REVEALER.get().create(level);
            if (sculkRevealer != null) {
                sculkRevealer.setPos(blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D);
                sculkRevealer.expansionRate = blocksExpansionPerTick;
                sculkRevealer.maxExpansionTime = ticksUntilDeletion;
                sculkRevealer.prevPlacement = 0;
                sculkRevealer.strength = strength;
                level.addFreshEntity(sculkRevealer);
            }
        }
    }

    private float expansionRate;
    private int expansionProgress;
    private int maxExpansionTime;
    public float strength;

    private float prevPlacement;

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        this.expansionRate = compoundTag.getFloat("ExpansionRate");
        this.expansionProgress = compoundTag.getInt("ExpansionProgress");
        this.maxExpansionTime = compoundTag.getInt("MaxExpansionTime");
        this.strength = compoundTag.getFloat("Strength");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putFloat("ExpansionRate", this.expansionRate);
        compoundTag.putInt("ExpansionProgress", this.expansionProgress);
        compoundTag.putInt("MaxExpansionTime", this.maxExpansionTime);
        compoundTag.putFloat("Strength", this.strength);
    }

    private boolean attemptedToRevealATentacle;

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {
            this.expansionProgress++;

            if (this.expansionProgress > this.maxExpansionTime) {
                this.discard();
            }

            float radius = this.expansionProgress * this.expansionRate;

            if (radius >= this.prevPlacement + 1.0F) {
                this.prevPlacement = radius;
                BlockUtilities.forEachBlockInCubeCentredAt(this.blockPosition(), (int) radius, blockPos -> {
                    if (blockPos.distSqr(this.blockPosition()) < radius * radius) {
                        BlockState blockState = this.level().getBlockState(blockPos);
                        if (blockState.getBlock() instanceof AssimilatedSculk sculkBlock && !sculkBlock.revealed(blockState)) {
                            if (blockState.is(BlockRegistry.ASSIMILATED_SCULK_TENTACLES.get())) {
                                if (!attemptedToRevealATentacle) {
                                    attemptedToRevealATentacle = true;
                                    sculkBlock.reveal(this.level(), blockPos, blockState, strength);
                                }
                            } else {
                                sculkBlock.reveal(this.level(), blockPos, blockState, strength);
                            }
                        }
                    }
                });
            }
        }
    }
}
