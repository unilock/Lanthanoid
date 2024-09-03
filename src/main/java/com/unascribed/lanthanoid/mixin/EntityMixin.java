package com.unascribed.lanthanoid.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.unascribed.lanthanoid.Lanthanoid;
import net.minecraft.entity.Entity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {
	@WrapOperation(method = "isInRangeToRenderDist", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;renderDistanceWeight:D", opcode = Opcodes.GETFIELD))
	private double renderDistanceWeight(Entity instance, Operation<Double> original) {
		return Lanthanoid.getDistanceWeight(original.call(instance));
	}
}
