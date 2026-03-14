package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

import java.awt.*;

public class TralaleroTralala extends GameCharacter{
    private static TralaleroTralala tt = new TralaleroTralala(null);
    private boolean left = true;
    private TralaleroTralala(Room room) {
        super(room);
    }

    public static TralaleroTralala getInstance() {
        return tt;
    }

    @Override
    public String getName() {
        return left? "tralaleroTralalaLeft": "tralaleroTralalaRight" ;
    }

    @Override
    public int getLayer() {
        return 1;
    }

    /**
     * Nome da Função: autoMove
     *
     * Descrição:
     *   Funcao resposavel por mover o tubarao de forma inteligente para pereseguir os peixes no nivel especial
     *
     * Observações:
     *   A explicacao do metodo esta ao longo desse
     */
    public void autoMove(){
        //Posicoes dos peixes e do tubarao
        Point2D SFPos = SmallFish.getInstance().getPosition();
        Point2D BFPos = BigFish.getInstance().getPosition();
        Point2D TTPos = getPosition();
        Point2D destination= TTPos;

        //Se a distanncia ao peixe pequeno e maior que a distancia ao grande ou o pequeno ja ganhou queremos ir atras do grande porque e oq esta mais perto
        if(TTPos.distanceTo(SFPos)>TTPos.distanceTo(BFPos) || SmallFish.getInstance().didWin()){
            if(Math.abs(BFPos.getX()-TTPos.getX())> Math.abs(BFPos.getY()-TTPos.getY())){ //se a distancia no eixo horizontal e maior que o yy entao mexemos no xx para aproximarmos cada vez mais
                //Move-se para a esquerda ou para a direita dependendo donde esta
                destination = BFPos.getX()>TTPos.getX()? TTPos.plus(Direction.RIGHT.asVector()) : TTPos.plus(Direction.LEFT.asVector());
            }
            else {
                destination = BFPos.getY()>TTPos.getY()? TTPos.plus(Direction.DOWN.asVector()) : TTPos.plus(Direction.UP.asVector());
            }
        }
        //Vamos atras do peixe pequeno
        else if(TTPos.distanceTo(SFPos)<=TTPos.distanceTo(BFPos) || BigFish.getInstance().didWin()){
            if(Math.abs(SFPos.getX()-TTPos.getX())> Math.abs(SFPos.getY()-TTPos.getY())){
                destination = SFPos.getX()>TTPos.getX()? TTPos.plus(Direction.RIGHT.asVector()) : TTPos.plus(Direction.LEFT.asVector());
            }
            else {
                destination = SFPos.getY()>TTPos.getY()? TTPos.plus(Direction.DOWN.asVector()) : TTPos.plus(Direction.UP.asVector());
            }
        }
        left = Vector2D.movementVector(TTPos, destination).getX() <= 0;

        if(Moveable.canMove(this,destination, new Vector2D(0,0), this)) //No fim se ele se pode mover move-se
            setPosition(destination);
    }
}
