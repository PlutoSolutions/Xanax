package cat.yoink.xanax.main.clickgui;

import cat.yoink.xanax.main.font.CFontRenderer;
import cat.yoink.xanax.main.module.Category;
import cat.yoink.xanax.main.module.ModuleManager;
import cat.yoink.xanax.main.util.GuiUtil;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public final class ClickGUI extends GuiScreen
{
    public static final ClickGUI INSTANCE = new ClickGUI();

    private final List<CategoryButton> buttons = new ArrayList<>();
    private final int w = 380;
    private final int h = 245;
    private int x = 150;
    private int y = 40;
    private int dragX, dragY;
    private boolean dragging;

    public ClickGUI()
    {
        final Category[] values = Category.values();
        for (int i = 0; i < values.length; i++)
        {
            this.buttons.add(new CategoryButton(values[i], this.x + 10 + i * 60, this.y + 34, 60, 15, this.x, this.y));
        }
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks)
    {
        if (this.dragging)
        {
            this.x = this.dragX + mouseX;
            this.y = this.dragY + mouseY;
        }

        final boolean outline = ModuleManager.INSTANCE.getSetting("ClickGUI", "Outline").toBoolean().getValue();

        final float[] hue = new float[]{(float) (System.currentTimeMillis() % 11520L) / 11520.0f};
        final Color c = new Color(Color.HSBtoRGB(hue[0], 1.0f, 1.0f));

        GuiUtil.drawSmoothRect(this.x, this.y, this.w, this.h, 3, new Color(52, 52, 52).getRGB(), outline, c.getRGB());
        GuiUtil.drawRect(this.x + 10, this.y + 50, this.w - 20, this.h - 60, new Color(43, 43, 43).getRGB(), outline, c.getRGB());
        GuiUtil.drawRect(this.x + 15, this.y + 70, this.w - 30, this.h - 85, new Color(34, 34, 34).getRGB(), outline, c.getRGB());

        final CategoryButton selected = this.buttons.stream().filter(CategoryButton::isSelected).findAny().orElse(null);

        if (selected != null)
        {
            if (selected.getTab() > 0)
            {
                GuiUtil.drawSmoothRect(this.x + 15, this.y + 57, 10, 10, 3, new Color(34, 34, 34).getRGB(), outline, c.getRGB());
                CFontRenderer.TEXT.drawString("<", this.x + 16, this.y + 57.5f, -1);
            }

            if (selected.getTab() < selected.getButtons().size() - 5)
            {
                GuiUtil.drawSmoothRect(this.x + 355, this.y + 57, 10, 10, 3, new Color(34, 34, 34).getRGB(), outline, c.getRGB());
                CFontRenderer.TEXT.drawString(">", this.x + 356.5f, this.y + 57.5f, -1);
            }
        }

        CFontRenderer.TITLE.drawCenteredString("XANAX", this.x + this.w / 2f, this.y + 5, c.getRGB());

        if (ModuleManager.INSTANCE.getSetting("ClickGUI", "Closing").toEnum().is("Button") || ModuleManager.INSTANCE.getSetting("ClickGUI", "Closing").toEnum().is("Both"))
        {
            GuiUtil.drawSmoothRect(this.x + this.w - 15, this.y + 5, 10, 10, 3, new Color(34, 34, 34).getRGB());
            CFontRenderer.TEXT.drawString("X", this.x + this.w - 13, this.y + 6, -1);
        }

        if (this.dragging)
        {
            for (int i = 0; i < this.buttons.size(); i++)
            {
                final CategoryButton button = this.buttons.get(i);
                button.setX(this.x + 10 + i * 60);
                button.setY(this.y + 34);
            }
        }

        this.buttons.forEach(button -> button.drawScreen(mouseX, mouseY, this.x, this.y, true));
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton)
    {
        if (GuiUtil.isHover(this.x, this.y, this.w, 30, mouseX, mouseY) && mouseButton == 0)
        {
            this.dragging = true;
            this.dragX = this.x - mouseX;
            this.dragY = this.y - mouseY;
        }

        if ((ModuleManager.INSTANCE.getSetting("ClickGUI", "Closing").toEnum().is("Button") || ModuleManager.INSTANCE.getSetting("ClickGUI", "Closing").toEnum().is("Both")) && GuiUtil.isHover(this.x + this.w - 15, this.y + 5, 10, 10, mouseX, mouseY))
        {
            this.dragging = false;
            this.mc.displayGuiScreen(null);
        }

        this.buttons.forEach(button -> button.mouseClicked(mouseX, mouseY, mouseButton, true));
    }

    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state)
    {
        this.dragging = false;

        this.buttons.forEach(buttons -> buttons.mouseReleased(mouseX, mouseY, state));
    }

    @Override
    protected void keyTyped(final char typedChar, final int keyCode)
    {
        if (keyCode == 1 && ModuleManager.INSTANCE.getSetting("ClickGUI", "Closing").toEnum().is("Keyboard") || ModuleManager.INSTANCE.getSetting("ClickGUI", "Closing").toEnum().is("Both"))
            this.mc.displayGuiScreen(null);

        this.buttons.forEach(button -> button.keyTyped(typedChar, keyCode));
    }

    @Override
    public void onGuiClosed()
    {
        ModuleManager.INSTANCE.getModule("ClickGUI").disable();
        this.dragging = false;

        this.buttons.forEach(CategoryButton::onGuiClosed);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public List<CategoryButton> getButtons()
    {
        return this.buttons;
    }
}
