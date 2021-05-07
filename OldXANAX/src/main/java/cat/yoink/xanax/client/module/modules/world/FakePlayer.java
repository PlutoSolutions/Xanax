// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.world;

import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import java.nio.charset.StandardCharsets;
import java.net.URL;
import com.google.gson.JsonParser;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import cat.yoink.xanax.client.module.Category;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import cat.yoink.xanax.client.module.Module;

public class FakePlayer extends Module
{
    private EntityOtherPlayerMP player;
    
    public FakePlayer() {
        super("FakePlayer", Category.WORLD);
    }
    
    @Override
    public void onEnable() {
        if (this.nullCheck()) {
            this.disable();
            return;
        }
        this.player = null;
        if (FakePlayer.mc.player != null) {
            try {
                this.player = new EntityOtherPlayerMP((World)FakePlayer.mc.world, new GameProfile(UUID.fromString(this.getUuid()), "Katatje"));
            }
            catch (Exception e) {
                this.player = new EntityOtherPlayerMP((World)FakePlayer.mc.world, new GameProfile(UUID.fromString("94b4b37a-7651-4a21-814f-3d4368e312cc"), "Katatje"));
            }
            this.player.copyLocationAndAnglesFrom((Entity)FakePlayer.mc.player);
            this.player.rotationYawHead = FakePlayer.mc.player.rotationYawHead;
            FakePlayer.mc.world.addEntityToWorld(-100, (Entity)this.player);
        }
    }
    
    @Override
    public void onDisable() {
        if (FakePlayer.mc.world != null && FakePlayer.mc.player != null) {
            super.onDisable();
            FakePlayer.mc.world.removeEntity((Entity)this.player);
        }
    }
    
    private String getUuid() {
        final JsonParser parser = new JsonParser();
        final String url = "https://api.mojang.com/users/profiles/minecraft/Katatje";
        try {
            final String UUIDJson = IOUtils.toString(new URL(url), StandardCharsets.UTF_8);
            if (UUIDJson.isEmpty()) {
                return "invalid name";
            }
            final JsonObject UUIDObject = (JsonObject)parser.parse(UUIDJson);
            return this.reformatUuid(UUIDObject.get("id").toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
    
    private String reformatUuid(final String uuid) {
        String longUuid = "";
        longUuid = longUuid + uuid.substring(1, 9) + "-";
        longUuid = longUuid + uuid.substring(9, 13) + "-";
        longUuid = longUuid + uuid.substring(13, 17) + "-";
        longUuid = longUuid + uuid.substring(17, 21) + "-";
        longUuid += uuid.substring(21, 33);
        return longUuid;
    }
}
