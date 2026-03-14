package objects;

import pt.iscte.poo.game.Room;

public class Trap extends Moveable{
    public Trap(Room room){super(room);}

    @Override
    public String getName(){return hasExtra()? "trap" + getRoom().getExtra() : "trap";}

    @Override
    public int getLayer() {
        return 1;
    }
}
