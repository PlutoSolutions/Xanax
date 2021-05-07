// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.util;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.File;

public class FileUtil
{
    public static void saveFile(final File file, final ArrayList<String> content) throws IOException {
        final BufferedWriter out = new BufferedWriter(new FileWriter(file));
        for (final String s : content) {
            out.write(s);
            out.write("\r\n");
        }
        out.close();
    }
    
    public static ArrayList<String> loadFile(final File file) throws IOException {
        final ArrayList<String> content = new ArrayList<String>();
        final FileInputStream stream = new FileInputStream(file.getAbsolutePath());
        final DataInputStream in = new DataInputStream(stream);
        final BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = br.readLine()) != null) {
            content.add(line);
        }
        br.close();
        return content;
    }
}
