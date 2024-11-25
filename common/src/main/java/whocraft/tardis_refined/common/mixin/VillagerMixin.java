package whocraft.tardis_refined.common.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.schedule.Activity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import whocraft.tardis_refined.common.capability.tardis.TardisLevelOperator;
import whocraft.tardis_refined.registry.TRVillagerProfession;

@Mixin(Villager.class)
public class VillagerMixin {

    @Inject(method = "tick()V", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo ci) {
        Villager villager = (Villager) (Object) this;

        if (villager.level() instanceof ServerLevel serverLevel) {
            TardisLevelOperator.get(serverLevel).ifPresent(tardisLevelOperator -> {

                if (tardisLevelOperator.getPilotingManager().isInFlight()) {
                    if (villager.getVillagerData().getProfession() == TRVillagerProfession.PILOT.get() && !villager.getBrain().isActive(Activity.WORK)) {
                        villager.getBrain().setDefaultActivity(Activity.WORK);
                        villager.getBrain().setActiveActivityIfPossible(Activity.WORK);
                    } else {
                        villager.getVillagerData().setProfession(TRVillagerProfession.PILOT.get());
                    }
                }
            });
        }
    }

}