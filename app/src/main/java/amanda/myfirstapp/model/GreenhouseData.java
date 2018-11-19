package amanda.myfirstapp.model;

public class GreenhouseData {
    Info info;
    Actions actions;

    public GreenhouseData(Info info, Actions actions){
        this.info = info;
        this.actions = actions;
    }

    public Info getInfo() {
        return info;
    }

    public Actions getActions() {
        return actions;
    }
}
