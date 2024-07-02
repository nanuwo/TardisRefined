package whocraft.tardis_refined.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import whocraft.tardis_refined.TardisRefined;
import whocraft.tardis_refined.common.tardis.themes.ConsoleTheme;
import whocraft.tardis_refined.common.tardis.themes.console.*;

public class TRConsoleThemes {
    /**
     * Registry Key for the ConsoleTheme registry. For addon mods, use this as the registry key
     */
    public static final ResourceKey<Registry<ConsoleTheme>> CONSOLE_THEME_REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(TardisRefined.MODID, "console_theme"));;
    /**
     * Tardis Refined instance of the ConsoleTheme registry. Addon Mods: DO NOT USE THIS, it is only for Tardis Refined use only
     */
    public static final DeferredRegistry<ConsoleTheme> CONSOLE_THEME_DEFERRED_REGISTRY = DeferredRegistry.createCustom(TardisRefined.MODID, CONSOLE_THEME_REGISTRY_KEY, true);
    /**
     * Instance of registry containing all ConsoleTheme entries. Addon mod entries will be included in this registry as long as they are use the same ResourceKey<Registry<ObjectType>>.
     */
    public static final Registry<ConsoleTheme> CONSOLE_THEME_REGISTRY = CONSOLE_THEME_DEFERRED_REGISTRY.getRegistry().get();
    public static final RegistrySupplierHolder<ConsoleTheme, ConsoleTheme> FACTORY = registerConsoleTheme("factory", new FactoryConsoleTheme());
    public static final RegistrySupplierHolder<ConsoleTheme, ConsoleTheme> CRYSTAL = registerConsoleTheme("crystal", new CrystalConsoleTheme());
    public static final RegistrySupplierHolder<ConsoleTheme, ConsoleTheme> COPPER = registerConsoleTheme("copper", new CopperConsoleTheme());
    public static final RegistrySupplierHolder<ConsoleTheme, ConsoleTheme> CORAL = registerConsoleTheme("coral", new CoralConsoleTheme());
    public static final RegistrySupplierHolder<ConsoleTheme, ConsoleTheme> TOYOTA = registerConsoleTheme("toyota", new ToyotaConsoleTheme());
    public static final RegistrySupplierHolder<ConsoleTheme, ConsoleTheme> VICTORIAN = registerConsoleTheme("victorian", new VictorianConsoleTheme());
    public static final RegistrySupplierHolder<ConsoleTheme, ConsoleTheme> MYST = registerConsoleTheme("myst", new MystConsoleTheme());
    public static final RegistrySupplierHolder<ConsoleTheme, ConsoleTheme> NUKA = registerConsoleTheme("nuka", new NukaConsoleTheme());
    public static final RegistrySupplierHolder<ConsoleTheme, ConsoleTheme> INITIATIVE = registerConsoleTheme("initiative", new InitiativeConsoleTheme());
    public static final RegistrySupplierHolder<ConsoleTheme, ConsoleTheme> REFURBISHED = registerConsoleTheme("refurbished", new RefurbishedConsoleTheme());

    private static RegistrySupplierHolder<ConsoleTheme, ConsoleTheme> registerConsoleTheme(String id, ConsoleThemeDetails themeDetails){
        return CONSOLE_THEME_DEFERRED_REGISTRY.registerHolder(id, () -> new ConsoleTheme(new ResourceLocation(TardisRefined.MODID, id), themeDetails));
    }
}