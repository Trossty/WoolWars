package woolwars.woolwars.enums;

public enum TeamType {

    NONE("&7None","&7"),
    RED("&cRed","&c"),
    BLUE("&9Blue","&9");

    private String coloredName;
    private String color;

    TeamType(String coloredName, String color){
        this.coloredName = coloredName;
        this.color = color;
    }

    public String getColoredName() {
        return coloredName;
    }

    public String getColor() {
        return color;
    }

}
