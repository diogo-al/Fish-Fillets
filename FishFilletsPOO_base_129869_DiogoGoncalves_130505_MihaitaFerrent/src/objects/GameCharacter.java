package objects;

import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

/**
 * Nome da Classe: GameCharacter
 *
 * Descrição:
 *   Classe abstrata resposanvel por construtir os metodos dos objetos que sao personagens i.e SF, BF Tralalero
 *
 * Observações:
 *   Esta classe e extendida pelos personagens do jogo e da extends de GameObject pois os Personagens sao no seu todo gameObjects
 */
public abstract class GameCharacter extends GameObject {

    private String fileName = "Left"; //personagens comecam a olha para a esquerda
    private boolean alive = true; //comecam vivos
    private final String[] frases = { //algumas frases ditas pelo policia
            "Achas te engraceide?",
            "Estamos a nos esticar portanto?",
            "Tens de ser mais rapido que eu!",
            "Ide para casa apenas...",
            "Max apanha o bandido!",
            "Sr. Holmes, este aqui está suspeito!",
            "Vou chamar o Sr. Frank Tenpenny!"
    };

    /**
     * Nome do Construtor: GameCharacter
     *
     * Descrição:
     *   Construtor simples para inicializar os GC com a sua room
     *
     * Parâmetros:
     *   @param room - room do GC
     *
     */
	public GameCharacter(Room room) {
		super(room);
	}


    /*##################################################################
     ##########################  GETTERS   #############################
     ##################################################################*/
    public String getFileName() {return fileName;}
    public boolean isAlive() {return alive;}
    public void Health() {alive=true;}
    public void kill(){alive = false;}
    @Override
	public int getLayer() {return 2;}

    /**
     * Nome da Função: move
     *
     * Descrição:
     *   Funcao responsavel pelo movimento dos GC
     *
     * Parâmetros:
     *   @param dir - direcao do movimento
     *
     * Observações:
     *   este metodo e tambem responsavel pela aparicao do polvo policia quando o GC tenta sair do mapa por um sítio que não seja o ponto de vitória
     */
	public void move(Vector2D dir){
        if(!isAlive()) return; // se não tiver vivo não faz nada
		Point2D dest = new Point2D(getPosition().getX() + dir.getX(), getPosition().getY() + dir.getY()); //para onde ele quer ir
        if(!GameEngine.insideBoard(dest) && !dest.equals(getRoom().getWinPoint())){ //se o peixe tentar sair do mapa para um sitio que nao e o ponto de vitoria
            PoliceOctopus po = new PoliceOctopus(getRoom());
            po.setPosition(getPosition()); //criamos e definios a localizadao do policia
            if(getRoom().getObjects().getLast() instanceof PoliceOctopus) {
                getRoom().removeObject(getRoom().getObjects().getLast());//se ja existe um na lista de onjetos apagamos
            }
            getRoom().getObjects().addLast(po); // adicionamos a lista de objetos
            ImageGUI.getInstance().clearImages(); //atualizamos a gui
            ImageGUI.getInstance().addImages(getRoom().getObjects());

            //movimenta se no sentido contrario ao do polvo policia para dar a sensacao de ser empurrado
            if(getPosition().getX()==0)
                move(Direction.RIGHT.asVector());
            else if(getPosition().getX() == 9)
                move(Direction.LEFT.asVector());
            else if(getPosition().getY()==0)
                move(Direction.DOWN.asVector());
            else if(getPosition().getY() == 9)
                move(Direction.UP.asVector());
            int aux = getRoom().getEngine().getOctopusAparitions();
            getRoom().getEngine().setOctopusAparitions(aux+1);
            if(getRoom().getEngine().getOctopusAparitions() >= 3) { //pequeno easter egg
                getRoom().getEngine().setOctopusAngry();
                ImageGUI.getInstance().showMessage("Polvo Bofia",frases[(int)(Math.random()* frases.length)]);
            }
            return;
        }
        //muda a imagem do peixe
        if(dir.getX() < 0) {fileName = "Left";}
        else if (dir.getX() > 0){fileName ="Right";}
        else if (dir.getY() > 0){fileName ="Down";}
        else if (dir.getY() < 0){fileName ="Up";}
        if(Moveable.canMove(this,dest, dir, this)) // se ele se puder mover
            setPosition(dest);
	}

}