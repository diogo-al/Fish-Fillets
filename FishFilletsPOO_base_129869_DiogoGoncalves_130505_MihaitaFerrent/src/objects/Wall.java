package objects;

import pt.iscte.poo.game.Room;

public class Wall extends GameObject {

	public Wall(Room room) {
		super(room);
        setHasExtra(true);
	}

	@Override
	public String getName() {
		return hasExtra()? "wall" + getRoom().getExtra() : "wall";
	}	

	@Override
	public int getLayer() {
		return 1;
	}

}
