package whocraft.tardis_refined.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import whocraft.tardis_refined.common.util.RegistryHelper;

@JeiPlugin
public class TRJeiPlugin implements IModPlugin {

    public static final ResourceLocation PLUGIN_ID = RegistryHelper.makeKey("main");

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }
}
