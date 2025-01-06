package whocraft.tardis_refined.compat.jei;

import com.mojang.blaze3d.platform.InputConstants;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import whocraft.tardis_refined.TardisRefined;
import whocraft.tardis_refined.common.crafting.astral_manipulator.ManipulatorCraftingIngredient;
import whocraft.tardis_refined.common.crafting.astral_manipulator.ManipulatorCraftingRecipe;
import whocraft.tardis_refined.registry.TRBlockRegistry;
import whocraft.tardis_refined.registry.TRItemRegistry;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ManipulatorRecipeCategory implements IRecipeCategory<ManipulatorCraftingRecipe> {

    public static final RecipeType<ManipulatorCraftingRecipe> MANIPULATOR_RECIPE_TYPE = RecipeType.create(TardisRefined.MODID, "astral_manipulator", ManipulatorCraftingRecipe.class);
    private final IGuiHelper guiHelper;
    public static final int BACKGROUND_WIDTH = 190;
    public static final int BACKGROUND_HEIGHT = 130;
    private @Nullable ManipulatorRecipeRenderer recipeRenderer;

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
        return guiHelper.createBlankDrawable(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
    }

    @Override
    public IDrawable getIcon() {
        return guiHelper.createDrawableItemStack(new ItemStack(TRBlockRegistry.ASTRAL_MANIPULATOR_BLOCK.get().asItem()));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ManipulatorCraftingRecipe recipe, IFocusGroup focuses) {
        this.recipeRenderer = new ManipulatorRecipeRenderer(recipe, 20, 20, 100, 100);

        builder.addSlot(RecipeIngredientRole.OUTPUT, BACKGROUND_WIDTH - 1, BACKGROUND_HEIGHT / 2)
                .addItemStack(RecipeUtil.getResultItem(recipe))
                .setBackground(guiHelper.getSlotDrawable(), -1, -1);
        builder.addSlot(RecipeIngredientRole.CATALYST, BACKGROUND_WIDTH / 2, 1)
                .addItemStack(new ItemStack(TRItemRegistry.SCREWDRIVER.get()))
                .setBackground(guiHelper.getSlotDrawable(), -1, -1);


        List<ItemStack> uniqueItems = new ArrayList<>();
        for (ManipulatorCraftingIngredient ingredient : recipe.ingredients()) {
            Item item = ingredient.inputBlockState().getBlock().asItem();
            uniqueItems.stream()
                    .filter(itemStack -> itemStack.is(item))
                    .findFirst()
                    .ifPresentOrElse(itemStack -> itemStack.grow(1), () -> uniqueItems.add(new ItemStack(item)));
        }
        for (int i = 0; i < uniqueItems.size(); i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, 1, 1 + 18 * i)
                    .addItemStack(uniqueItems.get(i))
                    .setBackground(guiHelper.getSlotDrawable(), -1, -1);
        }
    }

    @Override
    public void draw(ManipulatorCraftingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        if (this.recipeRenderer == null) return;
        this.recipeRenderer.draw(guiGraphics, mouseX, mouseY);

    }

    @Override
    public boolean handleInput(ManipulatorCraftingRecipe recipe, double mouseX, double mouseY, InputConstants.Key input) {
        if (input.getType() != InputConstants.Type.MOUSE) {
            return false;
        }
        return this.recipeRenderer != null && this.recipeRenderer.handleInput(mouseX, mouseY, input);
    }
}
