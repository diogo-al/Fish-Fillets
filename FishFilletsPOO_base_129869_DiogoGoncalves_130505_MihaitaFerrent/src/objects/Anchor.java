package objects;

import pt.iscte.poo.game.Room;

public class Anchor extends Moveable{

    public Anchor(Room room){
        super(room);
        turnHeavy();
    }

    @Override
    public String getName(){return hasExtra()? "anchor" + getRoom().getExtra() : "anchor";}

    @Override
    public int getLayer() {
        return 1;
    }

}
