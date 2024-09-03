package com.unascribed.lanthanoid.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.unascribed.lanthanoid.Lanthanoid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.entity.player.EntityPlayerMP;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Set;

/**
 * Very hacky mixin while @Expression is in beta
 */
@Mixin(EntityTrackerEntry.class)
public class EntityTrackerEntryMixin {
	@Shadow
	public Entity myEntity;

	@ModifyVariable(method = "tryStartWachingThis", at = @At("STORE"), ordinal = 0)
	private double d0(double value, @Local(argsOnly = true) EntityPlayerMP p_73117_1_, @Share("lanthanoid") LocalBooleanRef lanthanoid) {
		if (Lanthanoid.forceTrackingFor(p_73117_1_, this.myEntity)) {
			lanthanoid.set(true);
			return -1;
		} else {
			lanthanoid.set(false);
		}
		return value;
	}

	@WrapOperation(method = "tryStartWachingThis", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/EntityTrackerEntry;blocksDistanceThreshold:I", opcode = Opcodes.GETFIELD, ordinal = 0))
	private int blocksDistanceThreshold(EntityTrackerEntry instance, Operation<Integer> original, @Share("lanthanoid") LocalBooleanRef lanthanoid) {
		if (lanthanoid.get()) {
			return 0;
		}
		return original.call(instance);
	}

	@WrapOperation(method = "tryStartWachingThis", at = @At(value = "INVOKE", target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z", ordinal = 1))
	private boolean contains(Set instance, Object o, Operation<Boolean> original, @Share("lanthanoid") LocalBooleanRef lanthanoid) {
		if (lanthanoid.get()) {
			return true;
		}
		return original.call(instance, o);
	}
}
