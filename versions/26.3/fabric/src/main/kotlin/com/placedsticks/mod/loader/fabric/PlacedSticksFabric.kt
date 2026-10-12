package com.placedsticks.mod.loader.fabric

import com.placedsticks.mod.PlacedSticks
import com.placedsticks.mod.block.PlacedRods
import com.placedsticks.mod.block.RodBlock
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.material.MapColor

class PlacedSticksFabric : ModInitializer {

    override fun onInitialize() {
        PlacedSticks.LOGGER.info("Placed Sticks {} (fabric)", PlacedSticks.MOD_ID)
        val stick = register("stick", MapColor.WOOD, SoundType.WOOD, dropBamboo = false, half = 1.0, ignite = 5, burn = 20)
        val bamboo = register("bamboo", MapColor.PLANT, SoundType.BAMBOO, dropBamboo = true, half = 1.5, ignite = 60, burn = 60)
        PlacedRods.stick = stick
        PlacedRods.bamboo = bamboo
    }

    private fun register(
        name: String,
        color: MapColor,
        sound: SoundType,
        dropBamboo: Boolean,
        half: Double,
        ignite: Int,
        burn: Int,
    ): RodBlock {
        val id = Identifier.fromNamespaceAndPath(PlacedSticks.MOD_ID, name)
        val properties = RodBlock.rodProps(color, sound).setId(ResourceKey.create(Registries.BLOCK, id))
        val block = RodBlock(properties, dropBamboo, half)
        Registry.register(BuiltInRegistries.BLOCK, id, block)
        FlammableBlockRegistry.getDefaultInstance().add(block, ignite, burn)
        return block
    }
}
