package objects;

import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Nome da Classe: Bomb
 *
 * Descrição:
 *   Class do objeto Bomba
 *
 * Observações:
 *   classe nomrmal apenas modifica o fall para saber quando explodir e tem o metodo explode para fazer a explosao
 */
public class Bomb extends Moveable{
    private boolean Kaboom = false;
    private int initialY;

    public Bomb(Room room){
        super(room);
    }

    /*##################################################################
     ##########################  SETTERS   #############################
     ##################################################################*/

    public void setInitialY(int initialY) {this.initialY = initialY;}

    /*##################################################################
     ##########################  GETTERS   #############################
     ##################################################################*/
    @Override
    public String getName(){return hasExtra()? "bomb" + getRoom().getExtra() : "bomb";}
    @Override
    public int getLayer() {return 1;}
    public boolean getKaboom(){return Kaboom;}

    /**
     * Nome da Função: fall
     *
     * Descrição:
     *   Funcao subscrita do metodo fall do moveable, as diferecnca e que alem de cai esta quando certas circustancias explode
     *
     * Observações:
     *   Se nao tiver agua nem peixe debaixo explode
     */
    @Override
    public void fall(){ //funcao para fazer os objetos cair quando for necessario
        if(Moveable.isWaterUnder(this,getPosition())){
            Point2D p = getPosition();
            setPosition(new Point2D(p.getX(), p.getY()+1));
        }
        GameObject above = getRoom().getObjectByPoint(new Point2D(getPosition().getX(), getPosition().getY()+1));

        if(!GameEngine.insideBoard(this.getPosition())){
            getRoom().tradeToWater(this);
        }
        if(!isWaterUnder(this,getPosition()) && !isFishUnder(this,getPosition()) && !((Bomb) this).getKaboom()){
            explode();
        }
    }

    /**
     * Nome da Função: explode
     *
     * Descrição:
     *   funcao resposavel por realmemte fazer a explosao da bomba
     *
     * Observações:
     *   usa-se uma lista de posicoes adjacentes para exploidir essas posicoes
     */
    public void explode(){
        if(getPosition().getY() == initialY) return;
        Kaboom = true; // a bomba ja explodiu
        Room r = getRoom();
        Point2D pos = getPosition();
        List<Point2D> adjacent = new ArrayList<>(); //lista de posicoes adjacentes
        adjacent.add(new Point2D(pos.getX() + 1, pos.getY())); //DIREITA
        adjacent.add(new Point2D(pos.getX() - 1, pos.getY())); //ESQUERDA
        adjacent.add(new Point2D(pos.getX(), pos.getY()-1)); //CIMA
        adjacent.add(new Point2D(pos.getX(), pos.getY() + 1)); //BAIXO

        List<GameObject> objs = r.getObjects();
        for(Point2D p : adjacent){ //para cada posicao expliodimos-a
            GameObject obj = r.getObjectByPoint(p);
            if(obj instanceof IronTrap) continue;
            if(obj.getName().equals("water")) { // se for agua simplesmente metemos uma explosao por cima dessa
                Explosion e = new Explosion(r);
                e.setPosition(p);
                r.addObject(e);
            }
            if(obj instanceof GameCharacter) ((GameCharacter) r.getObjectByPoint(p)).kill(); // se for um GC mata-o
            int index = objs.indexOf(r.getObjectByPoint(p));//subsituimos o objeto por uma explosao visualmente para depois no proximo tick ser passado a agua
            Explosion e = new Explosion(r);
            e.setPosition(p);
            r.getObjects().set(index, e);
        }
        //Mudar a bomba para explosao
        int index = objs.indexOf(r.getObjectByPoint(pos));
        Explosion e = new Explosion(r);
        e.setPosition(pos);
        r.getObjects().set(index, e);

        ImageGUI.getInstance().clearImages();
        ImageGUI.getInstance().addImages(r.getObjects());
    }
}
