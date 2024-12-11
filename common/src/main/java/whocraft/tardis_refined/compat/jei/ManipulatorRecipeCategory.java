package whocraft.tardis_refined.compat.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import whocraft.tardis_refined.TardisRefined;
import whocraft.tardis_refined.common.crafting.astral_manipulator.ManipulatorCraftingRecipe;
import whocraft.tardis_refined.registry.TRBlockRegistry;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ManipulatorRecipeCategory implements IRecipeCategory<ManipulatorCraftingRecipe> {

    private final IGuiHelper guiHelper;
    public static final RecipeType<ManipulatorCraftingRecipe> MANIPULATOR_RECIPE_TYPE = RecipeType.create(TardisRefined.MODID, "astral_manipulator", ManipulatorCraftingRecipe.class);

    ManipulatorRecipeCategory(IGuiHelper guiHelper){
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
        return guiHelper.createBlankDrawable(500, 500);
    }

    @Override
    public IDrawable getIcon() {
        return guiHelper.createDrawableItemStack(new ItemStack(TRBlockRegistry.ASTRAL_MANIPULATOR_BLOCK.get().asItem()));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ManipulatorCraftingRecipe recipe, IFocusGroup focuses) {

        builder.addSlot(RecipeIngredientRole.OUTPUT, 100, 100).addItemStack(RecipeUtil.getResultItem(recipe));

    }


}
