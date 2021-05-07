// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.gui.click;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Iterator;
import cat.yoink.xanax.client.module.Module;
import cat.yoink.xanax.client.module.ModuleManager;
import java.util.ArrayList;
import cat.yoink.xanax.client.module.Category;
import net.minecraft.util.ResourceLocation;
import java.util.List;
import cat.yoink.xanax.client.MinecraftInstance;

public class CategoryButton implements MinecraftInstance
{
    private final List<ModuleButton> buttons;
    private final ResourceLocation image;
    private final Category category;
    private int x;
    private int y;
    private final int w;
    private final int h;
    private boolean selected;
    private int scrollModule;
    
    public CategoryButton(final Category category, final int x, final int y, final int w, final int h, final ResourceLocation image, final int windowX, final int windowY) {
        this.buttons = new ArrayList<ModuleButton>();
        this.category = category;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.image = image;
        this.selected = false;
        int iMod = 0;
        for (final Module module : ModuleManager.INSTANCE.getModules(category)) {
            this.buttons.add(new ModuleButton(module, windowX, windowY + 40 + 20 * iMod, 80, 20, this, windowX, windowY));
            ++iMod;
        }
    }
    
    public void render(final int mouseX, final int mouseY, final int scrollWheel, final int windowX, final int windowY) {
        GlStateManager.enableBlend();
        CategoryButton.mc.getTextureManager().bindTexture(this.image);
        if (this.selected) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
        else {
            GlStateManager.color(0.7f, 0.7f, 0.7f, 1.0f);
        }
        Gui.drawModalRectWithCustomSizedTexture(this.x + 10, this.y + 10, 0.0f, 0.0f, this.w - 20, this.h - 20, (float)(this.w - 20), (float)(this.h - 20));
        GlStateManager.disableBlend();
        for (int i = 0; i < this.buttons.size(); ++i) {
            final ModuleButton button = this.buttons.get(i);
            button.setX(windowX);
            button.setY(windowY + 40 + 20 * i);
        }
        if (this.selected) {
            this.doScroll(mouseX, mouseY, scrollWheel, windowX, windowY);
            int iMod = 0;
            int modIndex = 0;
            for (final ModuleButton button2 : this.buttons) {
                if (modIndex < this.scrollModule) {
                    ++modIndex;
                    if (!button2.isSelected()) {
                        continue;
                    }
                    button2.render(mouseX, mouseY, scrollWheel, windowX, windowY, false);
                }
                else if (iMod >= 7) {
                    if (!button2.isSelected()) {
                        continue;
                    }
                    button2.render(mouseX, mouseY, scrollWheel, windowX, windowY, false);
                }
                else {
                    button2.setY(this.y + 40 + 20 * iMod);
                    button2.render(mouseX, mouseY, scrollWheel, windowX, windowY, true);
                    ++iMod;
                }
            }
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.isHover(this.x, this.y + 10, this.w, this.h - 10, mouseX, mouseY) && mouseButton == 0) {
            ClickGUI.INSTANCE.getButtons().forEach(button -> button.selected = false);
            this.selected = true;
        }
        if (this.selected) {
            int iMod = 0;
            int modIndex = 0;
            for (final ModuleButton button2 : this.buttons) {
                if (modIndex < this.scrollModule) {
                    ++modIndex;
                    button2.mouseClicked(mouseX, mouseY, mouseButton, false);
                }
                else if (iMod >= 7) {
                    button2.mouseClicked(mouseX, mouseY, mouseButton, false);
                }
                else {
                    button2.mouseClicked(mouseX, mouseY, mouseButton, true);
                    ++iMod;
                }
            }
        }
    }
    
    public void mouseReleased(final int mouseX, final int mouseY) {
        if (this.selected) {
            this.buttons.forEach(button -> button.mouseReleased(mouseX, mouseY));
        }
    }
    
    public void keyTyped(final char typedChar, final int keyCode) {
        if (this.selected) {
            this.buttons.forEach(button -> button.keyTyped(typedChar, keyCode));
        }
    }
    
    private void doScroll(final int mouseX, final int mouseY, final int scrollWheel, final int windowX, final int windowY) {
        if (scrollWheel < 0) {
            if (this.isHover(windowX, windowY + 40, 80, 180, mouseX, mouseY)) {
                if (this.scrollModule >= this.buttons.size() - 7) {
                    return;
                }
                ++this.scrollModule;
            }
        }
        else if (scrollWheel > 0 && this.isHover(windowX, windowY + 40, 80, 180, mouseX, mouseY)) {
            if (this.scrollModule <= 0) {
                return;
            }
            --this.scrollModule;
        }
    }
    
    private boolean isHover(final int X, final int Y, final int W, final int H, final int mX, final int mY) {
        return mX >= X && mX <= X + W && mY >= Y && mY <= Y + H;
    }
    
    public void setSelected(final boolean selected) {
        this.selected = selected;
    }
    
    public Category getCategory() {
        return this.category;
    }
    
    public List<ModuleButton> getButtons() {
        return this.buttons;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
}
