// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.config;

import cat.yoink.xanax.client.setting.EnumSetting;
import cat.yoink.xanax.client.setting.NumberSetting;
import cat.yoink.xanax.client.setting.BooleanSetting;
import cat.yoink.xanax.client.setting.Setting;
import cat.yoink.xanax.client.component.Component;
import java.util.Iterator;
import net.minecraftforge.common.MinecraftForge;
import java.util.Collection;
import cat.yoink.xanax.client.friend.FriendManager;
import cat.yoink.xanax.client.component.ComponentManager;
import java.io.IOException;
import cat.yoink.xanax.client.util.FileUtil;
import java.util.stream.Collector;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.function.Function;
import cat.yoink.xanax.client.module.Module;
import cat.yoink.xanax.client.module.ModuleManager;
import java.util.ArrayList;
import java.io.File;
import cat.yoink.xanax.client.MinecraftInstance;

public class Config extends Thread implements MinecraftInstance
{
    public static final File mainFolder;
    private static final String ENABLED_MODULES = "EnabledModule.txt";
    private static final String SETTINGS = "Settings.txt";
    private static final String BINDS = "Binds.txt";
    private static final String HUDCoords = "HUDCoords.txt";
    private static final String DRAWN_MODULES = "DrawnModules.txt";
    private static final String FRIENDS = "Friends.txt";
    
    @Override
    public void run() {
        if (!Config.mainFolder.exists() && !Config.mainFolder.mkdirs()) {
            System.out.println("Failed to create config folder");
        }
        try {
            FileUtil.saveFile(new File(Config.mainFolder.getAbsolutePath(), "EnabledModule.txt"), ModuleManager.INSTANCE.getEnabledModules().stream().map(Module::getName).collect(Collectors.toCollection(ArrayList::new)));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileUtil.saveFile(new File(Config.mainFolder.getAbsolutePath(), "Settings.txt"), getSettings());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileUtil.saveFile(new File(Config.mainFolder.getAbsolutePath(), "Binds.txt"), ModuleManager.INSTANCE.getModules().stream().map(module -> module.getName() + ":" + module.getBind()).collect(Collectors.toCollection(ArrayList::new)));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileUtil.saveFile(new File(Config.mainFolder.getAbsolutePath(), "HUDCoords.txt"), ComponentManager.INSTANCE.getComponents().stream().map(component -> component.getName() + ":" + component.getX() + ":" + component.getY()).collect(Collectors.toCollection(ArrayList::new)));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileUtil.saveFile(new File(Config.mainFolder.getAbsolutePath(), "Friends.txt"), new ArrayList<String>(FriendManager.INSTANCE.getFriends()));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileUtil.saveFile(new File(Config.mainFolder.getAbsolutePath(), "DrawnModules.txt"), ModuleManager.INSTANCE.getModules().stream().map(module -> module.getName() + ":" + module.isDrawn()).collect(Collectors.toCollection(ArrayList::new)));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void loadConfig() {
        if (!Config.mainFolder.exists()) {
            return;
        }
        try {
            for (final String s : FileUtil.loadFile(new File(Config.mainFolder.getAbsolutePath(), "EnabledModule.txt"))) {
                try {
                    final Module module = ModuleManager.INSTANCE.getModule(s);
                    module.setEnabled(true);
                    MinecraftForge.EVENT_BUS.register((Object)module);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (final String s : FileUtil.loadFile(new File(Config.mainFolder.getAbsolutePath(), "Settings.txt"))) {
                try {
                    final String[] split = s.split(":");
                    saveSetting(ModuleManager.INSTANCE.getSetting(split[1], split[0]), split[2]);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (final String s : FileUtil.loadFile(new File(Config.mainFolder.getAbsolutePath(), "Binds.txt"))) {
                try {
                    final String[] split = s.split(":");
                    ModuleManager.INSTANCE.getModule(split[0]).setBind(Integer.parseInt(split[1]));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (final String s : FileUtil.loadFile(new File(Config.mainFolder.getAbsolutePath(), "HUDCoords.txt"))) {
                try {
                    final String[] split = s.split(":");
                    final Component component = ComponentManager.INSTANCE.getComponent(split[0]);
                    component.setX(Integer.parseInt(split[1]));
                    component.setY(Integer.parseInt(split[2]));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (final String s : FileUtil.loadFile(new File(Config.mainFolder.getAbsolutePath(), "DrawnModules.txt"))) {
                try {
                    final String[] split = s.split(":");
                    ModuleManager.INSTANCE.getModule(split[0]).setDrawn(Boolean.parseBoolean(split[1]));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (final String s : FileUtil.loadFile(new File(Config.mainFolder.getAbsolutePath(), "Friends.txt"))) {
                try {
                    FriendManager.INSTANCE.addFriend(s);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
    }
    
    private static ArrayList<String> getSettings() {
        final ArrayList<String> content = new ArrayList<String>();
        for (final Module module : ModuleManager.INSTANCE.getModules()) {
            for (final Setting setting : module.getSettings()) {
                switch (setting.getType()) {
                    case BOOLEAN: {
                        content.add(String.format("%s:%s:%s", setting.getName(), module.getName(), setting.getBoolean()));
                        continue;
                    }
                    case NUMBER: {
                        content.add(String.format("%s:%s:%s", setting.getName(), module.getName(), setting.getDouble()));
                        continue;
                    }
                    case ENUM: {
                        content.add(String.format("%s:%s:%s", setting.getName(), module.getName(), setting.getEnum()));
                        continue;
                    }
                }
            }
        }
        return content;
    }
    
    private static void saveSetting(final Setting setting, final String value) {
        switch (setting.getType()) {
            case BOOLEAN: {
                if (Boolean.parseBoolean(value)) {
                    ((BooleanSetting)setting).enable();
                    break;
                }
                ((BooleanSetting)setting).disable();
                break;
            }
            case NUMBER: {
                ((NumberSetting)setting).setValue(Double.parseDouble(value));
                break;
            }
            case ENUM: {
                ((EnumSetting)setting).setIndex(((EnumSetting)setting).getValues().indexOf(value));
                break;
            }
        }
    }
    
    static {
        mainFolder = new File(Config.mc.gameDir + File.separator + "xanax");
    }
}
