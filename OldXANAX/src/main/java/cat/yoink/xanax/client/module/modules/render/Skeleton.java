// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.render;

import net.minecraft.client.model.ModelPlayer;
import java.lang.reflect.Field;
import cat.yoink.xanax.client.handling.Mapping;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.gui.Gui;
import java.util.function.Predicate;
import org.lwjgl.opengl.GL11;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraft.client.renderer.culling.Frustum;
import cat.yoink.xanax.client.module.Category;
import net.minecraft.entity.player.EntityPlayer;
import java.util.HashMap;
import net.minecraft.client.renderer.culling.ICamera;
import cat.yoink.xanax.client.setting.NumberSetting;
import cat.yoink.xanax.client.module.Module;

public class Skeleton extends Module
{
    private final NumberSetting red;
    private final NumberSetting green;
    private final NumberSetting blue;
    private final NumberSetting alpha;
    private final ICamera camera;
    private final HashMap<EntityPlayer, float[][]> entities;
    
    public Skeleton() {
        super("Skeleton", Category.RENDER);
        this.red = this.newSetting(new NumberSetting("Red", 255.0, 0.0, 255.0, 1.0));
        this.green = this.newSetting(new NumberSetting("Green", 255.0, 0.0, 255.0, 1.0));
        this.blue = this.newSetting(new NumberSetting("Blue", 255.0, 0.0, 255.0, 1.0));
        this.alpha = this.newSetting(new NumberSetting("Alpha", 255.0, 0.0, 255.0, 1.0));
        this.camera = (ICamera)new Frustum();
        this.entities = new HashMap<EntityPlayer, float[][]>();
    }
    
    @SubscribeEvent
    public void onWorldRender(final RenderWorldLastEvent event) {
        if (this.nullCheck() || Skeleton.mc.getRenderManager().options == null) {
            return;
        }
        this.startEnd(true);
        GL11.glEnable(2903);
        GL11.glDisable(2848);
        this.entities.keySet().removeIf(this::doesntContain);
        Skeleton.mc.world.playerEntities.forEach(e -> this.drawSkeleton(event, e));
        Gui.drawRect(0, 0, 0, 0, 0);
        this.startEnd(false);
    }
    
    private Vec3d getVec3(final RenderWorldLastEvent event, final EntityPlayer e) {
        final float pt = event.getPartialTicks();
        final double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * pt;
        final double y = e.lastTickPosY + (e.posY - e.lastTickPosY) * pt;
        final double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * pt;
        return new Vec3d(x, y, z);
    }
    
    private void drawSkeleton(final RenderWorldLastEvent event, final EntityPlayer e) {
        final double d3 = Skeleton.mc.player.lastTickPosX + (Skeleton.mc.player.posX - Skeleton.mc.player.lastTickPosX) * event.getPartialTicks();
        final double d4 = Skeleton.mc.player.lastTickPosY + (Skeleton.mc.player.posY - Skeleton.mc.player.lastTickPosY) * event.getPartialTicks();
        final double d5 = Skeleton.mc.player.lastTickPosZ + (Skeleton.mc.player.posZ - Skeleton.mc.player.lastTickPosZ) * event.getPartialTicks();
        this.camera.setPosition(d3, d4, d5);
        final float[][] entPos = this.entities.get(e);
        if (entPos != null && e.isEntityAlive() && this.camera.isBoundingBoxInFrustum(e.getEntityBoundingBox()) && !e.isDead && e != Skeleton.mc.player && !e.isPlayerSleeping()) {
            GL11.glPushMatrix();
            GL11.glEnable(2848);
            GL11.glLineWidth(1.0f);
            GlStateManager.color((float)this.red.getValue() / 255.0f, (float)this.green.getValue() / 255.0f, (float)this.blue.getValue() / 255.0f, (float)this.alpha.getValue() / 255.0f);
            final Vec3d vec = this.getVec3(event, e);
            double renderPosX = 0.0;
            double renderPosY = 0.0;
            double renderPosZ = 0.0;
            try {
                final Field rPosX = RenderManager.class.getDeclaredField(Mapping.renderManagerRenderPosX);
                rPosX.setAccessible(true);
                renderPosX = (double)rPosX.get(Skeleton.mc.getRenderManager());
                final Field rPosY = RenderManager.class.getDeclaredField(Mapping.renderManagerRenderPosY);
                rPosY.setAccessible(true);
                renderPosY = (double)rPosY.get(Skeleton.mc.getRenderManager());
                final Field rPosZ = RenderManager.class.getDeclaredField(Mapping.renderManagerRenderPosZ);
                rPosZ.setAccessible(true);
                renderPosZ = (double)rPosZ.get(Skeleton.mc.getRenderManager());
            }
            catch (Exception ex) {}
            final double x = vec.x - renderPosX;
            final double y = vec.y - renderPosY;
            final double z = vec.z - renderPosZ;
            GL11.glTranslated(x, y, z);
            final float xOff = e.prevRenderYawOffset + (e.renderYawOffset - e.prevRenderYawOffset) * event.getPartialTicks();
            GL11.glRotatef(-xOff, 0.0f, 1.0f, 0.0f);
            GL11.glTranslated(0.0, 0.0, e.isSneaking() ? -0.235 : 0.0);
            final float yOff = e.isSneaking() ? 0.6f : 0.75f;
            GL11.glPushMatrix();
            GlStateManager.color((float)this.red.getValue() / 255.0f, (float)this.green.getValue() / 255.0f, (float)this.blue.getValue() / 255.0f, (float)this.alpha.getValue() / 255.0f);
            GL11.glTranslated(-0.125, (double)yOff, 0.0);
            if (entPos[3][0] != 0.0f) {
                GL11.glRotatef(entPos[3][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
            }
            if (entPos[3][1] != 0.0f) {
                GL11.glRotatef(entPos[3][1] * 57.295776f, 0.0f, 1.0f, 0.0f);
            }
            if (entPos[3][2] != 0.0f) {
                GL11.glRotatef(entPos[3][2] * 57.295776f, 0.0f, 0.0f, 1.0f);
            }
            GL11.glBegin(3);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, (double)(-yOff), 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GlStateManager.color((float)this.red.getValue() / 255.0f, (float)this.green.getValue() / 255.0f, (float)this.blue.getValue() / 255.0f, (float)this.alpha.getValue() / 255.0f);
            GL11.glTranslated(0.125, (double)yOff, 0.0);
            if (entPos[4][0] != 0.0f) {
                GL11.glRotatef(entPos[4][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
            }
            if (entPos[4][1] != 0.0f) {
                GL11.glRotatef(entPos[4][1] * 57.295776f, 0.0f, 1.0f, 0.0f);
            }
            if (entPos[4][2] != 0.0f) {
                GL11.glRotatef(entPos[4][2] * 57.295776f, 0.0f, 0.0f, 1.0f);
            }
            GL11.glBegin(3);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, (double)(-yOff), 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glTranslated(0.0, 0.0, e.isSneaking() ? 0.25 : 0.0);
            GL11.glPushMatrix();
            GlStateManager.color((float)this.red.getValue() / 255.0f, (float)this.green.getValue() / 255.0f, (float)this.blue.getValue() / 255.0f, (float)this.alpha.getValue() / 255.0f);
            GL11.glTranslated(0.0, e.isSneaking() ? -0.05 : 0.0, e.isSneaking() ? -0.01725 : 0.0);
            GL11.glPushMatrix();
            GlStateManager.color((float)this.red.getValue() / 255.0f, (float)this.green.getValue() / 255.0f, (float)this.blue.getValue() / 255.0f, (float)this.alpha.getValue() / 255.0f);
            GL11.glTranslated(-0.375, yOff + 0.55, 0.0);
            if (entPos[1][0] != 0.0f) {
                GL11.glRotatef(entPos[1][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
            }
            if (entPos[1][1] != 0.0f) {
                GL11.glRotatef(entPos[1][1] * 57.295776f, 0.0f, 1.0f, 0.0f);
            }
            if (entPos[1][2] != 0.0f) {
                GL11.glRotatef(-entPos[1][2] * 57.295776f, 0.0f, 0.0f, 1.0f);
            }
            GL11.glBegin(3);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, -0.5, 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated(0.375, yOff + 0.55, 0.0);
            if (entPos[2][0] != 0.0f) {
                GL11.glRotatef(entPos[2][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
            }
            if (entPos[2][1] != 0.0f) {
                GL11.glRotatef(entPos[2][1] * 57.295776f, 0.0f, 1.0f, 0.0f);
            }
            if (entPos[2][2] != 0.0f) {
                GL11.glRotatef(-entPos[2][2] * 57.295776f, 0.0f, 0.0f, 1.0f);
            }
            GL11.glBegin(3);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, -0.5, 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glRotatef(xOff - e.rotationYawHead, 0.0f, 1.0f, 0.0f);
            GL11.glPushMatrix();
            GlStateManager.color((float)this.red.getValue() / 255.0f, (float)this.green.getValue() / 255.0f, (float)this.blue.getValue() / 255.0f, (float)this.alpha.getValue() / 255.0f);
            GL11.glTranslated(0.0, yOff + 0.55, 0.0);
            if (entPos[0][0] != 0.0f) {
                GL11.glRotatef(entPos[0][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
            }
            GL11.glBegin(3);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, 0.3, 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPopMatrix();
            GL11.glRotatef(e.isSneaking() ? 25.0f : 0.0f, 1.0f, 0.0f, 0.0f);
            GL11.glTranslated(0.0, e.isSneaking() ? -0.16175 : 0.0, e.isSneaking() ? -0.48025 : 0.0);
            GL11.glPushMatrix();
            GL11.glTranslated(0.0, (double)yOff, 0.0);
            GL11.glBegin(3);
            GL11.glVertex3d(-0.125, 0.0, 0.0);
            GL11.glVertex3d(0.125, 0.0, 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GlStateManager.color((float)this.red.getValue() / 255.0f, (float)this.green.getValue() / 255.0f, (float)this.blue.getValue() / 255.0f, (float)this.alpha.getValue() / 255.0f);
            GL11.glTranslated(0.0, (double)yOff, 0.0);
            GL11.glBegin(3);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, 0.55, 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated(0.0, yOff + 0.55, 0.0);
            GL11.glBegin(3);
            GL11.glVertex3d(-0.375, 0.0, 0.0);
            GL11.glVertex3d(0.375, 0.0, 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPopMatrix();
        }
    }
    
    private void startEnd(final boolean revert) {
        if (revert) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GL11.glEnable(2848);
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GL11.glHint(3154, 4354);
        }
        else {
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
            GL11.glDisable(2848);
            GlStateManager.enableDepth();
            GlStateManager.popMatrix();
        }
        GlStateManager.depthMask(!revert);
    }
    
    public void addEntity(final EntityPlayer e, final ModelPlayer model) {
        this.entities.put(e, new float[][] { { model.bipedHead.rotateAngleX, model.bipedHead.rotateAngleY, model.bipedHead.rotateAngleZ }, { model.bipedRightArm.rotateAngleX, model.bipedRightArm.rotateAngleY, model.bipedRightArm.rotateAngleZ }, { model.bipedLeftLeg.rotateAngleX, model.bipedLeftLeg.rotateAngleY, model.bipedLeftLeg.rotateAngleZ }, { model.bipedRightLeg.rotateAngleX, model.bipedRightLeg.rotateAngleY, model.bipedRightLeg.rotateAngleZ }, { model.bipedLeftLeg.rotateAngleX, model.bipedLeftLeg.rotateAngleY, model.bipedLeftLeg.rotateAngleZ } });
    }
    
    private boolean doesntContain(final EntityPlayer var0) {
        return !Skeleton.mc.world.playerEntities.contains(var0);
    }
}
