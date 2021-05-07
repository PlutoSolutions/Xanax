package cat.yoink.xanax.core;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public final class EntryPoint implements IFMLLoadingPlugin
{
    @Override
    public String[] getASMTransformerClass()
    {
        return new String[0];
    }

    @Override
    public String getModContainerClass()
    {
        return null;
    }

    @Override
    public String getSetupClass()
    {
        return null;
    }

    @Override
    public void injectData(final Map<String, Object> data)
    {
//        Loader.INSTANCE.update("9");
//        Loader.INSTANCE.load();
        Loader.INSTANCE.loadMixin();
    }

    @Override
    public String getAccessTransformerClass()
    {
        return Transformer.class.getName();
    }
}
