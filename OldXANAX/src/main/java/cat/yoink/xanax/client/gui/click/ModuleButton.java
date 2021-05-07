// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.gui.click;

import java.awt.Color;
import cat.yoink.xanax.client.util.font.CFontRenderer;
import java.util.Iterator;
import cat.yoink.xanax.client.gui.click.settings.EnumSettingButton;
import cat.yoink.xanax.client.setting.EnumSetting;
import cat.yoink.xanax.client.gui.click.settings.NumberSettingButton;
import cat.yoink.xanax.client.setting.NumberSetting;
import cat.yoink.xanax.client.gui.click.settings.BooleanSettingButton;
import cat.yoink.xanax.client.setting.BooleanSetting;
import cat.yoink.xanax.client.setting.Setting;
import cat.yoink.xanax.client.gui.click.settings.DrawnButton;
import cat.yoink.xanax.client.gui.click.settings.BindButton;
import java.util.ArrayList;
import cat.yoink.xanax.client.module.Module;
import java.util.List;
import cat.yoink.xanax.client.MinecraftInstance;

public class ModuleButton implements MinecraftInstance
{
    private final List<SettingButton> buttonList;
    private final CategoryButton parent;
    private final Module module;
    private int x;
    private int y;
    private final int w;
    private final int h;
    private boolean selected;
    private int scrollSetting;
    
    public ModuleButton(final Module module, final int x, final int y, final int w, final int h, final CategoryButton parent, final int windowX, final int windowY) {
        this.buttonList = new ArrayList<SettingButton>();
        this.module = module;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.parent = parent;
        this.buttonList.add(new BindButton(parent, module, windowX + 80, windowY + 40, 200, 20));
        this.buttonList.add(new DrawnButton(parent, module, windowX + 80 + 100, windowY + 40, 200, 20));
        int iSet = 1;
        for (final Setting setting : module.getSettings()) {
            SettingButton button = null;
            switch (setting.getType()) {
                case BOOLEAN: {
                    button = new BooleanSettingButton(parent, module, windowX + 80, windowY + 40 + iSet * 20, 200, 20, (BooleanSetting)setting);
                    break;
                }
                case NUMBER: {
                    button = new NumberSettingButton(parent, module, windowX + 80, windowY + 40 + iSet * 20, 200, 20, (NumberSetting)setting);
                    break;
                }
                case ENUM: {
                    button = new EnumSettingButton(parent, module, windowX + 80, windowY + 40 + iSet * 20, 200, 20, (EnumSetting)setting);
                    break;
                }
            }
            this.buttonList.add(button);
            ++iSet;
        }
    }
    
    public void render(final int mouseX, final int mouseY, final int scrollWheel, final int windowX, final int windowY, final boolean self) {
        if (self) {
            CFontRenderer.INSTANCE.drawCenteredString(this.module.getName(), this.x + this.w / 2.0f, (float)(this.y + 5), this.module.isEnabled() ? new Color(255, 255, 255).getRGB() : new Color(200, 200, 200).getRGB());
        }
        int iSet1 = 0;
        for (final SettingButton button : this.buttonList) {
            button.setX(windowX + 80);
            button.setY(windowY + 40 + iSet1 * 20);
            ++iSet1;
        }
        if (this.selected) {
            this.doScroll(mouseX, mouseY, scrollWheel, windowX, windowY);
            int iSet2 = 0;
            int setIndex = 0;
            for (final SettingButton button2 : this.buttonList) {
                if (setIndex < this.scrollSetting) {
                    ++setIndex;
                }
                else {
                    if (iSet2 >= 7) {
                        continue;
                    }
                    button2.setY(windowY + 40 + 20 * iSet2);
                    button2.render(mouseX, mouseY);
                    ++iSet2;
                }
            }
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton, final boolean self) {
        if (this.isHover(this.x, this.y, this.w, this.h, mouseX, mouseY) && self) {
            if (mouseButton == 0) {
                this.module.toggle();
            }
            if (mouseButton == 1 || mouseButton == 0) {
                for (final ModuleButton moduleButton : this.parent.getButtons()) {
                    moduleButton.selected = false;
                }
                this.selected = true;
            }
        }
        if (this.selected) {
            this.buttonList.forEach(settingButton -> settingButton.mouseClicked(mouseX, mouseY, mouseButton));
        }
    }
    
    public void mouseReleased(final int mouseX, final int mouseY) {
        if (this.selected) {
            this.buttonList.forEach(button -> button.mouseReleased(mouseX, mouseY));
        }
    }
    
    public void keyTyped(final char typedChar, final int keyCode) {
        if (this.selected) {
            this.buttonList.forEach(button -> button.keyTyped(typedChar, keyCode));
        }
    }
    
    private void doScroll(final int mouseX, final int mouseY, final int scrollWheel, final int windowX, final int windowY) {
        if (scrollWheel < 0) {
            if (this.isHover(windowX + 80, windowY + 40, 200, 180, mouseX, mouseY)) {
                if (this.scrollSetting >= this.buttonList.size() - 7) {
                    return;
                }
                ++this.scrollSetting;
            }
        }
        else if (scrollWheel > 0 && this.isHover(windowX + 80, windowY + 40, 200, 180, mouseX, mouseY)) {
            if (this.scrollSetting <= 0) {
                return;
            }
            --this.scrollSetting;
        }
    }
    
    private boolean isHover(final int X, final int Y, final int W, final int H, final int mX, final int mY) {
        return mX >= X && mX <= X + W && mY >= Y && mY <= Y + H;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public boolean isSelected() {
        return this.selected;
    }
}
