package cat.yoink.xanax.main.module;

public enum Category
{
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    WORLD("World"),
    RENDER("Render"),
    MISC("Misc"),
    CLIENT("Client");

    private final String name;

    Category(final String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }
}
