package com.placedsticks.mod.block

import com.placedsticks.mod.PlacedSticks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Function
import java.util.function.Supplier

object ModBlocks {

    val BLOCKS: DeferredRegister.Blocks = DeferredRegister.createBlocks(PlacedSticks.MOD_ID)

    val STICK: DeferredBlock<RodBlock> = BLOCKS.registerBlock(
        "stick",
        Function { properties: BlockBehaviour.Properties ->
            NeoRodBlock(properties, dropBamboo = false, half = 1.0, igniteOdds = 5, burnOdds = 20)
        },
        Supplier { RodBlock.rodProps(MapColor.WOOD, SoundType.WOOD) },
    )

    val BAMBOO: DeferredBlock<RodBlock> = BLOCKS.registerBlock(
        "bamboo",
        Function { properties: BlockBehaviour.Properties ->
            NeoRodBlock(properties, dropBamboo = true, half = 1.5, igniteOdds = 60, burnOdds = 60)
        },
        Supplier { RodBlock.rodProps(MapColor.PLANT, SoundType.BAMBOO) },
    )

    fun register(modBus: IEventBus) {
        BLOCKS.register(modBus)
    }

    fun onCommonSetup() {
        PlacedRods.stick = STICK.get()
        PlacedRods.bamboo = BAMBOO.get()
    }
}
