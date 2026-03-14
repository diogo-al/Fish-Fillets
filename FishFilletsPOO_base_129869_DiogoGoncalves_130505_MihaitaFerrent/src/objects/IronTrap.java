package objects;

import pt.iscte.poo.game.Room;

public class IronTrap extends GameObject {

    public IronTrap(Room room) {
        super(room);
    }

    @Override
    public String getName() {
        return hasExtra()? "ironTrapDoor" + getRoom().getExtra() : "ironTrapDoor";
    }

    @Override
    public int getLayer() {
        return 1;
    }

}
