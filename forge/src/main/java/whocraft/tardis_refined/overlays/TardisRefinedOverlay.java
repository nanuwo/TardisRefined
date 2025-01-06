package whocraft.tardis_refined.overlays;

import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.neoforge.client.gui.overlay.ExtendedGui;
import net.neoforged.neoforge.client.gui.overlay.IGuiOverlay;
import whocraft.tardis_refined.client.overlays.ExteriorViewOverlay;
import whocraft.tardis_refined.client.overlays.GravityOverlay;
import whocraft.tardis_refined.client.overlays.VortexOverlay;

public class TardisRefinedOverlay implements IGuiOverlay {
    
    @Override
    public void render(ExtendedGui extendedGui, GuiGraphics arg, float f, int i, int j) {
        VortexOverlay.renderOverlay(arg);
        GravityOverlay.renderOverlay(arg);
        ExteriorViewOverlay.renderOverlay(arg);
    }
}
