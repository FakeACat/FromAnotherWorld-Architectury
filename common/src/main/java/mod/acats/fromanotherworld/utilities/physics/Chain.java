package mod.acats.fromanotherworld.utilities.physics;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Chain {
    public final List<Segment> segments;
    public Chain(Vec3 position, int... segmentLengths) {
        this.segments = NonNullList.create();
        for (int segment:
             segmentLengths) {
            float realLength = segment / 16.0F;
            this.segments.add(new Segment(realLength, position));
        }
    }

    public Chain(List<Segment> segments) {
        this.segments = segments;
    }

    public Vec3 updatePosition(Vec3 desiredPos, Vec3 pointToPos, @Nullable Vec3 anchor, float maxTurnSpeed) {
        for (Segment segment:
             this.segments) {
            segment.pointTo(pointToPos, maxTurnSpeed);
            segment.moveTo(desiredPos);
            desiredPos = segment.getTailPos();
            pointToPos = segment.position;
        }

        if (anchor != null) {
            Vec3 offset = anchor.subtract(desiredPos);
            for (Segment segment:
                    this.segments) {
                segment.position = segment.position.add(offset);
            }
        }

        return segments.get(0).position;
    }

    public void writeTo(CompoundTag tag, String name) {
        CompoundTag compoundTag = new CompoundTag();

        int i = 0;
        for (Segment segment:
             this.segments) {
            compoundTag.put("segment" + i, segment.toCompoundTag());
            i++;
        }

        tag.put(name, compoundTag);
    }

    public @Nullable static Chain readFrom(CompoundTag tag, String name) {
        if (!tag.contains(name)) {
            return null;
        }
        CompoundTag compoundTag = tag.getCompound(name);
        List<Segment> segments1 = NonNullList.create();
        int i = 0;
        while (compoundTag.contains("segment" + i)) {
            segments1.add(Segment.fromCompoundTag(compoundTag.getCompound("segment" + i)));
            i++;
        }
        return new Chain(segments1);
    }

    public static class Segment {
        public final float length;
        public Vec3 position;

        public float pitch;
        public float pitchRadians() {
            return this.pitch * (float) Math.PI / 180.0F;
        }
        public float yaw;
        public float yawRadians() {
            return this.yaw * (float) Math.PI / 180.0F;
        }

        private Segment(float length, Vec3 position) {
            this.length = length;
            this.position = position;
        }

        private void pointTo(Vec3 pointToPos, float maxTurnSpeed) {
            Vec3 offset = pointToPos.subtract(this.position);
            double d = offset.x;
            double e = offset.y;
            double f = offset.z;
            double g = Math.sqrt(d * d + f * f);
            float desiredPitch = Mth.wrapDegrees((float)(-(Mth.atan2(e, g) * 57.2957763671875)));
            float desiredYaw = Mth.wrapDegrees((float)(Mth.atan2(f, d) * 57.2957763671875) - 90.0F);

            this.pitch = rotateTowards(this.pitch, desiredPitch, maxTurnSpeed);
            this.yaw = rotateTowards(this.yaw, desiredYaw, maxTurnSpeed);
        }

        private void moveTo(Vec3 moveToPos) {
            Vec3 offset2 = moveToPos.subtract(this.position);

            this.position = this.position.add(offset2);
        }

        private static float rotateTowards(float f, float g, float h) {
            float i = Mth.degreesDifference(f, g);
            float j = Mth.clamp(i, -h, h);
            return f + j;
        }

        private Vec3 getTailPos() {
            return Vec3.directionFromRotation(pitch, yaw).multiply(-this.length, -this.length, -this.length).add(this.position);
        }

        private CompoundTag toCompoundTag() {
            CompoundTag tag = new CompoundTag();
            tag.putFloat("length", this.length);

            tag.putDouble("x", position.x());
            tag.putDouble("y", position.y());
            tag.putDouble("z", position.z());

            tag.putFloat("pitch", this.pitch);
            tag.putFloat("yaw", this.yaw);
            return tag;
        }

        private static Segment fromCompoundTag(CompoundTag tag) {
            Vec3 position = new Vec3(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"));
            Segment segment = new Segment(tag.getFloat("length"), position);
            segment.pitch = tag.getFloat("pitch");
            segment.yaw = tag.getFloat("yaw");
            return segment;
        }
    }
}
