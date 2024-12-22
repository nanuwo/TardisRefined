package whocraft.tardis_refined.client.renderer.astral_manipulator;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import whocraft.tardis_refined.common.crafting.astral_manipulator.ManipulatorCraftingIngredient;
import whocraft.tardis_refined.common.crafting.astral_manipulator.ManipulatorCraftingRecipe;

import java.util.HashMap;
import java.util.Map;

public class ManipulatorRecipeRenderer {
    private final Minecraft minecraft = Minecraft.getInstance();

    private final ManipulatorCraftingRecipe recipe;
    private final float centreX, centreY, centreZ;
    private int age = 0;

    // Variables for rotation
    private float rotationX = -45F;
    private float rotationY = 45F;
    private boolean isDragging = false;
    // Screen width and height
    private int width = 0;
    private int height = 0;

    public ManipulatorRecipeRenderer(ManipulatorCraftingRecipe recipe) {
        this.recipe = recipe;
        int minX = 100, maxX = -100, minY = 100, maxY = -100, minZ = 100, maxZ = -100;
        for (ManipulatorCraftingIngredient ingredient : recipe.ingredients()) {
            BlockPos pos = ingredient.relativeBlockPos();
            minX = Math.min(minX, pos.getX());
            maxX = Math.max(maxX, pos.getX());
            minY = Math.min(minY, pos.getY());
            maxY = Math.max(maxY, pos.getY());
            minZ = Math.min(minZ, pos.getZ());
            maxZ = Math.max(maxZ, pos.getZ());
        }
        centreX = (maxX - minX + 1) / 2f;
        centreY = (maxY - minY + 1) / 2f;
        centreZ = (maxZ - minZ + 1) / 2f;
    }

    public void render(@NotNull GuiGraphics guiGraphics) {
        if (recipe.ingredients().isEmpty()) return;

        PoseStack pose = guiGraphics.pose();

        Lighting.setupFor3DItems();
        pose.pushPose();
        pose.translate((float) width / 2 - 55, height / 2f, 500);
        pose.mulPose(Axis.XP.rotationDegrees(rotationX / 2));
        pose.mulPose(Axis.YP.rotationDegrees(rotationY));
        pose.scale(-20, -20, -20);

        int i = 0;
        int e = age / 10;
        for (ManipulatorCraftingIngredient ingredient : recipe.ingredients()) {
            if (i > e % recipe.ingredients().size()) break;
            BlockState s = ingredient.inputBlockState();
            BlockPos pos = ingredient.relativeBlockPos();
            pose.pushPose();
            // Translate blocks relative to the centre pos, so that the model rotates around the centre
            pose.translate(pos.getX() - centreX,  pos.getY() - centreY, pos.getZ() - centreZ);
            assert minecraft.level != null;
            RenderSystem.setShaderColor(1, 1, 1, 1);
            minecraft.getBlockRenderer().renderSingleBlock(s, guiGraphics.pose(), guiGraphics.bufferSource(), LightTexture.FULL_BLOCK, OverlayTexture.NO_OVERLAY);

            pose.popPose();
            i++;
        }

        guiGraphics.flush();
        Lighting.setupFor3DItems();
        pose.popPose();

    }

    public Map<String, Integer> getItemCounts() {
        Map<String, Integer> itemCounts = new HashMap<>();
        for (ManipulatorCraftingIngredient ingredient : recipe.ingredients()) {
            String itemName = new ItemStack(ingredient.inputBlockState().getBlock()).getHoverName().getString();
            itemCounts.put(itemName, itemCounts.getOrDefault(itemName, 0) + 1);
        }
        return itemCounts;
    }

    public void tick() {
        age++;
    }

    public void onMouseDragged(double deltaX, double deltaY) {
        if (isDragging) {
            rotationY += (float) (deltaX * 0.5f);
            rotationX -= (float) (deltaY * 0.5f);
            rotationX = Math.max(-90, Math.min(90, rotationX));
        }
    }

    public void onMouseClicked() {
        isDragging = true;
    }

    public boolean onMouseReleased(int button) {
        if (button == 0) { // Left mouse button
            isDragging = false;
            return true;
        }
        return false;
    }

    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
