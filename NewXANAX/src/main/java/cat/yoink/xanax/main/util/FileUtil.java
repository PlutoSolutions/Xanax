package cat.yoink.xanax.main.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class FileUtil
{
    public static void saveFile(final File file, final List<String> content) throws IOException
    {
        final BufferedWriter out = new BufferedWriter(new FileWriter(file));

        for (final String s : content)
        {
            out.write(s);
            out.write("\r\n");
        }
        out.close();
    }

    public static List<String> loadFile(final File file) throws IOException
    {
        final List<String> content = new ArrayList<>();

        final FileInputStream stream = new FileInputStream(file.getAbsolutePath());
        final DataInputStream in = new DataInputStream(stream);
        final BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;

        while ((line = br.readLine()) != null) content.add(line);

        br.close();

        return content;
    }
}
