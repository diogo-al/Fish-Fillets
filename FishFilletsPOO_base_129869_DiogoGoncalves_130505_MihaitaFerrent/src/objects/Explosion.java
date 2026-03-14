package objects;

import pt.iscte.poo.game.Room;

public class Explosion extends GameObject{

    public Explosion(Room room) {
        super(room);
    }

    @Override
    public String getName() {
        return hasExtra()? "explosion" + getRoom().getExtra() : "explosion";
    }

    @Override
    public int getLayer() {
        return 1;
    }
}
