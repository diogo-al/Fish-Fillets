package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public class Stone extends Moveable{
    private boolean hasKrab = false;
    public Stone(Room room){super(room); setHasExtra(true); turnHeavy();}

    @Override
    public String getName(){return hasExtra()? "stone" + getRoom().getExtra() : "stone";}

    @Override
    public int getLayer() {
        return 1;
    }

    public boolean hasKrab() {return hasKrab;}
    public void setHasKrab(boolean hasKrab) {this.hasKrab = hasKrab;}

    /**
     * Nome da Função: createKrab
     *
     * Descrição:
     *   Funcao resposavel por criar um caranguejo qunado ha ummovimento da pedra
     *
     * Observações:
     *   usa se uma variavel hasKrab apra verificar se ja foi solto um caranguejo daquela pedra
     */
    public void createKrab(){
        if(hasKrab) return; //se ja houver nao se faz nada
        hasKrab = true;

        GameObject above = getRoom().getObjectByPoint(new Point2D(getPosition().getX(),getPosition().getY() - 1)); //objeto em cima
        if(above != null && above instanceof Water){ //se nao for null e for agua ou krab pode nascer
            Krab krab = new Krab(getRoom());
            krab.setPosition(above.getPosition());
            getRoom().addObject(krab);
        }
    }
}
