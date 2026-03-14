package objects;

import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.game.SoundManager;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public abstract class GameObject implements ImageTile {
    private Point2D position;
    private Room room;
    private boolean isHeavy = false;
    private boolean hasExtra = false;


    public GameObject(Room room) {
        this.room = room;
    }

    /*##################################################################
    ##########################  GETTERS   #############################
    ##################################################################*/
    public Room getRoom() {return room;}
    public boolean isHeavy() {return isHeavy;}
    public boolean hasExtra() {return hasExtra;}
    @Override
    public Point2D getPosition() {return position;}

    /*##################################################################
    ##########################  SETTERS   #############################
    ##################################################################*/
    public void setPosition(Point2D position) {this.position = position;}
    public void setRoom(Room room) {this.room = room;}
    public void turnHeavy() {this.isHeavy = true;}
    public void setHasExtra(boolean hasExtra) {this.hasExtra = hasExtra;}
}
