package whocraft.tardis_refined.common.mixin;

import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import whocraft.tardis_refined.common.capability.player.TardisPlayerInfo;
import whocraft.tardis_refined.common.network.messages.player.ExitTardisViewMessage;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

    @Shadow
    public Input input;

    @Inject(method = "aiStep()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getTutorial()Lnet/minecraft/client/tutorial/Tutorial;"))
    private void inputEdit(CallbackInfo ci) {
        LocalPlayer localPlayer = (LocalPlayer) (Object) this;
        handleInput(localPlayer, input);
    }

    private void handleInput(LocalPlayer localPlayer, Input input) {

        TardisPlayerInfo.get(localPlayer).ifPresent(tardisPlayerInfo -> {
            if(tardisPlayerInfo.isViewingTardis()){
                blockMovement(input);
            }
        });

    }

    private static void blockMovement(Input moveType) {
        // Set all movement-related fields to false or 0.0F to block movement

        if(moveType.jumping){
            new ExitTardisViewMessage().send();
            return;
        }

        moveType.right = false;
        moveType.left = false;
        moveType.down = false;
        moveType.jumping = false;
        moveType.forwardImpulse = 0.0F;
        moveType.shiftKeyDown = false;
        moveType.leftImpulse = 0.0F;
    }

}