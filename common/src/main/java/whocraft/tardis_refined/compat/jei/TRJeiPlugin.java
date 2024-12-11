package whocraft.tardis_refined.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import whocraft.tardis_refined.common.crafting.astral_manipulator.ManipulatorCraftingRecipe;
import whocraft.tardis_refined.common.util.RegistryHelper;
import whocraft.tardis_refined.registry.TRBlockRegistry;

@JeiPlugin
public class TRJeiPlugin implements IModPlugin {

    public static final ResourceLocation PLUGIN_ID = RegistryHelper.makeKey("main");

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ManipulatorRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            registration.addRecipes(ManipulatorRecipeCategory.MANIPULATOR_RECIPE_TYPE, ManipulatorCraftingRecipe.getAllRecipes(level));
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(TRBlockRegistry.ASTRAL_MANIPULATOR_BLOCK.get()), ManipulatorRecipeCategory.MANIPULATOR_RECIPE_TYPE);
    }
}
