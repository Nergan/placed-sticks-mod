package com.placedsticks.mod.block

import com.placedsticks.mod.PlacedSticks
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FireBlock
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModBlocks {

    val BLOCKS: DeferredRegister.Blocks = DeferredRegister.createBlocks(PlacedSticks.MOD_ID)

    val STICK: DeferredBlock<RodBlock> = BLOCKS.register("stick", Supplier { RodBlock.createStick() })

    val BAMBOO: DeferredBlock<RodBlock> = BLOCKS.register("bamboo", Supplier { RodBlock.createBamboo() })

    fun register(modBus: IEventBus) {
        BLOCKS.register(modBus)
    }

    fun onCommonSetup() {
        PlacedRods.stick = STICK.get()
        PlacedRods.bamboo = BAMBOO.get()
        val fire = Blocks.FIRE as FireBlock
        fire.setFlammable(PlacedRods.stick, 5, 20)
        fire.setFlammable(PlacedRods.bamboo, 60, 60)
    }
}
