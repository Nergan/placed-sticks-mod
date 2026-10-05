package com.placedsticks.mod.block

import com.placedsticks.mod.util.RodAxes
import com.placedsticks.mod.util.RodAxis
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.material.PushReaction
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import net.minecraft.tags.BlockTags

class RodBlock(
    properties: Properties,
    private val dropBamboo: Boolean,
    private val half: Double,
) : Block(properties) {

    init {
        registerDefaultState(
            stateDefinition.any()
                .setValue(AXIS_X, false)
                .setValue(AXIS_Y, false)
                .setValue(AXIS_Z, false),
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(AXIS_X, AXIS_Y, AXIS_Z)
    }

    override fun getShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
    ): VoxelShape = shapeFor(state)

    override fun getCollisionShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
    ): VoxelShape = shapeFor(state)

    override fun getCloneItemStack(level: LevelReader, pos: BlockPos, state: BlockState): ItemStack =
        ItemStack(if (dropBamboo) Items.BAMBOO else Items.STICK)

    override fun propagatesSkylightDown(state: BlockState, level: BlockGetter, pos: BlockPos): Boolean = true

    fun axesOf(state: BlockState): RodAxes = RodAxes(
        x = state.getValue(AXIS_X),
        y = state.getValue(AXIS_Y),
        z = state.getValue(AXIS_Z),
    )

    fun withAxes(axes: RodAxes): BlockState = defaultBlockState()
        .setValue(AXIS_X, axes.x)
        .setValue(AXIS_Y, axes.y)
        .setValue(AXIS_Z, axes.z)

    private fun shapeFor(state: BlockState): VoxelShape {
        var shape = Shapes.empty()
        if (state.getValue(AXIS_X)) shape = Shapes.or(shape, alongX)
        if (state.getValue(AXIS_Y)) shape = Shapes.or(shape, alongY)
        if (state.getValue(AXIS_Z)) shape = Shapes.or(shape, alongZ)
        return shape
    }

    private val alongX: VoxelShape = box(0.0, 8.0 - half, 8.0 - half, 16.0, 8.0 + half, 8.0 + half)
    private val alongY: VoxelShape = box(8.0 - half, 0.0, 8.0 - half, 8.0 + half, 16.0, 8.0 + half)
    private val alongZ: VoxelShape = box(8.0 - half, 8.0 - half, 0.0, 8.0 + half, 8.0 + half, 16.0)

    companion object {
        val AXIS_X: BooleanProperty = BooleanProperty.create("axis_x")
        val AXIS_Y: BooleanProperty = BooleanProperty.create("axis_y")
        val AXIS_Z: BooleanProperty = BooleanProperty.create("axis_z")

        fun axisOf(direction: Direction): RodAxis = when (direction.axis) {
            Direction.Axis.X -> RodAxis.X
            Direction.Axis.Y -> RodAxis.Y
            Direction.Axis.Z -> RodAxis.Z
        }

        fun isReplaceable(state: BlockState, context: BlockPlaceContext): Boolean {
            if (state.block is RodBlock) return false
            return state.canBeReplaced(context)
        }

        fun belowSupportsVanillaBamboo(state: BlockState): Boolean =
            state.`is`(BlockTags.BAMBOO_PLANTABLE_ON) || state.`is`(Blocks.BAMBOO)

        fun createStick(): RodBlock = RodBlock(rodProps(MapColor.WOOD, SoundType.WOOD), dropBamboo = false, half = 1.0)

        fun createBamboo(): RodBlock = RodBlock(rodProps(MapColor.PLANT, SoundType.BAMBOO), dropBamboo = true, half = 1.5)

        private fun rodProps(color: MapColor, sound: SoundType): Properties =
            Properties.of()
                .mapColor(color)
                .sound(sound)
                .strength(0.4f)
                .noOcclusion()
                .pushReaction(PushReaction.DESTROY)
                .isRedstoneConductor { _, _, _ -> false }
                .isSuffocating { _, _, _ -> false }
                .isViewBlocking { _, _, _ -> false }
    }
}
