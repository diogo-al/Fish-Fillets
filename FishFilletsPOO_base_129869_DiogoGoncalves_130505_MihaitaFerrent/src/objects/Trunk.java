package objects;

import pt.iscte.poo.game.Room;

public class Trunk extends GameObject{

    public Trunk(Room room){
        super(room);
        setHasExtra(true);
    }

    @Override
    public String getName(){
        return hasExtra()? "trunk" + getRoom().getExtra() : "trunk";
    }

    @Override
    public int getLayer() {
        return 1;
    }
}