package com.placedsticks.mod.loader.fabric.mixin;

import com.placedsticks.mod.block.RodPlacementAction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** После блока и до собственного использования предмета — тот же момент, что ITEM_AFTER_BLOCK на NeoForge. */
@Mixin(ItemStack.class)
public abstract class StickPlaceMixin {

    @Inject(
            method = "useOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/Item;useOn(Lnet/minecraft/world/item/context/UseOnContext;)Lnet/minecraft/world/InteractionResult;"
            ),
            cancellable = true
    )
    private void afterBlock(UseOnContext context, CallbackInfoReturnable<InteractionResult> callback) {
        InteractionResult result = RodPlacementAction.INSTANCE.tryPlace(context);
        if (result != null) {
            callback.setReturnValue(result);
        }
    }
}
