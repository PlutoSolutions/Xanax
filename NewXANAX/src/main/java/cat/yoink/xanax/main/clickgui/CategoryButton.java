package cat.yoink.xanax.main.clickgui;

import cat.yoink.xanax.main.MinecraftInstance;
import cat.yoink.xanax.main.font.CFontRenderer;
import cat.yoink.xanax.main.module.Category;
import cat.yoink.xanax.main.module.Module;
import cat.yoink.xanax.main.module.ModuleManager;
import cat.yoink.xanax.main.util.GuiUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class CategoryButton implements GuiBase, MinecraftInstance
{
    private final List<ModuleButton> buttons = new ArrayList<>();
    private final Category category;
    private final int w;
    private final int h;
    private int x;
    private int y;
    private boolean selected;
    private int windowX, windowY;
    private int tab;

    public CategoryButton(final Category category, final int x, final int y, final int w, final int h, final int windowX, final int windowY)
    {
        this.category = category;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        final List<Module> modules = ModuleManager.INSTANCE.getModules().stream().filter(m -> m.getCategory().equals(category)).collect(Collectors.toList());
        for (int i = 0; i < modules.size(); i++)
        {
            this.buttons.add(new ModuleButton(modules.get(i), windowX + 30 + i * 65, y + 20, 60, 15, this, windowX, windowY));
        }
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final int windowX, final int windowY, final boolean self)
    {
        this.windowX = windowX;
        this.windowY = windowY;

        if (this.selected)
        {
            final boolean outline = ModuleManager.INSTANCE.getSetting("ClickGUI", "Outline").toBoolean().getValue();

            final float[] hue = new float[]{(float) (System.currentTimeMillis() % 11520L) / 11520.0f};
            final Color c = new Color(Color.HSBtoRGB(hue[0], 1.0f, 1.0f));

            if (outline) GuiUtil.drawSmoothRect(this.x - 1, this.y - 1, this.w + 2, this.h + 2, 2, c.getRGB());
            GuiUtil.drawSmoothRect(this.x, this.y, this.w, this.h + 3, 2, new Color(43, 43, 43).getRGB());
        }

        CFontRenderer.TEXT.drawString(this.category.getName(), this.x + (this.w / 2f) - (CFontRenderer.TEXT.getStringWidth(this.category.getName()) / 2f), this.y + 3, -1);

        if (this.selected)
        {
            int modX = 0;
            for (int i = 0; i < this.buttons.size(); i++)
            {
                if (i < this.tab || i > this.tab + 4)
                {
                    this.buttons.get(i).drawScreen(mouseX, mouseY, windowX, windowY, false);
                    continue;
                }

                final ModuleButton button = this.buttons.get(i);

                button.setX(windowX + 30 + modX * 65);
                button.setY(this.y + 20);
                this.buttons.get(i).drawScreen(mouseX, mouseY, windowX, windowY, true);

                modX++;
            }
        }
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton, final boolean self)
    {
        if (GuiUtil.isHover(this.x, this.y, this.w, this.h, mouseX, mouseY) && mouseButton == 0)
        {
            ClickGUI.INSTANCE.getButtons().forEach(b -> b.selected = false);
            this.selected = true;
        }

        if (mouseButton == 0 && this.selected)
        {
            if (GuiUtil.isHover(this.windowX + 15, this.windowY + 57, 10, 10, mouseX, mouseY) && this.tab > 0)
                this.tab--;
            if (GuiUtil.isHover(this.windowX + 355, this.windowY + 57, 10, 10, mouseX, mouseY) && this.tab < this.buttons.size() - 5)
                this.tab++;
        }

        if (this.selected)
        {
            for (int i = 0; i < this.buttons.size(); i++)
            {
                if (i < this.tab || i > this.tab + 4)
                {
                    this.buttons.get(i).mouseClicked(mouseX, mouseY, mouseButton, false);
                    continue;
                }

                this.buttons.get(i).mouseClicked(mouseX, mouseY, mouseButton, true);
            }
        }
    }

    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state)
    {
        if (this.selected) this.buttons.forEach(button -> button.mouseReleased(mouseX, mouseY, state));
    }

    @Override
    public void keyTyped(final char typedChar, final int keyCode)
    {
        if (this.selected) this.buttons.forEach(button -> button.keyTyped(typedChar, keyCode));
    }

    @Override
    public void onGuiClosed()
    {
        if (this.selected) this.buttons.forEach(ModuleButton::onGuiClosed);
    }

    public void setX(final int x)
    {
        this.x = x;
    }

    public void setY(final int y)
    {
        this.y = y;
    }

    public List<ModuleButton> getButtons()
    {
        return this.buttons;
    }

    public boolean isSelected()
    {
        return this.selected;
    }

    public int getTab()
    {
        return this.tab;
    }
}
