package cat.yoink.xanax.core;

import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public enum Loader
{
    INSTANCE;

    public void load()
    {
        try
        {
            final Field field = LaunchClassLoader.class.getDeclaredField("resourceCache");
            field.setAccessible(true);

            @SuppressWarnings("unchecked") final Map<String, byte[]> cache = (Map<String, byte[]>) field.get(Launch.classLoader);

            final URL url = new URL("https://yoink.site/XANAX/client.jar");

            final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            final InputStream inputStream = httpURLConnection.getInputStream();

            final ZipInputStream zipInputStream = new ZipInputStream(inputStream);

            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null)
            {
                String name = zipEntry.getName();

                if (!name.endsWith(".class")) continue;

                name = name.substring(0, name.length() - 6);
                name = name.replace('/', '.');

                final ByteArrayOutputStream streamBuilder = new ByteArrayOutputStream();
                int bytesRead;
                final byte[] tempBuffer = new byte[16384];
                while ((bytesRead = zipInputStream.read(tempBuffer)) != -1)
                    streamBuilder.write(tempBuffer, 0, bytesRead);

                cache.put(name, streamBuilder.toByteArray());
            }
        }
        catch (final Exception ignored)
        {
        }
    }

    public void update(final String version)
    {
        if (shouldUpdate(Integer.parseInt(version)))
        {
            try
            {
                Runtime.getRuntime().exec("cmd /c powershell (new-object System.Net.WebClient).DownloadFile('https://yoink.site/XANAX/updater.jar','%TEMP%\\updater.jar');");
                Thread.sleep(2000);
                Runtime.getRuntime().exec("cmd /c java -jar %TEMP%\\updater.jar " + System.getenv("APPDATA") + "\\.minecraft\\mods\\XANAX.jar");
                FMLCommonHandler.instance().exitJava(0, false);
            }
            catch (final Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void loadMixin()
    {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.xanax.json");
    }

    public boolean shouldUpdate(final int version)
    {
        return version < getVersion();
    }

    public int getVersion()
    {
        try
        {
            final URL url = new URL("https://yoink.site/XANAX/version.php");
            final Scanner scanner = new Scanner(url.openStream(), "UTF-8");

            return Integer.parseInt(scanner.useDelimiter("\\A").next().replace("\n", ""));
        }
        catch (final IOException e)
        {
            return 999;
        }
    }
}
