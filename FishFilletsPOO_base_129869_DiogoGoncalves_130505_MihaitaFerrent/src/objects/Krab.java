package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;

public class Krab extends Moveable{
    private boolean isAlive = true;
    private String fileName = "Left";

    public Krab(Room room) {
        super(room);
        setHasExtra(true);
    }

    public boolean isAlive() {return isAlive;}

    @Override
    public String getName() {
        if(!isAlive()) return "KrabDead";
        return getRoom()==null? "Krab" + getFileName() : "Krab" + getFileName() + getRoom().getExtra();
    }

    @Override
    public int getLayer() {
        return 1;
    }

    public void kill(){
        isAlive = false;
        ImageGUI.getInstance().clearImages();
        ImageGUI.getInstance().addImages(getRoom().getObjects());
    }

    public String getFileName() {return fileName;}
    public void setFileName(String fileName) {this.fileName = fileName;}
}
