package cat.yoink.xanax.main.clickgui;

public interface GuiBase
{
    void drawScreen(final int mouseX, final int mouseY, final int windowX, final int windowY, final boolean self);

    void mouseClicked(final int mouseX, final int mouseY, final int mouseButton, final boolean self);

    void mouseReleased(final int mouseX, final int mouseY, final int state);

    void keyTyped(final char typedChar, final int keyCode);

    void onGuiClosed();
}
