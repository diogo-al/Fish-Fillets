package objects;

import pt.iscte.poo.game.Room;

public class SeaHorse extends GameObject {

    private static SeaHorse sh = new SeaHorse(null);
    private SeaHorse(Room room) {
        super(room);
    }
    public static SeaHorse getInstance() {
        return sh;
    }
    @Override
    public String getName() {
        return hasExtra()? "seahorse" + getRoom().getExtra() : "seahorse";
    }

    @Override
    public int getLayer() {
        return 1;
    }
}
