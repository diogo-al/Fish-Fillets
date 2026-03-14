package objects;

import pt.iscte.poo.game.Room;

public class HoledWall extends GameObject{
    public HoledWall(Room room) {
        super(room);
    }

    @Override
    public String getName() {
        return hasExtra()? "holedWall" + getRoom().getExtra() : "holedWall";
    }

    @Override
    public int getLayer() {
        return 1;
    }
}
