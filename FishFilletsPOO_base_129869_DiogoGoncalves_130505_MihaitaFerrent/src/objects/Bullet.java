package objects;

import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;

public class Bullet extends GameObject {
    private int dirX = -1;
    private boolean canVanish = false;

    public boolean CanVanish() {return canVanish;}
    public void setCanVanish(boolean canVanish) {this.canVanish = canVanish;}

    public Bullet(Room room){
        super(room);
    }

    public void moveBala(){             // posicao para onde a bala vai
        Point2D pos = getPosition();
        int newX = pos.getX() + dirX;
        Point2D dest = new Point2D(newX, pos.getY());
        if(getRoom().getObjectByPoint(dest) instanceof GameCharacter) ((GameCharacter) getRoom().getObjectByPoint(dest)).kill();
        if(!(getRoom().getObjectByPoint(dest) instanceof Water) || !GameEngine.insideBoard(dest)) {
            canVanish = true;
        }
        else setPosition(new Point2D(newX, pos.getY()));
        ImageGUI.getInstance().update();
    }

    @Override
    public String getName(){
        return "Bala";
    }

    @Override
    public int getLayer(){
        return 1;
    }
}
