package com.placedsticks.mod

import com.placedsticks.mod.block.ModBlocks
import com.placedsticks.mod.event.GuideHandler
import com.placedsticks.mod.event.RodPlacement
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.common.NeoForge
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(PlacedSticksMod.MOD_ID)
class PlacedSticksMod(modEventBus: IEventBus, modContainer: ModContainer) {

    companion object {
        const val MOD_ID = "placedsticks"

        @JvmField
        val LOGGER: Logger = LogManager.getLogger(MOD_ID)
    }

    init {
        LOGGER.info("Placed Sticks {} ({})", modContainer.modInfo.version, MOD_ID)
        ModBlocks.register(modEventBus)
        modEventBus.addListener { _: FMLCommonSetupEvent -> ModBlocks.onCommonSetup() }
        NeoForge.EVENT_BUS.register(RodPlacement)
        NeoForge.EVENT_BUS.register(GuideHandler)
    }
}
