package objects;

import pt.iscte.poo.game.Room;

public class Key extends GameObject{
    public Key(Room room) {
        super(room);
    }

    @Override
    public String getName() {
        return hasExtra()? "key" + getRoom().getExtra() : "key";
    }

    @Override
    public int getLayer() {
        return 1;
    }
}
