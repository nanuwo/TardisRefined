package whocraft.tardis_refined.compat.jei;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import whocraft.tardis_refined.TardisRefined;
import whocraft.tardis_refined.common.crafting.astral_manipulator.ManipulatorCraftingIngredient;
import whocraft.tardis_refined.common.crafting.astral_manipulator.ManipulatorCraftingRecipe;
import whocraft.tardis_refined.registry.TRBlockRegistry;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ManipulatorRecipeCategory implements IRecipeCategory<ManipulatorCraftingRecipe> {

    public static final RecipeType<ManipulatorCraftingRecipe> MANIPULATOR_RECIPE_TYPE = RecipeType.create(TardisRefined.MODID, "astral_manipulator", ManipulatorCraftingRecipe.class);
    private final IGuiHelper guiHelper;

    private float animationScale = 1;
    private static int BACKGROUND_SIZE = 200;

    ManipulatorRecipeCategory(IGuiHelper guiHelper) {
        this.guiHelper = guiHelper;
    }

    @Override
    public RecipeType<ManipulatorCraftingRecipe> getRecipeType() {
        return MANIPULATOR_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return TRBlockRegistry.ASTRAL_MANIPULATOR_BLOCK.get().getName();
    }

    @Override
    public IDrawable getBackground() {
        return guiHelper.createBlankDrawable(BACKGROUND_SIZE, BACKGROUND_SIZE);
    }

    @Override
    public IDrawable getIcon() {
        return guiHelper.createDrawableItemStack(new ItemStack(TRBlockRegistry.ASTRAL_MANIPULATOR_BLOCK.get().asItem()));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ManipulatorCraftingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, 100, 100).addItemStack(RecipeUtil.getResultItem(recipe));
    }

    @Override
    public void draw(ManipulatorCraftingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {

        BlockPos maxPos = recipe.ingredients().stream().max((o1, o2) -> {
            double o1Dist = o1.relativeBlockPos().distSqr(BlockPos.ZERO);
            double o2Dist = o2.relativeBlockPos().distSqr(BlockPos.ZERO);
            return Mth.ceil(o1Dist) - Mth.ceil(o2Dist);
        }).map(ManipulatorCraftingIngredient::relativeBlockPos).orElse(BlockPos.ZERO);

        if (mouseX >= 50 && mouseX <= 150) {
            this.animationScale = Mth.lerp(0.3f, animationScale, 2.5f);
        } else {
            this.animationScale = Mth.lerp(0.3f, animationScale, 1.0f);
        }

//        TardisRefined.LOGGER.info("current mouse x: {}, y: {}", mouseX, mouseY);

        PoseStack poseStack = guiGraphics.pose();
        Lighting.setupFor3DItems();
        poseStack.pushPose();
        poseStack.translate((float) BACKGROUND_SIZE / 2f, (float) BACKGROUND_SIZE / 2f, 500f);
        poseStack.mulPose(Axis.XP.rotationDegrees(-45f / 2));
        poseStack.mulPose(Axis.YP.rotationDegrees(45));
        poseStack.scale(-20, -20, -20);
        //poseStack.translate(-maxPos.getX() / 2f, -maxPos.getY() / 2f, -maxPos.getZ() / 2f);
        for (IRecipeSlotView slotView : recipeSlotsView.getSlotViews()) {

        }

        for (ManipulatorCraftingIngredient ingredient : recipe.ingredients()) {
            BlockState state = ingredient.inputBlockState();
            BlockPos pos = ingredient.relativeBlockPos();

            poseStack.pushPose();
            poseStack.translate(pos.getX(), pos.getY() * this.animationScale, pos.getZ());
            RenderSystem.setShaderColor(1, 1, 1, 1);
            Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, poseStack, guiGraphics.bufferSource(), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
        }
        poseStack.popPose();
        guiGraphics.flush();
    }
}
