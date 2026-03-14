package objects;

import pt.iscte.poo.game.Room;

public class Portal extends GameObject{

    public Portal(Room room) {
        super(room);
    }

    @Override
    public String getName() {
        return hasExtra()? "portal" + getRoom().getExtra() : "portal";
    }

    @Override
    public int getLayer() {
        return 1;
    }
}
