package objects;

import pt.iscte.poo.game.Room;

public class SteelVertical extends GameObject {

    public SteelVertical(Room room) {
        super(room);
        turnHeavy();
        setHasExtra(true);
    }

    @Override
    public String getName() {
        return hasExtra()? "steelVertical" + getRoom().getExtra() : "steelVertical";
    }

    @Override
    public int getLayer() {
        return 1;
    }

}