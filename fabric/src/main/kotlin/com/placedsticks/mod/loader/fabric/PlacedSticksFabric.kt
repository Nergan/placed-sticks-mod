package com.placedsticks.mod.loader.fabric

import com.placedsticks.mod.PlacedSticks
import com.placedsticks.mod.block.PlacedRods
import com.placedsticks.mod.block.RodBlock
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation

class PlacedSticksFabric : ModInitializer {

    override fun onInitialize() {
        PlacedSticks.LOGGER.info("Placed Sticks {} (fabric)", PlacedSticks.MOD_ID)
        val stick = register("stick", RodBlock.createStick())
        val bamboo = register("bamboo", RodBlock.createBamboo())
        PlacedRods.stick = stick
        PlacedRods.bamboo = bamboo
        FlammableBlockRegistry.getDefaultInstance().add(stick, 5, 20)
        FlammableBlockRegistry.getDefaultInstance().add(bamboo, 60, 60)
    }

    private fun register(name: String, block: RodBlock): RodBlock {
        val id = ResourceLocation.fromNamespaceAndPath(PlacedSticks.MOD_ID, name)
        return Registry.register(BuiltInRegistries.BLOCK, id, block)
    }
}
