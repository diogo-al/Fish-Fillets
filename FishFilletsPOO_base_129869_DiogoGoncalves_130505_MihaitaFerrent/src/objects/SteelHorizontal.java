package objects;

import pt.iscte.poo.game.Room;

public class SteelHorizontal extends GameObject {

	public SteelHorizontal(Room room) {
		super(room);
        setHasExtra(true);
	}

	@Override
	public String getName() {
        return hasExtra()? "steelHorizontal" + getRoom().getExtra() : "steelHorizontal";
	}

	@Override
	public int getLayer() {
		return 1;
	}

}
