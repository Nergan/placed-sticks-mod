package com.placedsticks.mod.event

import com.placedsticks.mod.PlacedSticksMod
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.ModList
import net.neoforged.neoforge.event.entity.player.PlayerEvent

object GuideHandler {

    const val BOOK_ID = "placedsticks:field_notes"
    private const val TAG_RECEIVED = "placedsticks.received_guide"

    fun isPatchouliLoaded(): Boolean = ModList.get().isLoaded("patchouli")

    @SubscribeEvent
    fun onPlayerLogin(event: PlayerEvent.PlayerLoggedInEvent) {
        val player = event.entity
        if (player.level().isClientSide) return
        if (!isPatchouliLoaded()) return
        if (player.persistentData.getBoolean(TAG_RECEIVED)) return

        val book = createBookStack() ?: return
        if (!player.addItem(book)) {
            player.drop(book, false)
        }
        player.persistentData.putBoolean(TAG_RECEIVED, true)
    }

    fun createBookStack(): ItemStack? {
        if (!isPatchouliLoaded()) return null
        val item = BuiltInRegistries.ITEM.get(ResourceLocation.parse("patchouli:guide_book"))
        if (item === Items.AIR) return null
        val stack = ItemStack(item)
        val componentType = BuiltInRegistries.DATA_COMPONENT_TYPE.get(ResourceLocation.parse("patchouli:book"))
            ?: return null
        val bookId = ResourceLocation.parse(BOOK_ID)
        return try {
            @Suppress("UNCHECKED_CAST")
            stack.set(componentType as DataComponentType<Any>, bookId)
            stack
        } catch (_: Throwable) {
            try {
                @Suppress("UNCHECKED_CAST")
                stack.set(componentType as DataComponentType<Any>, BOOK_ID)
                stack
            } catch (error: Throwable) {
                PlacedSticksMod.LOGGER.warn("Не удалось создать книгу Patchouli.", error)
                null
            }
        }
    }
}
