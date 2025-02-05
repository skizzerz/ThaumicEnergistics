package thaumicenergistics.fml;

import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import java.util.Map;

/**
 * Thaumic Energistics Core Mod.
 *
 * @author Nividica
 *
 */
@MCVersion("1.7.10")
public class ThECore extends DummyModContainer implements IFMLLoadingPlugin {
    /**
     * Set to true if any classes required for golem hooks can not be transformed.
     */
    public static boolean golemHooksTransformFailed = false;

    private final ModMetadata metadata = new ModMetadata();

    public ThECore() {
        this.metadata.autogenerated = false;
        this.metadata.authorList.add("Nividica");
        this.metadata.credits = "Nividica";
        this.metadata.modId = this.getModId();
        this.metadata.version = this.getVersion();
        this.metadata.name = this.getName();
        this.metadata.url = "http://minecraft.curseforge.com/projects/thaumic-energistics";
        this.metadata.logoFile = "assets/thaumicenergistics/meta/logo.png";
        this.metadata.description = "Embedded Coremod for Thaumic Energistics";
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[] {"thaumicenergistics.fml.ASMTransformer"};
    }

    @Override
    public String getDisplayVersion() {
        return this.getVersion();
    }

    @Override
    public ModMetadata getMetadata() {
        return this.metadata;
    }

    @Override
    public String getModContainerClass() {
        return "thaumicenergistics.fml.ThECore";
    }

    @Override
    public String getModId() {
        return "ThE-core";
    }

    @Override
    public String getName() {
        return "Thaumic Energistics Core";
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public String getVersion() {
        return "1.0.0.1";
    }

    @Override
    public void injectData(final Map<String, Object> data) {}

    @Override
    public boolean registerBus(final EventBus bus, final LoadController controller) {
        return true;
    }
}
