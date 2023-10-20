package mod.acats.fromanotherworld.block.spreading;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.acats.fromanotherworld.block.AssimilatedSculkVeinBlock;
import mod.acats.fromanotherworld.block.interfaces.AssimilatedSculkBehaviour;
import mod.acats.fromanotherworld.tags.BlockTags;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.*;
import java.util.stream.Stream;

// Copied from vanilla's sculk spreader. I don't understand most of it, but I understand enough to make it do what I need it to do.

public class AssimilatedSculkSpreader {
    public static final String TAG_NAME = "AssimilatedSculkSpreader";
    public static final int MAX_GROWTH_RATE_RADIUS = 24;
    public static final int MAX_CHARGE = 1000;
    public static final float MAX_DECAY_FACTOR = 0.5F;
    private static final int MAX_CURSORS = 32;
    final boolean isWorldGeneration;
    private final TagKey<Block> replaceableBlocks;
    private final int growthSpawnCost;
    private final int noGrowthRadius;
    private final int chargeDecayRate;
    private final int additionalDecayRate;
    private List<AssimilatedChargeCursor> cursors = new ArrayList<>();
    private static final Logger LOGGER = LogUtils.getLogger();

    public AssimilatedSculkSpreader(boolean isWorldGeneration,
                                    TagKey<Block> replaceableBlocks,
                                    int growthSpawnCost,
                                    int noGrowthRadius,
                                    int chargeDecayRate,
                                    int additionalDecayRate) {
        this.isWorldGeneration = isWorldGeneration;
        this.replaceableBlocks = replaceableBlocks;
        this.growthSpawnCost = growthSpawnCost;
        this.noGrowthRadius = noGrowthRadius;
        this.chargeDecayRate = chargeDecayRate;
        this.additionalDecayRate = additionalDecayRate;
    }

    public static AssimilatedSculkSpreader create() {
        return new AssimilatedSculkSpreader(false, BlockTags.ASSIMILATED_SCULK_REPLACEABLE, 10, 4, 10, 5);
    }

    public TagKey<Block> replaceableBlocks() {
        return this.replaceableBlocks;
    }

    public int growthSpawnCost() {
        return this.growthSpawnCost;
    }

    public int noGrowthRadius() {
        return this.noGrowthRadius;
    }

    public int chargeDecayRate() {
        return this.chargeDecayRate;
    }

    public int additionalDecayRate() {
        return this.additionalDecayRate;
    }

    public boolean isWorldGeneration() {
        return this.isWorldGeneration;
    }

    public List<AssimilatedChargeCursor> getCursors() {
        return this.cursors;
    }

    public void clear() {
        this.cursors.clear();
    }

    public void loadFrom(CompoundTag tag) {
        if (tag.contains(TAG_NAME)) {
            this.load(tag.getCompound(TAG_NAME));
        }
    }

    private void load(CompoundTag compoundTag) {
        if (compoundTag.contains("cursors", 9)) {
            this.cursors.clear();
            DataResult<List<AssimilatedChargeCursor>> var10000 = AssimilatedChargeCursor.CODEC.listOf().parse(new Dynamic<>(NbtOps.INSTANCE, compoundTag.getList("cursors", 10)));
            Logger var10001 = LOGGER;
            Objects.requireNonNull(var10001);
            List<AssimilatedChargeCursor> list = var10000.resultOrPartial(var10001::error).orElseGet(ArrayList::new);
            int i = Math.min(list.size(), MAX_CURSORS);

            for(int j = 0; j < i; ++j) {
                this.addCursor(list.get(j));
            }
        }

    }

    public void saveTo(CompoundTag tag) {
        CompoundTag compoundTag = new CompoundTag();
        this.save(compoundTag);
        tag.put(TAG_NAME, compoundTag);
    }

    private void save(CompoundTag compoundTag) {
        DataResult<Tag> var10000 = AssimilatedChargeCursor.CODEC.listOf().encodeStart(NbtOps.INSTANCE, this.cursors);
        Logger var10001 = LOGGER;
        Objects.requireNonNull(var10001);
        var10000.resultOrPartial(var10001::error).ifPresent((tag) -> {
            compoundTag.put("cursors", tag);
        });
    }

    public void addCursors(BlockPos blockPos, int i) {
        while(i > 0) {
            int j = Math.min(i, MAX_CHARGE);
            this.addCursor(new AssimilatedChargeCursor(blockPos, j));
            i -= j;
        }

    }

    private void addCursor(AssimilatedChargeCursor chargeCursor) {
        if (this.cursors.size() < MAX_CURSORS) {
            this.cursors.add(chargeCursor);
        }
    }

    public void updateCursors(LevelAccessor levelAccessor, BlockPos blockPos, RandomSource randomSource, boolean bl) {
        if (!this.cursors.isEmpty()) {
            List<AssimilatedChargeCursor> list = new ArrayList<>();
            Map<BlockPos, AssimilatedChargeCursor> map = new HashMap<>();
            Object2IntMap<BlockPos> object2IntMap = new Object2IntOpenHashMap<>();
            Iterator<AssimilatedChargeCursor> var8 = this.cursors.iterator();

            BlockPos blockPos2;
            while(var8.hasNext()) {
                AssimilatedChargeCursor chargeCursor = var8.next();
                chargeCursor.update(levelAccessor, blockPos, randomSource, this, bl);
                if (chargeCursor.charge <= 0) {
                    levelAccessor.levelEvent(3006, chargeCursor.getPos(), 0);
                } else {
                    blockPos2 = chargeCursor.getPos();
                    object2IntMap.computeInt(blockPos2, (blockPosx, integer) -> (integer == null ? 0 : integer) + chargeCursor.charge);
                    AssimilatedChargeCursor chargeCursor2 = map.get(blockPos2);
                    if (chargeCursor2 == null) {
                        map.put(blockPos2, chargeCursor);
                        list.add(chargeCursor);
                    } else if (!this.isWorldGeneration() && chargeCursor.charge + chargeCursor2.charge <= MAX_CHARGE) {
                        chargeCursor2.mergeWith(chargeCursor);
                    } else {
                        list.add(chargeCursor);
                        if (chargeCursor.charge < chargeCursor2.charge) {
                            map.put(blockPos2, chargeCursor);
                        }
                    }
                }
            }

            for (Object2IntMap.Entry<BlockPos> blockPosEntry : object2IntMap.object2IntEntrySet()) {
                blockPos2 = blockPosEntry.getKey();
                int i = blockPosEntry.getIntValue();
                AssimilatedChargeCursor chargeCursor3 = map.get(blockPos2);
                Collection<Direction> collection = chargeCursor3 == null ? null : chargeCursor3.getFacingData();
                if (i > 0 && collection != null) {
                    int j = (int) (Math.log1p(i) / 2.299999952316284) + 1;
                    int k = (j << 6) + MultifaceBlock.pack(collection);
                    levelAccessor.levelEvent(3006, blockPos2, k);
                }
            }

            this.cursors = list;
        }
    }

    public static class AssimilatedChargeCursor {
        private static final ObjectArrayList<Vec3i> NON_CORNER_NEIGHBOURS = Util.make(new ObjectArrayList<>(18), (objectArrayList) -> {
            Stream<BlockPos> var10000 = BlockPos.betweenClosedStream(new BlockPos(-1, -1, -1), new BlockPos(1, 1, 1)).filter((blockPos) ->
                    (blockPos.getX() == 0 || blockPos.getY() == 0 || blockPos.getZ() == 0) && !blockPos.equals(BlockPos.ZERO)).map(BlockPos::immutable);
            Objects.requireNonNull(objectArrayList);
            var10000.forEach(objectArrayList::add);
        });
        private BlockPos pos;
        int charge;
        private int updateDelay;
        private int decayDelay;
        @Nullable
        private Set<Direction> facings;
        private static final Codec<Set<Direction>> DIRECTION_SET;
        public static final Codec<AssimilatedChargeCursor> CODEC;

        private AssimilatedChargeCursor(BlockPos blockPos, int i, int j, int k, Optional<Set<Direction>> optional) {
            this.pos = blockPos;
            this.charge = i;
            this.decayDelay = j;
            this.updateDelay = k;
            this.facings = optional.orElse(null);
        }

        public AssimilatedChargeCursor(BlockPos blockPos, int i) {
            this(blockPos, i, 1, 0, Optional.empty());
        }

        public BlockPos getPos() {
            return this.pos;
        }

        public int getCharge() {
            return this.charge;
        }

        public int getDecayDelay() {
            return this.decayDelay;
        }

        @Nullable
        public Set<Direction> getFacingData() {
            return this.facings;
        }

        private boolean shouldUpdate(LevelAccessor levelAccessor, BlockPos blockPos, boolean bl) {
            if (this.charge <= 0) {
                return false;
            } else if (bl) {
                return true;
            } else if (levelAccessor instanceof ServerLevel serverLevel) {
                return serverLevel.shouldTickBlocksAt(blockPos);
            } else {
                return false;
            }
        }

        public void update(LevelAccessor levelAccessor, BlockPos blockPos, RandomSource randomSource, AssimilatedSculkSpreader sculkSpreader, boolean bl) {
            if (this.shouldUpdate(levelAccessor, blockPos, sculkSpreader.isWorldGeneration)) {
                if (this.updateDelay > 0) {
                    --this.updateDelay;
                } else {
                    BlockState blockState = levelAccessor.getBlockState(this.pos);
                    AssimilatedSculkBehaviour sculkBehaviour = getBlockBehaviour(blockState);
                    if (bl && sculkBehaviour.attemptAssimilatedSpreadVein(levelAccessor, this.pos, blockState, this.facings, sculkSpreader.isWorldGeneration())) {
                        if (sculkBehaviour.canAssimilatedChangeBlockStateOnSpread()) {
                            blockState = levelAccessor.getBlockState(this.pos);
                            sculkBehaviour = getBlockBehaviour(blockState);
                        }

                        levelAccessor.playSound(null, this.pos, SoundEvents.SCULK_BLOCK_SPREAD, SoundSource.BLOCKS, 1.0F, 1.0F);
                    }

                    this.charge = sculkBehaviour.attemptAssimilatedUseCharge(this, levelAccessor, blockPos, randomSource, sculkSpreader, bl);
                    if (this.charge <= 0) {
                        sculkBehaviour.onAssimilatedDischarged(levelAccessor, blockState, this.pos, randomSource);
                    } else {
                        BlockPos blockPos2 = getValidMovementPos(levelAccessor, this.pos, randomSource);
                        if (blockPos2 != null) {
                            sculkBehaviour.onAssimilatedDischarged(levelAccessor, blockState, this.pos, randomSource);
                            this.pos = blockPos2.immutable();
                            if (sculkSpreader.isWorldGeneration() && !this.pos.closerThan(new Vec3i(blockPos.getX(), this.pos.getY(), blockPos.getZ()), 15.0)) {
                                this.charge = 0;
                                return;
                            }

                            blockState = levelAccessor.getBlockState(blockPos2);
                        }

                        if (blockState.getBlock() instanceof SculkBehaviour) {
                            this.facings = MultifaceBlock.availableFaces(blockState);
                        }

                        this.decayDelay = sculkBehaviour.updateAssimilatedDecayDelay(this.decayDelay);
                        this.updateDelay = sculkBehaviour.getAssimilatedSculkSpreadDelay();
                    }
                }
            }
        }

        void mergeWith(AssimilatedChargeCursor chargeCursor) {
            this.charge += chargeCursor.charge;
            chargeCursor.charge = 0;
            this.updateDelay = Math.min(this.updateDelay, chargeCursor.updateDelay);
        }

        private static AssimilatedSculkBehaviour getBlockBehaviour(BlockState blockState) {
            Block var2 = blockState.getBlock();
            AssimilatedSculkBehaviour var10000;
            if (var2 instanceof AssimilatedSculkBehaviour sculkBehaviour) {
                var10000 = sculkBehaviour;
            } else {
                var10000 = AssimilatedSculkBehaviour.DEFAULT;
            }

            return var10000;
        }

        private static List<Vec3i> getRandomizedNonCornerNeighbourOffsets(RandomSource randomSource) {
            return Util.shuffledCopy(NON_CORNER_NEIGHBOURS, randomSource);
        }

        @Nullable
        private static BlockPos getValidMovementPos(LevelAccessor levelAccessor, BlockPos blockPos, RandomSource randomSource) {
            BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();
            BlockPos.MutableBlockPos mutableBlockPos2 = blockPos.mutable();

            for (Vec3i vec3i : getRandomizedNonCornerNeighbourOffsets(randomSource)) {
                mutableBlockPos2.setWithOffset(blockPos, vec3i);
                BlockState blockState = levelAccessor.getBlockState(mutableBlockPos2);
                if (blockState.getBlock() instanceof SculkBehaviour && isMovementUnobstructed(levelAccessor, blockPos, mutableBlockPos2)) {
                    mutableBlockPos.set(mutableBlockPos2);
                    if (AssimilatedSculkVeinBlock.hasAssimilatedSubstrateAccess(levelAccessor, blockState, mutableBlockPos2)) {
                        break;
                    }
                }
            }

            return mutableBlockPos.equals(blockPos) ? null : mutableBlockPos;
        }

        private static boolean isMovementUnobstructed(LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
            if (blockPos.distManhattan(blockPos2) == 1) {
                return true;
            } else {
                BlockPos blockPos3 = blockPos2.subtract(blockPos);
                Direction direction = Direction.fromAxisAndDirection(Direction.Axis.X, blockPos3.getX() < 0 ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE);
                Direction direction2 = Direction.fromAxisAndDirection(Direction.Axis.Y, blockPos3.getY() < 0 ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE);
                Direction direction3 = Direction.fromAxisAndDirection(Direction.Axis.Z, blockPos3.getZ() < 0 ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE);
                if (blockPos3.getX() == 0) {
                    return isUnobstructed(levelAccessor, blockPos, direction2) || isUnobstructed(levelAccessor, blockPos, direction3);
                } else if (blockPos3.getY() == 0) {
                    return isUnobstructed(levelAccessor, blockPos, direction) || isUnobstructed(levelAccessor, blockPos, direction3);
                } else {
                    return isUnobstructed(levelAccessor, blockPos, direction) || isUnobstructed(levelAccessor, blockPos, direction2);
                }
            }
        }

        private static boolean isUnobstructed(LevelAccessor levelAccessor, BlockPos blockPos, Direction direction) {
            BlockPos blockPos2 = blockPos.relative(direction);
            return !levelAccessor.getBlockState(blockPos2).isFaceSturdy(levelAccessor, blockPos2, direction.getOpposite());
        }

        static {
            DIRECTION_SET = Direction.CODEC.listOf().xmap((list) -> Sets.newEnumSet(list, Direction.class), Lists::newArrayList);
            CODEC = RecordCodecBuilder.create((instance) ->
                    instance.group(BlockPos.CODEC.fieldOf("pos").forGetter(AssimilatedChargeCursor::getPos), Codec.intRange(0, 1000).fieldOf("charge").orElse(0).forGetter(AssimilatedChargeCursor::getCharge), Codec.intRange(0, 1).fieldOf("decay_delay").orElse(1).forGetter(AssimilatedChargeCursor::getDecayDelay), Codec.intRange(0, Integer.MAX_VALUE).fieldOf("update_delay").orElse(0).forGetter((chargeCursor) -> chargeCursor.updateDelay), DIRECTION_SET.optionalFieldOf("facings").forGetter((chargeCursor) -> Optional.ofNullable(chargeCursor.getFacingData()))).apply(instance, AssimilatedChargeCursor::new));
        }
    }
}
