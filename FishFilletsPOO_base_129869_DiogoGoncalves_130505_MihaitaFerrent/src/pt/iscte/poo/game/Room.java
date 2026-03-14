package pt.iscte.poo.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import objects.*;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;

/**
 * Nome da Classe: Room
 *
 * Descrição:
 *   Classe que guarda as informações dos objetos pertencentes a essa room entre outras carctersiticas dela como extra e o winpoint
 *
 */
public class Room {
	
	private List<GameObject> objects; //lista de objetos da room
	private String roomName; //nome da sala
	private GameEngine engine; // engine ond eesta essa sala
    //posicao inicial dos GC
	private Point2D smallFishStartingPosition;
	private Point2D bigFishStartingPosition;
    private Point2D TTStartingPosition;
    private Point2D seaHorseStartingPosition;

	private Point2D winPoint; //ponto de vitoria
    private String extra = ""; //extra dos obejtois dessa rrom


	public Room() { //Construtor Inicia a lista de objetos
		objects = new ArrayList<GameObject>();
	}
    /*##################################################################
     ##########################  GETTERS   #############################
     ##################################################################*/
	public String getName() {return roomName;}
    public String getExtra() {return extra;}
    public GameEngine getEngine(){return engine;}
	public List<GameObject> getObjects() {return objects;}
	public Point2D getSmallFishStartingPosition() {return smallFishStartingPosition;}
    public Point2D getTTStartingPosition() {
        return TTStartingPosition;
    }
    public Point2D getBigFishStartingPosition() {
        return bigFishStartingPosition;
    }
	public Point2D getSeaHorseStartingPosition() {
		return seaHorseStartingPosition;
	}
    public Point2D getWinPoint() {return winPoint;}

    /*##################################################################
     ##########################  SETTERS   #############################
     ##################################################################*/
	public void setBigFishStartingPosition(Point2D heroStartingPosition) {this.bigFishStartingPosition = heroStartingPosition;}
    public void setSeaHorseStartingPosition(Point2D heroStartingPosition) {this.seaHorseStartingPosition = heroStartingPosition;}
    public void setTTStartingPosition(Point2D heroStartingPosition) {this.TTStartingPosition = heroStartingPosition;}
    public void setExtra(String str){extra =str;}
    private void setName(String name) {roomName = name;}
	private void setEngine(GameEngine engine) {this.engine = engine;}
	public void setSmallFishStartingPosition(Point2D heroStartingPosition) {this.smallFishStartingPosition = heroStartingPosition;}
    public void setWinPoint(Point2D winPoint) {this.winPoint = winPoint;}

    /**
     * Nome das Funções: addObject, addObjectFirst, removeObject
     *
     * Descrição:
     *   Funcoes que manipulam a lsita de obejtos, seja adicionar novos objetos na lista no fim ou no inicio ou entao remover
     *
     */
	public void addObject(GameObject obj) {
		objects.add(obj);
		engine.updateGUI();
	}
    public void addObjectFirst(GameObject obj) {
        objects.addFirst(obj);
        engine.updateGUI();
    }
	public void removeObject(GameObject obj) {
		objects.remove(obj);
		engine.updateGUI();
	}



    /**
     * Nome da Função: getObjectByPoint
     *
     * Descrição:
     *   A partir de um ponto devolve o objeot que esta nesse ponto
     *
     * Parâmetros:
     *   @param point - poonto para encontraro objeto
     *
     * Retorno:
     *   @return GameObject que esta no ponto point
     *
     * Observações:
     *   so devolve se agua quando se tem a certeza que nao esta la outro objeto sem ser agua
     */
    public GameObject getObjectByPoint(Point2D point){
        GameObject aux = null;
        for(GameObject GO : objects){ //Corremos os objetos todos a procura de um com o ponto pedido
            if(GO.getPosition().equals(point)){
                if(aux instanceof GameCharacter && GO instanceof Water) continue;
                if(aux instanceof Water || aux == null || aux instanceof GameCharacter) aux = GO;
            }
        }
        return aux;
    }

	/**
	 * Nome da Função: readRoom
	 *
	 * Descrição:
	 *   Funcao estatica que a partir de um ficheiro criar uma sala lendo cada caractere desse ficheir e criando o objeto desse caractere
	 *
	 * Parâmetros:
	 *   @param f - Ficheiro da room
	 *   @param engine - GameEngine onde essa room se encontra
	 * Retorno:
	 *   @return Objeto Room referente ao ficheiro dado
	 *
	 * Observações:
	 *   Usou se uma linha extra para definir concretamente a posicao de vitoria para deixar o jogo mais seguro
	 */
	public static Room readRoom(File f, GameEngine engine) {
		Room r = new Room(); //sala vazia
		r.setEngine(engine); //definimos o engine dessa sala
		r.setName(f.getName());

        try {
            Scanner scanner = new Scanner(f); //scannner que vai ler o ficheiro
            int line = 0;
            int col = 0;
            while(scanner.hasNextLine()){ //corremos as linhas todas do ficheiro
                char[] linha = scanner.nextLine().toCharArray(); // guardamos cada linha como um array de caracteres
                if(linha.length == 0) break;
                for(int i = 0; i< 10 ; i++){ //corremos as 10 colunas
                    Point2D point2D = new Point2D(col,line); //criamops um ponto para cada linha/coluna
                    char c = (i < linha.length)? linha[i] : ' '; // como uma saida do lado direito pode deixar espacos vazios temos de ter cuidado

                    //Usamos um switch case para ver todas as hipoteses de tile e respetivo GameObject a usar
                    switch (c){
                        case 'B' -> {
                            r.setBigFishStartingPosition(point2D);
                            r.addObject(BigFish.getInstance());
                        }
                        case 'S' -> {
                            r.setSmallFishStartingPosition(point2D);
                            r.addObject(SmallFish.getInstance());
                        }
                        case 'W' -> {
                            GameObject wall = new Wall(r);
                            wall.setPosition(point2D);
                            r.addObject(wall);
                        }
                        case 'H' -> {
                            GameObject sh = new SteelHorizontal(r);
                            sh.setPosition(new Point2D(col, line));
                            r.addObject(sh);
                        }
                        case 'V' -> {
                            GameObject sh = new SteelVertical(r);
                            sh.setPosition(new Point2D(col, line));
                            r.addObject(sh);
                        }
                        case 'C' -> {
                            GameObject sh = new Cup(r);
                            sh.setPosition(new Point2D(col, line));
                            r.addObject(sh);
                        }
                        case 'R' -> {
                            GameObject sh = new Stone(r);
                            sh.setPosition(new Point2D(col, line));
                            r.addObject(sh);
                        }
                        case 'A' -> {
                            GameObject sh = new Anchor(r);
                            sh.setPosition(new Point2D(col, line));
                            r.addObject(sh);
                        }
                        case 'b' -> {
                            GameObject sh = new Bomb(r);
                            sh.setPosition(new Point2D(col, line));
                            ((Bomb) sh).setInitialY(line);
                            r.addObject(sh);
                        }
                        case 'T' -> {
                            GameObject sh = new Trap(r);
                            sh.setPosition(new Point2D(col, line));
                            r.addObject(sh);
                        }
                        case 'Y' -> {
                            GameObject sh = new Trunk(r);
                            sh.setPosition(new Point2D(col, line));
                            r.addObject(sh);
                        }
                        case 'X' -> {
                            GameObject sh = new HoledWall(r);
                            sh.setPosition(new Point2D(col, line));
                            r.addObject(sh);
                        }
                        case 'I' -> {
                            GameObject sh = new IronTrap(r);
                            sh.setPosition(new Point2D(col, line));
                            r.addObject(sh);
                        }
                        case 'K' -> {
                            GameObject sh = new Key(r);
                            sh.setPosition(new Point2D(col, line));
                            r.addObject(sh);
                        }
                        case 's' -> {
                            r.setSeaHorseStartingPosition(new Point2D(col, line));
                            r.addObject(SeaHorse.getInstance());
                        }
                        case 'D'->{
                            GameObject sh = new Dummy(r);
                            sh.setPosition(new Point2D(col, line));
                            r.addObject(sh);
                        }
                        case 't'->{
                            r.setTTStartingPosition(point2D);
                            r.addObject(TralaleroTralala.getInstance());
                        }
                        case 'P'->{
                            GameObject sh = new Portal(r);
                            sh.setPosition(new Point2D(col, line));
                            r.addObject(sh);
                        }
                        case 'g'->{
                            GameObject sh = new GaloNavidad(r);
                            sh.setPosition(new Point2D(col, line));
                            r.addObject(sh);
                        }
                        case 'w'->{
                            GameObject sh = new Whiskey(r);
                            sh.setPosition(new Point2D(col, line));
                            r.addObject(sh);
                        }
                        case 'O'->{
                            GameObject sh = new Buoy(r);
                            sh.setPosition(new Point2D(col, line));
                            r.addObject(sh);
                        }
                    }
                    col++;
                }
                col = 0;
                line++;
            }
            for(int i = 0; i < 10; i++){ //colocar a agua em todo o mapa
                for(int j = 0; j < 10 ; j++){
                    GameObject water = new Water(r);
                    water.setPosition(new Point2D(j,i));
                    r.addObject(water);
                }
            }
            String[] winner = scanner.nextLine().split(" "); //linha de vitoria
            r.setWinPoint(new Point2D(Integer.parseInt(winner[0]), Integer.parseInt(winner[1])));
            scanner.close(); //Fechamos o scanner
            return r;
        }
        catch (FileNotFoundException e){
            System.err.println("Erro a ler ficheiro de Mapa");
            return null;
        }
	}

    /**
     * Nome da Função: tradeToWater
     *
     * Descrição:
     *   Trasnforma um objeto qualquer em agua para o fazer desaparecer do mapa
     *
     * Parâmetros:
     *   @param GO - Objeto que vai ser modificado
     *
     * Observações:
     *   cria uma agua e suistitui na lista de objetos esse por agua, atualiza a imagem do mapa
     */
    public void tradeToWater(GameObject GO){
        int index = objects.indexOf(GO);
        Water w = new Water(this);
        w.setPosition(GO.getPosition());
        objects.set(index, w);
        ImageGUI.getInstance().clearImages();
        ImageGUI.getInstance().addImages(objects);
    }

    /**
     * Nome da Função: containsSeaHorse
     *
     * Descrição:
     *   Funcao simples para verificar se existe algum cavalo marinho nesta room
     *
     * Retorno:
     *   @return true caso exsite cavalo marinho false o contrario
     *
     */
    public boolean containsSeaHorse(){
        for(GameObject go : objects)
            if(go instanceof SeaHorse) return true;
        return false;
    }
}