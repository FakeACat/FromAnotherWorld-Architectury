package mod.acats.fromanotherworld.transformation;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public record TransformationContext(ServerLevel level,
                                    LivingEntity previousForm,
                                    CompoundTag previousFormTag,
                                    Vec3 position,
                                    float xRot,
                                    float yRot,
                                    Component name,
                                    float prevWidth,
                                    float prevHeight) {
    public EntityType<?> previousType() {
        return this.previousForm().getType();
    }

    public BlockPos blockPosition() {
        return BlockPos.containing(this.position());
    }
}
