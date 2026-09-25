package com.placedsticks.mod.block

import com.placedsticks.mod.PlacedSticksMod
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FireBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.material.PushReaction
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModBlocks {

    val BLOCKS: DeferredRegister.Blocks = DeferredRegister.createBlocks(PlacedSticksMod.MOD_ID)

    val STICK: DeferredBlock<RodBlock> = BLOCKS.register(
        "stick",
        Supplier {
            RodBlock(
                props(MapColor.WOOD, SoundType.WOOD),
                dropBamboo = false,
                half = 1.0,
            )
        },
    )

    val BAMBOO: DeferredBlock<RodBlock> = BLOCKS.register(
        "bamboo",
        Supplier {
            RodBlock(
                props(MapColor.PLANT, SoundType.BAMBOO),
                dropBamboo = true,
                half = 1.5,
            )
        },
    )

    fun register(modBus: IEventBus) {
        BLOCKS.register(modBus)
    }

    fun onCommonSetup() {
        val fire = Blocks.FIRE as FireBlock
        fire.setFlammable(STICK.get(), 5, 20)
        fire.setFlammable(BAMBOO.get(), 60, 60)
    }

    private fun props(color: MapColor, sound: SoundType): BlockBehaviour.Properties =
        BlockBehaviour.Properties.of()
            .mapColor(color)
            .sound(sound)
            .strength(0.4f)
            .noOcclusion()
            .pushReaction(PushReaction.DESTROY)
            .isRedstoneConductor { _, _, _ -> false }
            .isSuffocating { _, _, _ -> false }
            .isViewBlocking { _, _, _ -> false }
}
