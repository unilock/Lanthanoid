package com.unascribed.lanthanoid.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.unascribed.lanthanoid.Lanthanoid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.Set;

@Mixin(EntityTrackerEntry.class)
public class EntityTrackerEntryMixin {
	@Shadow
	public Entity myEntity;

	@Inject(method = "tryStartWatchingThis", at = @At("HEAD"))
	private void head(EntityPlayerMP p_73117_1_, @Share("shouldForceTracking") LocalBooleanRef shouldForceTracking) {
		shouldForceTracking.set(Lanthanoid.forceTrackingFor(p_73117_1_, this.myEntity));
	}

	@Definition(id = "blocksDistanceThreshold", field = "Lnet/minecraft/entity/EntityTrackerEntry;blocksDistanceThreshold:I")
	@Definition(id = "d0", type = double.class, local = @Local(ordinal = 0))
	@Expression("d0 >= -this.blocksDistanceThreshold")
	@WrapOperation(method = "tryStartWatchingThis", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean checkA(boolean original, EntityPlayerMP p_73117_1_, @Share("shouldForceTracking") LocalBooleanRef shouldForceTracking) {
		return shouldForceTracking.get() || original;
	}
	@Definition(id = "blocksDistanceThreshold", field = "Lnet/minecraft/entity/EntityTrackerEntry;blocksDistanceThreshold:I")
	@Definition(id = "d0", type = double.class, local = @Local(ordinal = 0))
	@Expression("d0 <= this.blocksDistanceThreshold")
	@WrapOperation(method = "tryStartWatchingThis", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean checkB(boolean original, EntityPlayerMP p_73117_1_, @Share("shouldForceTracking") LocalBooleanRef shouldForceTracking) {
		return shouldForceTracking.get() || original;
	}
	@Definition(id = "blocksDistanceThreshold", field = "Lnet/minecraft/entity/EntityTrackerEntry;blocksDistanceThreshold:I")
	@Definition(id = "d1", type = double.class, local = @Local(ordinal = 1))
	@Expression("d1 >= -this.blocksDistanceThreshold")
	@WrapOperation(method = "tryStartWatchingThis", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean checkC(boolean original, EntityPlayerMP p_73117_1_, @Share("shouldForceTracking") LocalBooleanRef shouldForceTracking) {
		return shouldForceTracking.get() || original;
	}
	@Definition(id = "blocksDistanceThreshold", field = "Lnet/minecraft/entity/EntityTrackerEntry;blocksDistanceThreshold:I")
	@Definition(id = "d1", type = double.class, local = @Local(ordinal = 1))
	@Expression("d1 <= this.blocksDistanceThreshold")
	@WrapOperation(method = "tryStartWatchingThis", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean checkD(boolean original, EntityPlayerMP p_73117_1_, @Share("shouldForceTracking") LocalBooleanRef shouldForceTracking) {
		return shouldForceTracking.get() || original;
	}

	@WrapOperation(method = "tryStartWachingThis", at = @At(value = "INVOKE", target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z", ordinal = 1))
	private boolean contains(Set instance, Object o, Operation<Boolean> original, @Share("shouldForceTracking") LocalBooleanRef shouldForceTracking) {
		if (shouldForceTracking.get()) {
			return true;
		}
		return original.call(instance, o);
	}
}
