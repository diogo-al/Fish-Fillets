package objects;

import pt.iscte.poo.game.Room;

public class GaloNavidad extends GameObject{
    public GaloNavidad(Room room) {
        super(room);
    }

    @Override
    public String getName() {
        return "galo";
    }

    @Override
    public int getLayer() {
        return 1;
    }
}
