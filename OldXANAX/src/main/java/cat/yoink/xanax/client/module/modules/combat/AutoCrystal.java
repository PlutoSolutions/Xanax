// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.module.modules.combat;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.RenderGlobal;
import java.util.Objects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.CombatRules;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import cat.yoink.xanax.client.friend.FriendManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.GlStateManager;
import java.util.ArrayList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.Explosion;
import net.minecraft.util.math.Vec3d;
import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import net.minecraft.util.NonNullList;
import java.awt.Color;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import java.util.Iterator;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.init.MobEffects;
import cat.yoink.xanax.client.module.modules.misc.AutoGG;
import cat.yoink.xanax.client.module.ModuleManager;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUseEntity;
import java.util.Comparator;
import java.util.function.Predicate;
import net.minecraft.init.Items;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.Entity;
import java.lang.reflect.Field;
import cat.yoink.xanax.client.handling.Mapping;
import net.minecraft.network.play.client.CPacketPlayer;
import cat.yoink.xanax.client.event.PacketEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import cat.yoink.xanax.client.module.Category;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.EntityLivingBase;
import cat.yoink.xanax.client.setting.EnumSetting;
import cat.yoink.xanax.client.setting.NumberSetting;
import cat.yoink.xanax.client.setting.BooleanSetting;
import cat.yoink.xanax.client.module.Module;

public class AutoCrystal extends Module
{
    private final Timer placeTimer;
    private final Timer breakTimer;
    private final Timer manualTimer;
    private final BooleanSetting place;
    private final NumberSetting placeDelay;
    private final NumberSetting placeRange;
    private final BooleanSetting explode;
    private final BooleanSetting packetBreak;
    private final NumberSetting attackFactor;
    private final BooleanSetting rotate;
    private final NumberSetting breakDelay;
    private final NumberSetting breakRange;
    private final NumberSetting breakWallRange;
    private final BooleanSetting opPlace;
    private final BooleanSetting suicide;
    private final BooleanSetting autoSwitch;
    private final BooleanSetting ignoreUseAmount;
    private final NumberSetting wasteAmount;
    private final BooleanSetting facePlaceSword;
    private final NumberSetting targetRange;
    private final NumberSetting minDamage;
    private final NumberSetting facePlace;
    private final NumberSetting breakMaxSelfDamage;
    private final NumberSetting breakMinDmg;
    private final NumberSetting minArmor;
    private final EnumSetting swingMode;
    private final BooleanSetting render;
    private final BooleanSetting renderDmg;
    private final BooleanSetting box;
    private final BooleanSetting outline;
    private final NumberSetting red;
    private final NumberSetting green;
    private final NumberSetting blue;
    private final NumberSetting alpha;
    private final NumberSetting boxAlpha;
    private final NumberSetting lineWidth;
    private final NumberSetting cRed;
    private final NumberSetting cGreen;
    private final NumberSetting cBlue;
    private final NumberSetting cAlpha;
    private EntityLivingBase target;
    private BlockPos pos;
    private int hotBarSlot;
    private boolean armor;
    private boolean armorTarget;
    private float yaw;
    private float pitch;
    private boolean rotating;
    public static ICamera camera;
    
    public AutoCrystal() {
        super("AutoCrystal", Category.COMBAT);
        this.placeTimer = new Timer();
        this.breakTimer = new Timer();
        this.manualTimer = new Timer();
        this.place = this.newSetting(new BooleanSetting("Place", true));
        this.placeDelay = this.newSetting(new NumberSetting("PlaceDelay", 4.0, 0.0, 300.0, 1.0));
        this.placeRange = this.newSetting(new NumberSetting("PlaceRange", 4.0, 0.1, 8.0, 0.1));
        this.explode = this.newSetting(new BooleanSetting("Break", true));
        this.packetBreak = this.newSetting(new BooleanSetting("PacketBreak", true));
        this.attackFactor = this.newSetting(new NumberSetting("AttackVector", 1.0, 1.0, 5.0, 1.0));
        this.rotate = this.newSetting(new BooleanSetting("Rotate", true));
        this.breakDelay = this.newSetting(new NumberSetting("BreakDelay", 4.0, 0.0, 400.0, 1.0));
        this.breakRange = this.newSetting(new NumberSetting("BreakRange", 4.0, 0.1, 8.0, 0.1));
        this.breakWallRange = this.newSetting(new NumberSetting("BreakWallRange", 3.5, 0.1, 8.0, 0.1));
        this.opPlace = this.newSetting(new BooleanSetting("1.13", false));
        this.suicide = this.newSetting(new BooleanSetting("AntiSuicide", true));
        this.autoSwitch = this.newSetting(new BooleanSetting("AutoSwitch", true));
        this.ignoreUseAmount = this.newSetting(new BooleanSetting("IgnoreUseAmount", true));
        this.wasteAmount = this.newSetting(new NumberSetting("WasteAmount", 4.0, 1.0, 5.0, 1.0));
        this.facePlaceSword = this.newSetting(new BooleanSetting("FacePlaceSword", true));
        this.targetRange = this.newSetting(new NumberSetting("TargetRange", 6.0, 1.0, 12.0, 0.5));
        this.minDamage = this.newSetting(new NumberSetting("MinDamage", 4.0, 1.0, 20.0, 0.5));
        this.facePlace = this.newSetting(new NumberSetting("FacePlaceHP", 4.0, 0.0, 36.0, 1.0));
        this.breakMaxSelfDamage = this.newSetting(new NumberSetting("BreakMaxSelf", 4.0, 1.0, 12.0, 0.5));
        this.breakMinDmg = this.newSetting(new NumberSetting("BreakMinDmg", 4.0, 1.0, 7.0, 0.6));
        this.minArmor = this.newSetting(new NumberSetting("MinArmor", 4.0, 0.1, 80.0, 0.1));
        this.swingMode = this.newSetting(new EnumSetting("Swing", "Offhand", new String[] { "Offhand", "MainHand", "None" }));
        this.render = this.newSetting(new BooleanSetting("Render", true));
        this.renderDmg = this.newSetting(new BooleanSetting("RenderDamage", true));
        this.box = this.newSetting(new BooleanSetting("Box", true));
        this.outline = this.newSetting(new BooleanSetting("Outline", true));
        this.red = this.newSetting(new NumberSetting("Red", 200.0, 0.0, 255.0, 1.0));
        this.green = this.newSetting(new NumberSetting("Green", 20.0, 0.0, 255.0, 1.0));
        this.blue = this.newSetting(new NumberSetting("Blue", 20.0, 0.0, 255.0, 1.0));
        this.alpha = this.newSetting(new NumberSetting("Alpha", 100.0, 0.0, 255.0, 1.0));
        this.boxAlpha = this.newSetting(new NumberSetting("BoxAlpha", 200.0, 0.0, 255.0, 1.0));
        this.lineWidth = this.newSetting(new NumberSetting("LineWidth", 1.0, 0.1, 5.0, 0.1));
        this.cRed = this.newSetting(new NumberSetting("OL-Red", 200.0, 0.0, 255.0, 1.0));
        this.cGreen = this.newSetting(new NumberSetting("OL-Green", 20.0, 0.0, 255.0, 1.0));
        this.cBlue = this.newSetting(new NumberSetting("OL-Blue", 20.0, 0.0, 255.0, 1.0));
        this.cAlpha = this.newSetting(new NumberSetting("OL-Alpha", 150.0, 0.0, 255.0, 1.0));
        this.yaw = 0.0f;
        this.pitch = 0.0f;
        this.rotating = false;
    }
    
    @Override
    public void onEnable() {
        this.placeTimer.reset();
        this.breakTimer.reset();
        this.hotBarSlot = -1;
        this.pos = null;
        this.target = null;
        this.armor = false;
        this.armorTarget = false;
    }
    
    @Override
    public void onDisable() {
        this.rotating = false;
    }
    
    @SubscribeEvent
    public void onTickClientTick(final TickEvent.ClientTickEvent event) {
        if (this.nullCheck()) {
            return;
        }
        this.onCrystal();
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent event) {
        if (event.getType().equals(PacketEvent.Type.OUTGOING) && this.rotate.getValue() && this.rotating && event.getPacket() instanceof CPacketPlayer) {
            final CPacketPlayer packet = (CPacketPlayer)event.getPacket();
            try {
                final Field yaw = CPacketPlayer.class.getDeclaredField(Mapping.cPacketPlayerYaw);
                final Field pitch = CPacketPlayer.class.getDeclaredField(Mapping.cPacketPlayerPitch);
                yaw.setAccessible(true);
                pitch.setAccessible(true);
                yaw.set(packet, this.yaw);
                pitch.set(packet, this.pitch);
            }
            catch (Exception ex) {}
            this.rotating = false;
        }
    }
    
    public void onCrystal() {
        this.manualBreaker();
        int crystalCount = 0;
        if (!this.ignoreUseAmount.getValue()) {
            for (final Entity crystal : AutoCrystal.mc.world.loadedEntityList) {
                if (crystal instanceof EntityEnderCrystal) {
                    if (!this.IsValidCrystal(crystal)) {
                        continue;
                    }
                    boolean count = false;
                    final double damage = this.calculateDamage(this.target.getPosition().getX() + 0.5, this.target.getPosition().getY() + 1.0, this.target.getPosition().getZ() + 0.5, (Entity)this.target);
                    if (damage >= this.minDamage.getValue()) {
                        count = true;
                    }
                    if (!count) {
                        continue;
                    }
                    ++crystalCount;
                }
            }
        }
        this.hotBarSlot = -1;
        if (AutoCrystal.mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
            int crystalSlot = (AutoCrystal.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? AutoCrystal.mc.player.inventory.currentItem : -1;
            if (crystalSlot == -1) {
                for (int l = 0; l < 9; ++l) {
                    if (AutoCrystal.mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                        crystalSlot = l;
                        this.hotBarSlot = l;
                        break;
                    }
                }
            }
            if (crystalSlot == -1) {
                this.pos = null;
                this.target = null;
                return;
            }
        }
        if (AutoCrystal.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE && AutoCrystal.mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL) {
            this.pos = null;
            this.target = null;
            return;
        }
        if (this.target == null) {
            this.target = (EntityLivingBase)this.getTarget();
        }
        if (this.target == null) {
            return;
        }
        if (this.target.getDistance((Entity)AutoCrystal.mc.player) > 12.0f) {
            this.target = null;
        }
        final EntityEnderCrystal l_Crystal;
        if ((l_Crystal = (EntityEnderCrystal)AutoCrystal.mc.world.loadedEntityList.stream().filter(this::IsValidCrystal).map(p_Entity -> p_Entity).min(Comparator.comparing(p_Entity -> this.target.getDistance(p_Entity))).orElse(null)) != null && this.explode.getValue() && this.breakTimer.passedMs((long)this.breakDelay.getValue())) {
            this.breakTimer.reset();
            if (this.packetBreak.getValue()) {
                this.rotateTo((Entity)l_Crystal);
                for (int i = 0; i < this.attackFactor.getValue(); ++i) {
                    AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketUseEntity((Entity)l_Crystal));
                }
            }
            else {
                this.rotateTo((Entity)l_Crystal);
                for (int i = 0; i < this.attackFactor.getValue(); ++i) {
                    AutoCrystal.mc.playerController.attackEntity((EntityPlayer)AutoCrystal.mc.player, (Entity)l_Crystal);
                }
            }
            if (this.swingMode.getValue().equalsIgnoreCase("MainHand")) {
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
            }
            else if (this.swingMode.getValue().equalsIgnoreCase("Offhand")) {
                AutoCrystal.mc.player.swingArm(EnumHand.OFF_HAND);
            }
        }
        if (this.placeTimer.passedMs((long)this.placeDelay.getValue()) && this.place.getValue()) {
            this.placeTimer.reset();
            double damage2 = 0.5;
            for (final BlockPos blockPos : this.placePositions((float)this.placeRange.getValue())) {
                if (blockPos != null && this.target != null && AutoCrystal.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(blockPos)).isEmpty() && this.target.getDistance((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ()) <= this.targetRange.getValue() && !this.target.isDead) {
                    if (this.target.getHealth() + this.target.getAbsorptionAmount() <= 0.0f) {
                        continue;
                    }
                    final double targetDmg = this.calculateDamage(blockPos.getX() + 0.5, blockPos.getY() + 1.0, blockPos.getZ() + 0.5, (Entity)this.target);
                    this.armor = false;
                    for (final ItemStack is : this.target.getArmorInventoryList()) {
                        final float green = (is.getMaxDamage() - (float)is.getItemDamage()) / is.getMaxDamage();
                        final float red = 1.0f - green;
                        final int dmg = 100 - (int)(red * 100.0f);
                        if ((float)dmg > this.minArmor.getValue()) {
                            continue;
                        }
                        this.armor = true;
                    }
                    Label_1117: {
                        if (targetDmg < this.minDamage.getValue()) {
                            if (this.facePlaceSword.getValue()) {
                                if (this.target.getAbsorptionAmount() + this.target.getHealth() <= this.facePlace.getValue()) {
                                    break Label_1117;
                                }
                            }
                            else if (!(AutoCrystal.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) && this.target.getAbsorptionAmount() + this.target.getHealth() <= this.facePlace.getValue()) {
                                break Label_1117;
                            }
                            if (this.facePlaceSword.getValue()) {
                                if (!this.armor) {
                                    continue;
                                }
                            }
                            else if (AutoCrystal.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword || !this.armor) {
                                continue;
                            }
                        }
                    }
                    final double selfDmg;
                    if ((selfDmg = this.calculateDamage(blockPos.getX() + 0.5, blockPos.getY() + 1.0, blockPos.getZ() + 0.5, (Entity)AutoCrystal.mc.player)) + (this.suicide.getValue() ? 2.0 : 0.5) >= AutoCrystal.mc.player.getHealth() + AutoCrystal.mc.player.getAbsorptionAmount() && selfDmg >= targetDmg && targetDmg < this.target.getHealth() + this.target.getAbsorptionAmount()) {
                        continue;
                    }
                    if (damage2 >= targetDmg) {
                        continue;
                    }
                    this.pos = blockPos;
                    damage2 = targetDmg;
                }
            }
            if (damage2 == 0.5) {
                this.pos = null;
                this.target = null;
                return;
            }
            if (ModuleManager.INSTANCE.getModule("AutoGG").isEnabled()) {
                final AutoGG autoGG = (AutoGG)ModuleManager.INSTANCE.getModule("AutoGG");
                autoGG.addTargetedPlayer(this.target.getName());
            }
            if (this.hotBarSlot != -1 && this.autoSwitch.getValue() && !AutoCrystal.mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                AutoCrystal.mc.player.inventory.currentItem = this.hotBarSlot;
            }
            if (!this.ignoreUseAmount.getValue()) {
                int crystalLimit = (int)this.wasteAmount.getValue();
                if (crystalCount >= crystalLimit) {
                    return;
                }
                if (damage2 < this.minDamage.getValue()) {
                    crystalLimit = 1;
                }
                if (crystalCount < crystalLimit && this.pos != null) {
                    this.rotateToPos(this.pos);
                    AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(this.pos, EnumFacing.UP, (AutoCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                }
            }
            else if (this.pos != null) {
                this.rotateToPos(this.pos);
                AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(this.pos, EnumFacing.UP, (AutoCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            }
        }
    }
    
    @SubscribeEvent
    public void onRenderWorldLast(final RenderWorldLastEvent event) {
        if (this.nullCheck()) {
            return;
        }
        if (this.pos != null && this.render.getValue() && this.target != null) {
            this.drawBoxESP(this.pos, new Color((float)this.red.getValue() / 255.0f, (float)this.green.getValue() / 255.0f, (float)this.blue.getValue() / 255.0f, (float)this.alpha.getValue() / 255.0f), this.outline.getValue(), new Color((float)this.cRed.getValue() / 255.0f, (float)this.cGreen.getValue() / 255.0f, (float)this.cBlue.getValue() / 255.0f, (float)this.cAlpha.getValue() / 255.0f), (float)this.lineWidth.getValue(), this.outline.getValue(), this.box.getValue(), (int)this.boxAlpha.getValue() / 255);
            if (this.renderDmg.getValue()) {
                final double renderDamage = this.calculateDamage(this.pos.getX() + 0.5, this.pos.getY() + 1.0, this.pos.getZ() + 0.5, (Entity)this.target);
                drawText(this.pos, ((Math.floor(renderDamage) == renderDamage) ? Integer.valueOf((int)renderDamage) : String.format("%.1f", renderDamage)) + "");
            }
        }
    }
    
    private NonNullList<BlockPos> placePositions(final float placeRange) {
        final NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll((Collection)this.getSphere(new BlockPos(Math.floor(AutoCrystal.mc.player.posX), Math.floor(AutoCrystal.mc.player.posY), Math.floor(AutoCrystal.mc.player.posZ)), placeRange).stream().filter(this::canPlaceCrystal).collect(Collectors.toList()));
        return positions;
    }
    
    private boolean IsValidCrystal(final Entity p_Entity) {
        if (p_Entity == null) {
            return false;
        }
        if (!(p_Entity instanceof EntityEnderCrystal)) {
            return false;
        }
        if (this.target == null) {
            return false;
        }
        if (p_Entity.getDistance((Entity)AutoCrystal.mc.player) > this.breakRange.getValue()) {
            return false;
        }
        if (!AutoCrystal.mc.player.canEntityBeSeen(p_Entity) && p_Entity.getDistance((Entity)AutoCrystal.mc.player) > this.breakWallRange.getValue()) {
            return false;
        }
        if (this.target.isDead || this.target.getHealth() + this.target.getAbsorptionAmount() <= 0.0f) {
            return false;
        }
        final double targetDmg = this.calculateDamage(p_Entity.getPosition().getX() + 0.5, p_Entity.getPosition().getY() + 1.0, p_Entity.getPosition().getZ() + 0.5, (Entity)this.target);
        if (isInHole((Entity)AutoCrystal.mc.player) && targetDmg >= 1.0) {
            return true;
        }
        final double selfDmg = this.calculateDamage(p_Entity.getPosition().getX() + 0.5, p_Entity.getPosition().getY() + 1.0, p_Entity.getPosition().getZ() + 0.5, (Entity)AutoCrystal.mc.player);
        final double d = this.suicide.getValue() ? 2.0 : 0.5;
        if (selfDmg + d < AutoCrystal.mc.player.getHealth() + AutoCrystal.mc.player.getAbsorptionAmount() && targetDmg >= this.target.getAbsorptionAmount() + this.target.getHealth()) {
            return true;
        }
        this.armorTarget = false;
        for (final ItemStack is : this.target.getArmorInventoryList()) {
            this.doDamage(is);
        }
        return (targetDmg >= this.breakMinDmg.getValue() && selfDmg <= this.breakMaxSelfDamage.getValue()) || (isInHole((Entity)this.target) && this.target.getHealth() + this.target.getAbsorptionAmount() <= this.facePlace.getValue());
    }
    
    private float calculateDamage(final double posX, final double posY, final double posZ, final Entity entity) {
        final double distancedsize = entity.getDistance(posX, posY, posZ) / 12.0;
        final Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = 0.0;
        try {
            blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        }
        catch (Exception ex) {}
        final double v = (1.0 - distancedsize) * blockDensity;
        final float damage = (float)(int)((v * v + v) / 2.0 * 7.0 * 12.0 + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = this.getBlastReduction((EntityLivingBase)entity, this.getDamageMultiplied(damage), new Explosion((World)AutoCrystal.mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float)finald;
    }
    
    private void manualBreaker() {
        final RayTraceResult result;
        if (this.manualTimer.passedMs(200L) && AutoCrystal.mc.gameSettings.keyBindUseItem.isKeyDown() && AutoCrystal.mc.player.getHeldItemOffhand().getItem() != Items.GOLDEN_APPLE && AutoCrystal.mc.player.inventory.getCurrentItem().getItem() != Items.GOLDEN_APPLE && AutoCrystal.mc.player.inventory.getCurrentItem().getItem() != Items.BOW && AutoCrystal.mc.player.inventory.getCurrentItem().getItem() != Items.EXPERIENCE_BOTTLE && (result = AutoCrystal.mc.objectMouseOver) != null) {
            if (result.typeOfHit.equals((Object)RayTraceResult.Type.ENTITY)) {
                final Entity entity = result.entityHit;
                if (entity instanceof EntityEnderCrystal) {
                    if (this.packetBreak.getValue()) {
                        AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(entity));
                    }
                    else {
                        AutoCrystal.mc.playerController.attackEntity((EntityPlayer)AutoCrystal.mc.player, entity);
                    }
                    this.manualTimer.reset();
                }
            }
            else if (result.typeOfHit.equals((Object)RayTraceResult.Type.BLOCK)) {
                final BlockPos mousePos = new BlockPos((double)AutoCrystal.mc.objectMouseOver.getBlockPos().getX(), AutoCrystal.mc.objectMouseOver.getBlockPos().getY() + 1.0, (double)AutoCrystal.mc.objectMouseOver.getBlockPos().getZ());
                for (final Entity target : AutoCrystal.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(mousePos))) {
                    if (!(target instanceof EntityEnderCrystal)) {
                        continue;
                    }
                    if (this.packetBreak.getValue()) {
                        AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(target));
                    }
                    else {
                        AutoCrystal.mc.playerController.attackEntity((EntityPlayer)AutoCrystal.mc.player, target);
                    }
                    this.manualTimer.reset();
                }
            }
        }
    }
    
    private boolean canPlaceCrystal(final BlockPos blockPos) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 2, 0);
        try {
            if (!this.opPlace.getValue()) {
                if (AutoCrystal.mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && AutoCrystal.mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
                    return false;
                }
                if (AutoCrystal.mc.world.getBlockState(boost).getBlock() != Blocks.AIR || AutoCrystal.mc.world.getBlockState(boost2).getBlock() != Blocks.AIR) {
                    return false;
                }
                for (final Entity entity : AutoCrystal.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost))) {
                    if (entity instanceof EntityEnderCrystal) {
                        continue;
                    }
                    return false;
                }
                for (final Entity entity : AutoCrystal.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2))) {
                    if (entity instanceof EntityEnderCrystal) {
                        continue;
                    }
                    return false;
                }
            }
            else {
                if (AutoCrystal.mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && AutoCrystal.mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
                    return false;
                }
                if (AutoCrystal.mc.world.getBlockState(boost).getBlock() != Blocks.AIR) {
                    return false;
                }
                for (final Entity entity : AutoCrystal.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost))) {
                    if (entity instanceof EntityEnderCrystal) {
                        continue;
                    }
                    return false;
                }
            }
        }
        catch (Exception ignored) {
            return false;
        }
        return true;
    }
    
    public static boolean isInHole(final Entity entity) {
        return isBlockValid(new BlockPos(entity.posX, entity.posY, entity.posZ));
    }
    
    public static boolean isBlockValid(final BlockPos blockPos) {
        return isBedrockHole(blockPos) || isObbyHole(blockPos) || isBothHole(blockPos);
    }
    
    public static boolean isObbyHole(final BlockPos blockPos) {
        for (final BlockPos pos : new BlockPos[] { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down() }) {
            final IBlockState touchingState = AutoCrystal.mc.world.getBlockState(pos);
            if (touchingState.getBlock() == Blocks.AIR || touchingState.getBlock() != Blocks.OBSIDIAN) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isBedrockHole(final BlockPos blockPos) {
        for (final BlockPos pos : new BlockPos[] { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down() }) {
            final IBlockState touchingState = AutoCrystal.mc.world.getBlockState(pos);
            if (touchingState.getBlock() == Blocks.AIR || touchingState.getBlock() != Blocks.BEDROCK) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isBothHole(final BlockPos blockPos) {
        for (final BlockPos pos : new BlockPos[] { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down() }) {
            final IBlockState touchingState = AutoCrystal.mc.world.getBlockState(pos);
            if (touchingState.getBlock() == Blocks.AIR || (touchingState.getBlock() != Blocks.BEDROCK && touchingState.getBlock() != Blocks.OBSIDIAN)) {
                return false;
            }
        }
        return true;
    }
    
    private float getDamageMultiplied(final float damage) {
        final int diff = AutoCrystal.mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }
    
    private List<BlockPos> getSphere(final BlockPos loc, final float r) {
        final ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        final int cx = loc.getX();
        final int cy = loc.getY();
        final int cz = loc.getZ();
        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                int y = cy - (int)r;
                while (true) {
                    final float f = cy + r;
                    if (y >= f) {
                        break;
                    }
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (cy - y) * (cy - y);
                    if (dist < r * r) {
                        final BlockPos l = new BlockPos(x, y, z);
                        circleblocks.add(l);
                    }
                    ++y;
                }
            }
        }
        return circleblocks;
    }
    
    public static void drawText(final BlockPos pos, final String text) {
        GlStateManager.pushMatrix();
        glBillboardDistanceScaled(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, (EntityPlayer)AutoCrystal.mc.player, 1.0f);
        GlStateManager.disableDepth();
        GlStateManager.translate(-(AutoCrystal.mc.fontRenderer.getStringWidth(text) / 2.0), 0.0, 0.0);
        AutoCrystal.mc.fontRenderer.drawStringWithShadow(text, 0.0f, 0.0f, -5592406);
        GlStateManager.popMatrix();
    }
    
    public static void glBillboard(final float x, final float y, final float z) {
        final float scale = 0.02666667f;
        double renderPosX = 0.0;
        double renderPosY = 0.0;
        double renderPosZ = 0.0;
        try {
            final Field rPosX = RenderManager.class.getDeclaredField(Mapping.renderManagerRenderPosX);
            rPosX.setAccessible(true);
            renderPosX = (double)rPosX.get(AutoCrystal.mc.getRenderManager());
            final Field rPosY = RenderManager.class.getDeclaredField(Mapping.renderManagerRenderPosY);
            rPosY.setAccessible(true);
            renderPosY = (double)rPosY.get(AutoCrystal.mc.getRenderManager());
            final Field rPosZ = RenderManager.class.getDeclaredField(Mapping.renderManagerRenderPosZ);
            rPosZ.setAccessible(true);
            renderPosZ = (double)rPosZ.get(AutoCrystal.mc.getRenderManager());
        }
        catch (Exception ex) {}
        GlStateManager.translate(x - renderPosX, y - renderPosY, z - renderPosZ);
        GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-AutoCrystal.mc.player.rotationYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(AutoCrystal.mc.player.rotationPitch, (AutoCrystal.mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-scale, -scale, scale);
    }
    
    public static void glBillboardDistanceScaled(final float x, final float y, final float z, final EntityPlayer player, final float scale) {
        glBillboard(x, y, z);
        final int distance = (int)player.getDistance((double)x, (double)y, (double)z);
        float scaleDistance = distance / 2.0f / (2.0f + (2.0f - scale));
        if (scaleDistance < 1.0f) {
            scaleDistance = 1.0f;
        }
        GlStateManager.scale(scaleDistance, scaleDistance, scaleDistance);
    }
    
    private EntityPlayer getTarget() {
        EntityPlayer closestPlayer = null;
        for (final EntityPlayer entity : AutoCrystal.mc.world.playerEntities) {
            if (AutoCrystal.mc.player != null && !AutoCrystal.mc.player.isDead && !entity.isDead && entity != AutoCrystal.mc.player && entity.getDistance((Entity)AutoCrystal.mc.player) <= 12.0f) {
                if (FriendManager.INSTANCE.isFriend(entity.getName())) {
                    continue;
                }
                this.armorTarget = false;
                for (final ItemStack is : entity.getArmorInventoryList()) {
                    this.doDamage(is);
                }
                if (isInHole((Entity)entity) && entity.getAbsorptionAmount() + entity.getHealth() > this.facePlace.getValue() && !this.armorTarget && this.minDamage.getValue() > 2.200000047683716) {
                    continue;
                }
                if (closestPlayer == null) {
                    closestPlayer = entity;
                }
                else {
                    if (closestPlayer.getDistance((Entity)AutoCrystal.mc.player) <= entity.getDistance((Entity)AutoCrystal.mc.player)) {
                        continue;
                    }
                    closestPlayer = entity;
                }
            }
        }
        return closestPlayer;
    }
    
    private void doDamage(final ItemStack is) {
        final float green = (is.getMaxDamage() - (float)is.getItemDamage()) / is.getMaxDamage();
        final float red = 1.0f - green;
        final int dmg = 100 - (int)(red * 100.0f);
        if ((float)dmg > this.minArmor.getValue()) {
            return;
        }
        this.armorTarget = true;
    }
    
    private float getBlastReduction(final EntityLivingBase entity, final float damageI, final Explosion explosion) {
        float damage = damageI;
        if (entity instanceof EntityPlayer) {
            final EntityPlayer ep = (EntityPlayer)entity;
            final DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float)ep.getTotalArmorValue(), (float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            int k = 0;
            try {
                k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            }
            catch (Exception ex) {}
            final float f = MathHelper.clamp((float)k, 0.0f, 20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.isPotionActive(MobEffects.RESISTANCE)) {
                damage -= damage / 4.0f;
            }
            damage = Math.max(damage, 0.0f);
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float)entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }
    
    private void drawBoxESP(final BlockPos pos, final Color color, final boolean secondC, final Color secondColor, final float lineWidth, final boolean outline, final boolean box, final int boxAlpha) {
        if (box) {
            this.drawBox(pos, new Color(color.getRed(), color.getGreen(), color.getBlue(), boxAlpha));
        }
        if (outline) {
            this.drawBlockOutline(pos, secondC ? secondColor : color, lineWidth);
        }
    }
    
    private void drawBox(final BlockPos pos, final Color color) {
        final AxisAlignedBB bb = new AxisAlignedBB(pos.getX() - AutoCrystal.mc.getRenderManager().viewerPosX, pos.getY() - AutoCrystal.mc.getRenderManager().viewerPosY, pos.getZ() - AutoCrystal.mc.getRenderManager().viewerPosZ, pos.getX() + 1 - AutoCrystal.mc.getRenderManager().viewerPosX, pos.getY() + 1 - AutoCrystal.mc.getRenderManager().viewerPosY, pos.getZ() + 1 - AutoCrystal.mc.getRenderManager().viewerPosZ);
        AutoCrystal.camera.setPosition(Objects.requireNonNull(AutoCrystal.mc.getRenderViewEntity()).posX, AutoCrystal.mc.getRenderViewEntity().posY, AutoCrystal.mc.getRenderViewEntity().posZ);
        if (AutoCrystal.camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + AutoCrystal.mc.getRenderManager().viewerPosX, bb.minY + AutoCrystal.mc.getRenderManager().viewerPosY, bb.minZ + AutoCrystal.mc.getRenderManager().viewerPosZ, bb.maxX + AutoCrystal.mc.getRenderManager().viewerPosX, bb.maxY + AutoCrystal.mc.getRenderManager().viewerPosY, bb.maxZ + AutoCrystal.mc.getRenderManager().viewerPosZ))) {
            setGL();
            RenderGlobal.renderFilledBox(bb, color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
            GL11.glDisable(2848);
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }
    
    private static void setGL() {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
    }
    
    private void drawBlockOutline(final BlockPos pos, final Color color, final float linewidth) {
        final IBlockState iblockstate = AutoCrystal.mc.world.getBlockState(pos);
        if (AutoCrystal.mc.world.getWorldBorder().contains(pos)) {
            assert AutoCrystal.mc.getRenderViewEntity() != null;
            final Vec3d interp = interpolateEntity(AutoCrystal.mc.getRenderViewEntity(), AutoCrystal.mc.getRenderPartialTicks());
            drawBlockOutline(iblockstate.getSelectedBoundingBox((World)AutoCrystal.mc.world, pos).grow(0.0020000000949949026).offset(-interp.x, -interp.y, -interp.z), color, linewidth);
        }
    }
    
    public static void drawBlockOutline(final AxisAlignedBB bb, final Color color, final float linewidth) {
        final float red = color.getRed() / 255.0f;
        final float green = color.getGreen() / 255.0f;
        final float blue = color.getBlue() / 255.0f;
        final float alpha = color.getAlpha() / 255.0f;
        setGL();
        GL11.glLineWidth(linewidth);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
    
    private void rotateTo(final Entity entity) {
        if (this.rotate.getValue()) {
            final float[] angle = this.calcAngle(AutoCrystal.mc.player.getPositionEyes(AutoCrystal.mc.getRenderPartialTicks()), entity.getPositionVector());
            this.yaw = angle[0];
            this.pitch = angle[1];
            this.rotating = true;
        }
    }
    
    private void rotateToPos(final BlockPos pos) {
        if (this.rotate.getValue()) {
            final float[] angle = this.calcAngle(AutoCrystal.mc.player.getPositionEyes(AutoCrystal.mc.getRenderPartialTicks()), new Vec3d((double)(pos.getX() + 0.5f), (double)(pos.getY() - 0.5f), (double)(pos.getZ() + 0.5f)));
            this.yaw = angle[0];
            this.pitch = angle[1];
            this.rotating = true;
        }
    }
    
    private float[] calcAngle(final Vec3d from, final Vec3d to) {
        final double difX = to.x - from.x;
        final double difY = (to.y - from.y) * -1.0;
        final double difZ = to.z - from.z;
        final double dist = MathHelper.sqrt(difX * difX + difZ * difZ);
        return new float[] { (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0), (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difY, dist))) };
    }
    
    public static Vec3d interpolateEntity(final Entity entity, final float time) {
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * time, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * time, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * time);
    }
    
    static {
        AutoCrystal.camera = (ICamera)new Frustum();
    }
    
    private static class Timer
    {
        private long time;
        
        private Timer() {
            this.time = -1L;
        }
        
        public boolean passedMs(final long ms) {
            return this.getMs(System.nanoTime() - this.time) >= ms;
        }
        
        public long getPassedTimeMs() {
            return this.getMs(System.nanoTime() - this.time);
        }
        
        public void reset() {
            this.time = System.nanoTime();
        }
        
        public long getMs(final long time) {
            return time / 1000000L;
        }
    }
}
