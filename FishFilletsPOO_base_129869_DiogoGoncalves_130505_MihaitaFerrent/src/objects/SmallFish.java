package objects;

import pt.iscte.poo.game.Room;

public class SmallFish extends GameCharacter {
    private boolean Left = true;
    private boolean win = false;

	private static SmallFish sf = new SmallFish(null);
	
	private SmallFish(Room room) {
		super(room);
	}
    public boolean didWin(){return win;}
    public void setWin(boolean win) {this.win = win;}
	public static SmallFish getInstance() {
		return sf;
	}
	
	@Override
    public String getName() {
        if(!isAlive()) return "sfDead";
        return getRoom()==null? "smallFish" + getFileName() : "smallFish" + getFileName() + getRoom().getExtra();
    }

	@Override
	public int getLayer() {
		return 2;
	}

}
