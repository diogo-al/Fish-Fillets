package objects;

import pt.iscte.poo.game.Room;

public class Cup extends Moveable{
    public Cup(Room room){
        super(room);
    }

    @Override
    public String getName(){return hasExtra()? "cup" + getRoom().getExtra() : "cup";}

    @Override
    public int getLayer() {
        return 1;
    }
}
