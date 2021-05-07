// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EncryptionUtil
{
    public static String encryptXOR(final String obj, final String key) {
        final StringBuilder sb = new StringBuilder();
        final char[] keyChars = key.toCharArray();
        int i = 0;
        for (final char c : obj.toCharArray()) {
            sb.append((char)(c ^ keyChars[i % keyChars.length]));
            ++i;
        }
        return new String(Base64.getEncoder().encode(sb.toString().getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }
}
