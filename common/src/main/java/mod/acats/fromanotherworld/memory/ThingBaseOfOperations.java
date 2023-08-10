package mod.acats.fromanotherworld.memory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;

public class ThingBaseOfOperations {
    public final AIDirector director;
    private final GlobalThingMemory memory;
    private final int x;
    private final int y;
    private final int z;
    private int size = 32;
    ThingBaseOfOperations(int x, int y, int z, GlobalThingMemory memory) {
        this.director = new AIDirector();
        this.memory = memory;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    static ThingBaseOfOperations fromNBT(CompoundTag tag, GlobalThingMemory memory) {
        ThingBaseOfOperations base = new ThingBaseOfOperations(tag.getInt("X"), tag.getInt("Y"), tag.getInt("Z"), memory);
        base.size = tag.getInt("Size");

        CompoundTag dir = tag.getCompound("Director");
        base.director.setFromNBT(dir);

        return base;
    }

    CompoundTag toNBT(CompoundTag tag, int num) {
        CompoundTag baseTag = new CompoundTag();
        baseTag.putInt("X", this.x);
        baseTag.putInt("Y", this.y);
        baseTag.putInt("Z", this.z);
        baseTag.putInt("Size", this.size);
        baseTag.put("Director", this.director.toNBT());
        tag.put("Base" + num, baseTag);
        return tag;
    }

    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    public int getZ() {
        return this.z;
    }
    public int getSize() {
        return this.size;
    }

    public long sqDist(int x, int y, int z) {
        return Mth.square((long) this.x - x) +
                Mth.square((long) this.y - y) +
                Mth.square((long) this.z - z);
    }

    public int sqSize() {
        return Mth.square(this.size);
    }

    public class AIDirector {
        private int threatLevel = 0;
        private int assimilationSuccess = 0;
        private void setFromNBT(CompoundTag tag) {
            this.threatLevel = tag.getInt("ThreatLevel");
            this.assimilationSuccess = tag.getInt("AssimilationSuccess");
        }
        private CompoundTag toNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putInt("ThreatLevel", this.threatLevel);
            tag.putInt("AssimilationSuccess", this.assimilationSuccess);
            return tag;
        }

        public Aggression getAggression() {
            if (this.threatLevel < 11) {
                return Aggression.NORMAL;
            }
            else if (this.threatLevel < 31) {
                return Aggression.HIDING;
            }
            return Aggression.AGGRESSIVE;
        }

        public Hunger getHunger() {
            if (this.assimilationSuccess == 0) {
                return Hunger.VERY_HUNGRY;
            }
            else if (this.assimilationSuccess < 5) {
                return Hunger.HUNGRY;
            }
            else if (this.assimilationSuccess > 14) {
                return Hunger.SATISFIED;
            }
            return Hunger.NORMAL;
        }

        public void threaten() {
            this.threatLevel++;
            memory.setDirty();
        }

        public void successfulAssimilation() {
            this.assimilationSuccess++;
            memory.setDirty();
        }

        void tick() {
            if (this.threatLevel > 0 && memory.level.getRandom().nextInt(8) == 0) {
                this.threatLevel--;
                memory.setDirty();
            }
            if (memory.level.getRandom().nextInt(2) == 0) {
                this.assimilationSuccess = Math.max(0, (int) (this.assimilationSuccess * 0.95F) - 1);
            }
        }
    }
}
