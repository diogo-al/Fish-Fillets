package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;

public class BigFish extends GameCharacter {
    private boolean Left = true;
    private boolean win = false;

	private static BigFish bf = new BigFish(null);
	
	private BigFish(Room room) {
		super(room);
	}
    public void setWin(boolean win) {this.win = win;}

    public boolean didWin(){return win;}

	public static BigFish getInstance() {
		return bf;
	}
	
	@Override
	public String getName() {
        if(!isAlive()) {
            ImageGUI.getInstance().update();
            return "bfDead";
        }
		return getRoom()==null? "bigFish" + getFileName() : "bigFish" + getFileName() + getRoom().getExtra();
	}

	@Override
	public int getLayer() {
		return 2;
	}
}
