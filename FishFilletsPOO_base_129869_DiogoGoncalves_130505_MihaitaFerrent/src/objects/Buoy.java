package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Buoy extends Moveable{
    public Buoy(Room room) {
        super(room);
    }

    @Override
    public String getName() {
        return "buoy";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    /**
     * Nome da Função: fall
     *
     * Descrição:
     *   Override da funcao fall da classe Moveable, pois a boia tem uma "Gravidade" diferente
     *
     * Observações:
     *   garantimos que se em cima tiver um objeto moveable os dois caem
     */
    @Override
    public void fall() { //funcao para fazer os objetos cair quando for necessario
        if (isWaterUpper(getPosition())) {
            move(Direction.UP.asVector(), null);
        }
        //Se tiver um objeto moveable em cima ela cai com o objeto
        else if(getRoom().getObjectByPoint(new Point2D(getPosition().getX(), getPosition().getY() - 1)) instanceof Moveable m && !(m instanceof Buoy)) { super.fall();m.fall();}
    }

    private boolean isWaterUpper(Point2D p){ //verifcar se eme baico tem agua, ou seja se pode cair
        return getRoom().getObjectByPoint(new Point2D(p.getX(), p.getY() - 1)) == null || getRoom().getObjectByPoint(new Point2D(p.getX(), p.getY() - 1)).getName().equals("water");
    }
}
