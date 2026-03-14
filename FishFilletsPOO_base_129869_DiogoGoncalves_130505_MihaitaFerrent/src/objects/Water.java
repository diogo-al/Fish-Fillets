package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;

public class Water extends GameObject{

	public Water(Room room) {
		super(room);
	}

	@Override
	public String getName() {
		return "water";
	}

	@Override
	public int getLayer() {
		return 0;
	}

}
