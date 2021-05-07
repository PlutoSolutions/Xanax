// 
// Decompiled by Procyon v0.5.36
// 

package cat.yoink.xanax.client.gui.main;

import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import cat.yoink.xanax.client.MinecraftInstance;

public class CustomButton implements MinecraftInstance
{
    public int buttonWidth;
    public int buttonHeight;
    public int x;
    public int y;
    public String displayString;
    public int id;
    private int timeHovered;
    
    public CustomButton(final int buttonId, final int x, final int y, final int widthIn, final int heightIn, final String buttonText) {
        this.id = buttonId;
        this.x = x;
        this.y = y;
        this.buttonWidth = widthIn;
        this.buttonHeight = heightIn;
        this.displayString = buttonText;
    }
    
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.isHover(this.x, this.y, this.buttonWidth, this.buttonHeight, mouseX, mouseY)) {
            this.timeHovered += 14;
            if (this.timeHovered > this.buttonWidth / 2) {
                this.timeHovered = this.buttonWidth / 2;
            }
        }
        else if (!this.isHover(this.x, this.y, this.buttonWidth, this.buttonHeight, mouseX, mouseY)) {
            this.timeHovered -= 14;
            if (this.timeHovered < 0) {
                this.timeHovered = 0;
            }
        }
        Gui.drawRect(this.x, this.y, this.x + this.buttonWidth, this.y + this.buttonHeight, new Color(0, 0, 0, 112).getRGB());
        Gui.drawRect(this.x + this.buttonWidth / 2 - this.timeHovered, this.y + this.buttonHeight - 1, this.x + this.buttonWidth / 2 + this.timeHovered, this.y + this.buttonHeight, new Color(0, 0, 0, 123).getRGB());
        mc.fontRenderer.drawStringWithShadow(this.displayString, this.x + this.buttonWidth / 2.0f - mc.fontRenderer.getStringWidth(this.displayString) / 2.0f, this.y + this.buttonHeight / 2.0f - mc.fontRenderer.FONT_HEIGHT / 2.0f, new Color(255, 255, 255, 255).getRGB());
    }
    
    private boolean isHover(final int X, final int Y, final int W, final int H, final int mX, final int mY) {
        return mX >= X && mX <= X + W && mY >= Y && mY <= Y + H;
    }
}
