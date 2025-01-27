package whocraft.tardis_refined.mixin.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import whocraft.tardis_refined.common.capability.player.TardisPlayerInfo;

@Mixin(PlayerRenderer.class)
public class PlayerRenderMixin {

    @Inject(method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"), cancellable = true)
    private void render(AbstractClientPlayer abstractClientPlayer, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {

        TardisPlayerInfo.get(abstractClientPlayer).ifPresent(tardisPlayerInfo -> {
            if (tardisPlayerInfo.isViewingTardis()) {
                ci.cancel();
            }
        });

    }

}
