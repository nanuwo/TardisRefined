package whocraft.tardis_refined.compat.jei;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import whocraft.tardis_refined.common.crafting.astral_manipulator.ManipulatorCraftingIngredient;
import whocraft.tardis_refined.common.crafting.astral_manipulator.ManipulatorCraftingRecipe;
import whocraft.tardis_refined.constants.ModMessages;

/**
 * Used to render Astral Manipulator recipes through a "viewing window".
 * This class does not handle drawing of a border for this window.
 */
public class ManipulatorRecipeRenderer {

    private final Minecraft minecraft = Minecraft.getInstance();

    // Measurements of the viewing window
    private final int xOffset;
    private final int yOffset;
    private final int width;
    private final int height;

    private final Button addLayersButton;
    private final Button removeLayersButton;

    private final ManipulatorCraftingRecipe recipe;
    // Highest BlockPos in the recipe
    private final BlockPos maxRecipePos;
    // Maximum number of block layers to be rendered
    private final int maxLayers;
    private int currentLayers;

    public ManipulatorRecipeRenderer(ManipulatorCraftingRecipe recipe, int xOffset, int yOffset, int width, int height) {
        this.recipe = recipe;
        this.maxRecipePos = recipe.ingredients().stream().max((o1, o2) -> {
            double o1Dist = o1.relativeBlockPos().distSqr(BlockPos.ZERO);
            double o2Dist = o2.relativeBlockPos().distSqr(BlockPos.ZERO);
            return Mth.ceil(o1Dist) - Mth.ceil(o2Dist);
        }).map(ManipulatorCraftingIngredient::relativeBlockPos).orElse(BlockPos.ZERO);
        this.maxLayers = maxRecipePos.getY();
        this.currentLayers = maxLayers;

        this.addLayersButton = Button
                .builder(Component.literal("+"), button -> {})
                .bounds(xOffset + width, yOffset + 1, 12, 12)
                .build();
        this.removeLayersButton = Button
                .builder(Component.literal("-"), button -> {})
                .bounds(xOffset +  13,  yOffset + 1, 12, 12)
                .build();

        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.width = width;
        this.height = height;
    }

    public ManipulatorCraftingRecipe getRecipe() {
        return this.recipe;
    }

    public void draw(GuiGraphics guiGraphics, double mouseX, double mouseY) {
//        guiGraphics.enableScissor(
//                guiGraphics.guiWidth() / 2 + this.xOffset,
//                guiGraphics.guiHeight() / 2 + this.yOffset,
//                guiGraphics.guiWidth() / 2 + this.width,
//                guiGraphics.guiWidth() / 2 + this.height
//        );

        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate((float) this.width / 2f, (float) this.height / 2f, 500f);
        poseStack.mulPose(Axis.XP.rotationDegrees(-45f / 2f));
        poseStack.mulPose(Axis.YP.rotationDegrees(-45f));
        poseStack.translate((this.maxRecipePos.getX() + 1) * 10f, (this.maxRecipePos.getY() + 1) * 10f, 0);
        poseStack.scale(-20, -20, -20);

        for (ManipulatorCraftingIngredient ingredient : recipe.ingredients()) {
            BlockPos pos = ingredient.relativeBlockPos();
            if (pos.getY() > this.currentLayers) {
                continue;
            }
            poseStack.pushPose();
            poseStack.translate(pos.getX(), pos.getY(), pos.getZ());
            RenderSystem.setShaderColor(1, 1, 1, 1);
            this.minecraft.getBlockRenderer().renderSingleBlock(ingredient.inputBlockState(), poseStack,
                    guiGraphics.bufferSource(), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
        }
        poseStack.popPose();
        guiGraphics.flush();
        //guiGraphics.disableScissor();

        this.addLayersButton.active = (this.currentLayers < this.maxLayers);
        this.addLayersButton.render(guiGraphics, (int) mouseX, (int) mouseY, this.minecraft.getFrameTime());
        Component visibleLayersString = Component.translatable(ModMessages.UI_JEI_VISIBLE_RECIPE_LAYERS, this.maxLayers + 1);
        guiGraphics.drawString(this.minecraft.font, visibleLayersString,
                this.width - this.minecraft.font.width(visibleLayersString), this.height - 5, 0xFFFFFF);
        this.removeLayersButton.active = (this.currentLayers > 0);
        this.removeLayersButton.render(guiGraphics, (int) mouseX, (int) mouseY, this.minecraft.getFrameTime());
    }

    /**
     * Handle mouse input
     *
     * @return true if the input was handled, false otherwise
     */
    public boolean handleInput(double mouseX, double mouseY, InputConstants.Key input) {
        // Only handle mouse input if the mouse is within the viewing window
        if (mouseX < this.xOffset || mouseX > this.width || mouseY < this.yOffset || mouseY > this.height) {
            return false;
        }
        if (this.addLayersButton.mouseClicked(mouseX, mouseY, input.getValue())) {
            this.currentLayers = Math.min(this.currentLayers + 1, this.maxLayers);
            return true;
        }
        if (this.removeLayersButton.mouseClicked(mouseX, mouseY, input.getValue())) {
            this.currentLayers = Math.max(this.currentLayers - 1, 0);
            return true;
        }
        return false;
    }
}
