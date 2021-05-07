package cat.yoink.xanax.core;

import net.minecraftforge.fml.common.asm.transformers.AccessTransformer;

import java.io.IOException;

public final class Transformer extends AccessTransformer
{
    public Transformer() throws IOException
    {
        super("xanax_at.cfg");
    }
}
