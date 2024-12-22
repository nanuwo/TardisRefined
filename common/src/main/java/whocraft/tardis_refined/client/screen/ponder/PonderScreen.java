package whocraft.tardis_refined.client.screen.ponder;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import whocraft.tardis_refined.TardisRefined;
import whocraft.tardis_refined.client.ScreenHandler;
import whocraft.tardis_refined.client.renderer.astral_manipulator.ManipulatorRecipeRenderer;
import whocraft.tardis_refined.client.screen.components.GenericMonitorSelectionList;
import whocraft.tardis_refined.client.screen.components.SelectionListEntry;
import whocraft.tardis_refined.client.screen.main.MonitorOS;
import whocraft.tardis_refined.common.crafting.astral_manipulator.ManipulatorBlockResult;
import whocraft.tardis_refined.common.crafting.astral_manipulator.ManipulatorCraftingRecipe;
import whocraft.tardis_refined.common.crafting.astral_manipulator.ManipulatorItemResult;

import java.util.Map;

public class PonderScreen extends MonitorOS {

    private final ManipulatorRecipeRenderer recipeRenderer;

    public PonderScreen(ManipulatorCraftingRecipe recipe) {
        super(getResultName(recipe), new ResourceLocation(TardisRefined.MODID, "textures/gui/monitor/backdrop.png"));

        this.recipeRenderer = new ManipulatorRecipeRenderer(recipe);
        this.setEvents(() -> {}, ScreenHandler::openCraftingScreen);
    }

    public static Component getResultName(ManipulatorCraftingRecipe recipe) {
        if (recipe.result() instanceof ManipulatorBlockResult blockResult) {
            BlockState blockState = blockResult.recipeOutput();
            return blockState.getBlock().getName();
        } else if (recipe.result() instanceof ManipulatorItemResult itemResult) {
            ItemStack itemStack = itemResult.recipeOutput();
            return itemStack.getHoverName();
        }
        return Component.literal("What on earth are you crafting?");
    }

    @Override
    protected void init() {
        super.init();
        int vPos = (height - MONITOR_HEIGHT) / 2;
        addCancelButton(width / 2 - 105, height - vPos - 25);
        /*
         Necessary because the width and height are set in the super init method, so this must be done here
         instead of the constructor
        */
        recipeRenderer.setDimensions(width, height);
    }

    @Override
    public ObjectSelectionList<SelectionListEntry> createSelectionList() {
        int leftPos = width / 2;
        int topPos = (height - MONITOR_HEIGHT) / 2;
        GenericMonitorSelectionList<SelectionListEntry> selectionList = new GenericMonitorSelectionList<>(
                this.minecraft, 105, 80, leftPos, topPos + 15, topPos + MONITOR_HEIGHT - 30, 12
        );
        selectionList.setRenderBackground(false);

        // Add entries to the selection list with item count
        for (Map.Entry<String, Integer> entry : recipeRenderer.getItemCounts().entrySet()) {
            String itemNameWithCount = entry.getValue() + "x " + entry.getKey();
            SelectionListEntry selectionListEntry = new SelectionListEntry(
                    Component.literal(itemNameWithCount), (entryCallback) -> {
            }, leftPos);
            selectionList.children().add(selectionListEntry);
        }

        return selectionList;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        assert minecraft != null;

        recipeRenderer.render(guiGraphics);
}

    @Override
    public void tick() {
        super.tick();
        recipeRenderer.tick();
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        recipeRenderer.onMouseDragged(deltaX, deltaY);
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        recipeRenderer.onMouseClicked();
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (recipeRenderer.onMouseReleased(button)) return true;
        return super.mouseReleased(mouseX, mouseY, button);
    }
}
