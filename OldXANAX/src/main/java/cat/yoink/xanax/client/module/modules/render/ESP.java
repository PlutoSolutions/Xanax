// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.render;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.client.renderer.RenderGlobal;
import cat.yoink.xanax.client.util.RenderUtil;
import java.util.Objects;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraft.client.renderer.culling.Frustum;
import cat.yoink.xanax.client.module.Category;
import net.minecraft.client.renderer.culling.ICamera;
import cat.yoink.xanax.client.setting.NumberSetting;
import cat.yoink.xanax.client.setting.BooleanSetting;
import cat.yoink.xanax.client.module.Module;

public class ESP extends Module
{
    private final BooleanSetting box;
    private final BooleanSetting fill;
    private final NumberSetting red;
    private final NumberSetting green;
    private final NumberSetting blue;
    private final NumberSetting alpha;
    private final ICamera camera;
    
    public ESP() {
        super("ESP", Category.RENDER);
        this.box = this.newSetting(new BooleanSetting("Box", true));
        this.fill = this.newSetting(new BooleanSetting("Fill", true));
        this.red = this.newSetting(new NumberSetting("Red", 0.0, 0.0, 100.0, 1.0));
        this.green = this.newSetting(new NumberSetting("Green", 100.0, 0.0, 100.0, 1.0));
        this.blue = this.newSetting(new NumberSetting("Blue", 0.0, 0.0, 100.0, 1.0));
        this.alpha = this.newSetting(new NumberSetting("Alpha", 50.0, 0.0, 100.0, 1.0));
        this.camera = (ICamera)new Frustum();
    }
    
    @SubscribeEvent
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        if (this.nullCheck()) {
            return;
        }
        for (final EntityPlayer playerEntity : ESP.mc.world.playerEntities) {
            if (playerEntity.equals((Object)ESP.mc.player)) {
                continue;
            }
            final AxisAlignedBB bb = new AxisAlignedBB(playerEntity.posX - 0.3 - ESP.mc.getRenderManager().viewerPosX, playerEntity.posY - ESP.mc.getRenderManager().viewerPosY, playerEntity.posZ - 0.3 - ESP.mc.getRenderManager().viewerPosZ, playerEntity.posX + 0.3 - ESP.mc.getRenderManager().viewerPosX, playerEntity.posY + 1.8 - ESP.mc.getRenderManager().viewerPosY, playerEntity.posZ + 0.3 - ESP.mc.getRenderManager().viewerPosZ);
            this.camera.setPosition(Objects.requireNonNull(ESP.mc.getRenderViewEntity()).posX, ESP.mc.getRenderViewEntity().posY, ESP.mc.getRenderViewEntity().posZ);
            if (!this.camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + ESP.mc.getRenderManager().viewerPosX, bb.minY + ESP.mc.getRenderManager().viewerPosY, bb.minZ + ESP.mc.getRenderManager().viewerPosZ, bb.maxX + ESP.mc.getRenderManager().viewerPosX, bb.maxY + ESP.mc.getRenderManager().viewerPosY, bb.maxZ + ESP.mc.getRenderManager().viewerPosZ))) {
                continue;
            }
            RenderUtil.glSetup();
            if (this.fill.getValue()) {
                RenderGlobal.renderFilledBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, (float)this.red.getValue() / 100.0f, (float)this.green.getValue() / 100.0f, (float)this.blue.getValue() / 100.0f, (float)this.alpha.getValue() / 100.0f);
            }
            if (this.box.getValue()) {
                RenderGlobal.drawBoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, (float)this.red.getValue() / 100.0f, (float)this.green.getValue() / 100.0f, (float)this.blue.getValue() / 100.0f, ((float)this.alpha.getValue() / 100.0f + 0.3f > 1.0f) ? 1.0f : ((float)this.alpha.getValue() / 100.0f + 0.3f));
            }
            RenderUtil.glCleanup();
        }
    }
}
