package objects;

import pt.iscte.poo.game.Room;

public class Whiskey extends GameObject{
    public Whiskey(Room room) {
        super(room);
    }

    @Override
    public String getName() {
        return "whiskey";
    }

    @Override
    public int getLayer() {
        return 1;
    }
}
