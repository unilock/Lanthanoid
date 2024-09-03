package com.unascribed.lanthanoid.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.unascribed.lanthanoid.Lanthanoid;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(World.class)
public class WorldMixin {
	@WrapOperation(method = "getSkyColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldProvider;getSkyColor(Lnet/minecraft/entity/Entity;F)Lnet/minecraft/util/Vec3;"))
	private Vec3 getSkyColor(WorldProvider instance, Entity cameraEntity, float partialTicks, Operation<Vec3> original) {
		return Lanthanoid.modifySkyColor(original.call(instance, cameraEntity, partialTicks), cameraEntity, partialTicks);
	}
}
