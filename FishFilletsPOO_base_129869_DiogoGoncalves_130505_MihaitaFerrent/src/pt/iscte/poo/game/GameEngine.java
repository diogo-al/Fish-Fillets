package pt.iscte.poo.game;

import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;

import objects.*;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

/**
 * Nome da Classe: GameEngine
 *
 * Descrição:
 *   Classe reposavel por correr o jogo em si, trata de receber os inputs do utilizador tratar vitoria e recolhas de informacoes
 *
 * Observações:
 *   Esta classe e tambem resposavel por manter alguns estados dp jogo (i.e Drunk balas musica etc...)
 */
public class GameEngine implements Observer {
	//########## Atributos de salas ##########
	private Map<String,Room> rooms; //Mapa com as rooms
	private Room currentRoom; //room atual
    private int currentRoomNumber; //numero da room
    private int oldCurrentRoomNumber; //numero da sala anterior para o nivel do tubarao


	private int lastTickProcessed = 0; //contador de ticks

    //########## Atributos de Peixes ##########
    private boolean DaBig = true; //que peixe esta a jogar
    private int numeroJogadasBF = 0;
    private int numeroJogadasSF = 0;
    private boolean drunk = false; //se true os controles invertem
    private int octopusAparitions = 0;
    private boolean angry = false;

    //########## Cavalo Marinho ##########
    private  Bullet balaAtiva;//Bala do cavalo marinho

    //########## GUI + Jogador ##########
    private ImageGUI gui;
    private String NomeJogador;

    //########## Musica ##########
    SoundManager dj = SoundManager.getInstance();

    //########## Atributos final ##########
    private static final int NUMBEROFROOMS = 9; //numero de salas totais
    private static final Vector2D[] horizontalVectors = {Direction.LEFT.asVector(), Direction.RIGHT.asVector()};
    private static final String separator = MainMenu.getSeparator();

    /**
     * Nome do Construtor: GameEngine
     *
     * Descrição:
     *   Cria um jogo inicia as rooms, muda a posicaop dos peixes dependendo da room, dispara a bala, e atualiza a GUI
     *
     */
	public GameEngine() {
		rooms = new HashMap<>();
		loadGame();
        currentRoomNumber = 0;
		currentRoom = rooms.get("room0.txt"); // String nivel "room" + int + ".txt"
        if(currentRoom==null){
            ImageGUI.getInstance().showMessage("Algo Correu mal", "[Erro 001] Sala nao existe ou foi corrompida!");
            System.exit(0);
        }
        BigFish.getInstance().setPosition(currentRoom.getBigFishStartingPosition()); // muda a posicao inicial do BF para a da sala respectiva
        SmallFish.getInstance().setPosition(currentRoom.getSmallFishStartingPosition()); //muda a posicao do peixe para a da sala respectiva
        if(currentRoom.containsSeaHorse()){ //se a sala tiver cavalo marinho entao modificamos a sua posicao inical e a sua room
            SeaHorse.getInstance().setPosition(currentRoom.getSeaHorseStartingPosition());
            SeaHorse.getInstance().setRoom(currentRoom);
            fireBullet();
        }
        SmallFish.getInstance().setRoom(currentRoom);
        BigFish.getInstance().setRoom(currentRoom);
        this.gui = ImageGUI.getInstance();
        updateGUI();
	}

    /*##################################################################
     ##########################  GETTERS   #############################
     ##################################################################*/
    public int getOctopusAparitions(){return octopusAparitions;}
    public boolean isAngry() {return angry;}
    public SoundManager getDJ(){return dj;}

    /*##################################################################
     ##########################  SETTERS   #############################
     ##################################################################*/
    public void setOctopusAparitions(int num){this.octopusAparitions=num;}
    public void setOctopusAngry(){angry = true;}
    public void setDrunk(boolean bol){drunk=bol;}
    public void setPlayName(String nome){this.NomeJogador = nome;}


    /**
     * Nome da Função: loadGame
     *
     * Descrição:
     *   Funcao resposavel por adicionar ao mapa o nome da sala e o objeto Room associado para cada linha no Ficheiro que guarda as rooms
     *
     */
	private void loadGame() {
		File[] files = new File("rooms").listFiles();
		for(File f : files) {
			rooms.put(f.getName(),Room.readRoom(f,this));
		}
	}

    /**
     * Nome da Função: update
     *
     * Descrição:
     *   Funcao chamada de meio a meio segundo reposanvel por tratar de aspetos continuos como receber inputs tratar da gravidade verificar se peixes morreram entre outros
     *
     * Parâmetros:
     *   @param source - objeto que notifica o observador
     *
     * Observações:
     *   Usa se algumas funcoes auxiliares para o metodo ficar mais legivel
     */
	@Override
	public void update(Observed source) {
		if (ImageGUI.getInstance().wasKeyPressed()) { // se uma tecla foi clicada....
			int k = ImageGUI.getInstance().keyPressed();
            //Espaco troca de peixe
            if(k == KeyEvent.VK_SPACE) {DaBig = !DaBig; return; } //esapco troca de jogador
            else if(k == KeyEvent.VK_R) resetRoom(); // R reseta o mapa

            if(Direction.isDirection(k)) { // se for uma direcao
                //Vetor de movimento independentemente do peixe
                Vector2D V2d = Direction.directionFor(k).asVector();
                if(drunk) V2d = Direction.forVector(V2d).opposite().asVector();

                //movemos o peixe que tem a vez
                if (DaBig && !BigFish.getInstance().didWin()) moveFish(BigFish.getInstance(), V2d);
                else if(!DaBig && !SmallFish.getInstance().didWin()) moveFish(SmallFish.getInstance(), V2d);

                try {
                    if (BigFish.getInstance().didWin() && SmallFish.getInstance().didWin()) {
                        trataVitoria();
                        return;
                    } //se os peixes ganharam entao chamamos a funcao auxiliar
                } catch (RuntimeException e) {
                    ImageGUI.getInstance().showMessage("Algo Correu mal", e.getMessage());
                    dj.stopBackgroundSound(); // para a musica
                    new MainMenu((v)-> System.exit(0), "CLOSE");
                }
            }
		}
        if((!BigFish.getInstance().isAlive() || !SmallFish.getInstance().isAlive())){ // se um dos peixes morreu
            processTick();
            gui.showMessage("Recomecando", "Um peixe FALECEU");
            resetRoom();
            BigFish.getInstance().Health();
            SmallFish.getInstance().Health();
        }
        if(DaBig) gui.setStatusMessage("Big Fillete: " + numeroJogadasBF + " jogadadas (" + (int)(lastTickProcessed * 0.5) + " s)");
        else gui.setStatusMessage("Small Fillete: " + numeroJogadasSF + " jogadadas (" + (int)(lastTickProcessed * 0.5) + " s)");
        //processa os ticks
		int t = ImageGUI.getInstance().getTicks();
		while (lastTickProcessed < t) {
			processTick();
		}
		ImageGUI.getInstance().update();
	}

    /**
     * Nome da Função: processTick
     *
     * Descrição:
     *   Funcao que trata para cada tick de algumas ‘nuances’, p.e mudar a mensagem em cima com o tempo que esta a demorar o jogo mais as jogadas, tirar esplosoes e meter gravidade
     *   tratar da bala do cavalo marinho (...)
     *
     * Observações:
     *   Usa se algumas funcoes auxiliares para o metodo ficar mais legivel
     */
    private void processTick() {
        if(currentRoom==null)return;
        lastTickProcessed++; //mostra em cima do ecra
        // Aplicar Gravidade aos elementos e tirar explosoes
        GravivadeExplosoes();
        if(currentRoom.containsSeaHorse()) { // se o mapa tiver cavalo marinho entao disparamos bala
            if(balaAtiva == null) fireBullet();
            else {
                balaAtiva.moveBala();
                if (balaAtiva.CanVanish())
                    fireBullet();
            }
        }
        if(currentRoomNumber==99){// se tivermos na sala do tubarao movemos-o
            TralaleroTralala.getInstance().autoMove();
        }
    }

    /**
     * Nome da Função: fireBullet
     *
     * Descrição:
     *   Funcao que dispara a bala do cavalo marinho
     *
     * Observações:
     *   So dispara a bala se a outra saiu do mapa e existe cavalo marinho neste mapa
     */
    private void fireBullet(){ //### Funcao responsavel por disparar a bala do cavalo marinho
        Point2D bulletPos = currentRoom.getSeaHorseStartingPosition().plus(Direction.LEFT.asVector());
        if(balaAtiva == null){
            balaAtiva = new Bullet(currentRoom);
            balaAtiva.setPosition(bulletPos); //mudamos a posicao da bala
            currentRoom.addObjectFirst(balaAtiva);
        }
        else{
            balaAtiva.setPosition(bulletPos);
            balaAtiva.setCanVanish(false);
        }
    }

    /**
     * Nome da Função: updateGUI
     *
     * Descrição:
     *   Funcao que atualiza o desnho do mapa
     *
     */
	public void updateGUI() {
        if (currentRoom != null) {
            ImageGUI.getInstance().clearImages();
            ImageGUI.getInstance().addImages(currentRoom.getObjects());
        }
    }

    /**
     * Nome da Função: rankGame
     *
     * Descrição:
     *   Funcao resposanvel para qunadno o jogo acabar escrever no ficheiro as informacoes do jogador
     *
     * Observações:
     *   true dentro do FileWrite para dar apppend
     *   caso haja erro o programa fecha
     *   Caso um jogador joque com o mesmo nome que esteja no rank o rank desse mesmo jogador e atualizado caso a pontuacao seja melhor
     */
    public void rankGame(){
        boolean found = false;
        List<String> jogadores = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File("Rank/rankInfo.txt"));
            while (scanner.hasNextLine()){
                String[] line = scanner.nextLine().split(separator);
                if(line.length != 3) continue;
                int numSeconds = convertTimetoSec(line[2]);
                if(numSeconds == -1) continue; //caso o tempo esteja em formato invalido ignora a linha
                if(line[0].equals(NomeJogador)){ //se ha uma linha do ficheiro que tem um nome de jogador igual ao novo entao verificamos a pontuacao
                    found = true;
                    if(Integer.parseInt(line[1]) > (numeroJogadasSF + numeroJogadasBF)) line[1] = String.valueOf(numeroJogadasSF + numeroJogadasBF);
                    if(numSeconds > (int)(lastTickProcessed*0.5)) numSeconds = (int)(lastTickProcessed*0.5);
                }
                jogadores.add(line[0]+separator+line[1]+separator+numSeconds);
            }
            scanner.close();

            if(!found) jogadores.add(NomeJogador + separator + (numeroJogadasBF+numeroJogadasBF) + separator + (int)(lastTickProcessed*0.5));

            PrintWriter pw = new PrintWriter(new FileWriter("Rank/rankInfo.txt", false));
            for(String line : jogadores) {
                String[] info = line.split(separator);
                pw.print(info[0] + separator);
                pw.print(info[1] + separator);
                int segundos = Integer.parseInt(info[2]);
                pw.println(String.format("%d:%02d", segundos / 60, segundos % 60));//formata para garantir que temos os sendugos da forma 06 e nao so 6
            }
            pw.close();
        }
        catch(IOException e){
            ImageGUI.getInstance().showMessage("Erro","[Erro 003] Nao foi possivel salvar as informacoes");
        }
    }

    /**
     * Nome da Função: sharkLevel
     *
     * Descrição:
     *   Funcao resposanvel para quando entrar no portal vai incia o nivel do tubarao
     *
     * Observações:
     *   o Nivel do tubarao e a sala 99
     */
    public void sharkLevel(){
        balaAtiva.getRoom().tradeToWater(balaAtiva);
        oldCurrentRoomNumber = currentRoomNumber;
        currentRoomNumber=99;
        currentRoom = rooms.get("room99.txt");

        //poiscao incial dos GC
        TralaleroTralala.getInstance().setPosition(currentRoom.getTTStartingPosition());
        BigFish.getInstance().setPosition(currentRoom.getBigFishStartingPosition());
        SmallFish.getInstance().setPosition(currentRoom.getSmallFishStartingPosition());
        //Mudamos a room dos peixes
        SmallFish.getInstance().setRoom(currentRoom);
        BigFish.getInstance().setRoom(currentRoom);
        TralaleroTralala.getInstance().setRoom(currentRoom);
        //E comecam obviamente num estado nao vitorioso
        BigFish.getInstance().setWin(false);
        SmallFish.getInstance().setWin(false);

        updateGUI(); //atualizamos a iamgem do jogo
    }

    /**
     * Nome da Função: insideBoard
     *
     * Descrição:
     *   Funcao que verificar se um ponto esta dentro do mapa
     *
     */
    public static boolean insideBoard(Point2D p){ return p.getX()>=0 && p.getY() < 10 && p.getX()<10 && p.getY() >= 0; }

    /*##################################################################
     ######################  FUNCOES AUXILIARES   ######################
     ##################################################################*/


    /**
     * Nome da Função: moveKrabs
     *
     * Descrição:
     *   Coloca gravidade na bomba
     *
     * Observações:
     *   E usado um array de vetores horiazontais vindos de Direction para escolher de forma aleatoria para onde o caranguejo vai
     */
    private void moveKrabs(){
        for (GameObject obj : currentRoom.getObjects()) {
            if(obj instanceof Krab k) //procuramos o krab e movemos - o
                k.move(horizontalVectors[(int)(Math.random()*2)], null);
        }
    }

    /**
     * Nome da Função: resetRoom
     *
     * Descrição:
     *   Reseta a room ficando esta com o aspeto orignal
     *
     * Observações:
     *   usamos o readRoom para gerar uma sala nova a aprtir do ficheiro
     */
    private void resetRoom(){
        currentRoom = Room.readRoom(new File("rooms/"+"room" + currentRoomNumber + ".txt"), this); //gerar uma sala nova a partir do ficheiro original
        rooms.put(currentRoom.getName(),currentRoom);
        //mudamos a posicao inicial dos peixes
        SmallFish.getInstance().setPosition(currentRoom.getSmallFishStartingPosition());
        BigFish.getInstance().setPosition(currentRoom.getBigFishStartingPosition());

        SmallFish.getInstance().setRoom(currentRoom);
        BigFish.getInstance().setRoom(currentRoom);
        if(currentRoom.containsSeaHorse()){
            SeaHorse.getInstance().setPosition(currentRoom.getSeaHorseStartingPosition());
            SeaHorse.getInstance().setRoom(currentRoom);
            balaAtiva =null;
        }
        //E comecam obviamente num estado nao vitorioso
        BigFish.getInstance().setWin(false);
        SmallFish.getInstance().setWin(false);
        dj.stopBackgroundSound();
        dj.playBackgroundMusic("./Audio/retroBackground.wav");
        drunk=false;
        currentRoom.setExtra("");
        updateGUI(); // Pintamos a tela do zero
    }

    /**
     * Nome da Função: moveFish
     *
     * Descrição:
     *   Funcao que moveOPeixe que tiver que ser movido
     *
     * Observações:
     *   ESta funcao trata tambem de mudar o texto em cima, incrementar as jogadas e mexer o Krab
     */
    private void moveFish(GameCharacter fish, Vector2D V2d){
        Point2D initPos = fish.getPosition(); //posicao incial do peixe grande
        fish.move(V2d); //movemos o peixe
        Point2D destination = fish.getPosition(); // vamos ver onde ele ficou
        if(!initPos.equals(fish.getPosition())){ //se o peixe se moveu
            if(DaBig) numeroJogadasBF ++; //incrementa o numero de jogadas
            else numeroJogadasSF ++;
            moveKrabs();
        }
        //### Se o peixe ficar no winPoint entao ele ganhou, e troca logo para outro peixe
        if(destination.equals(currentRoom.getWinPoint())) {
            if(fish instanceof SmallFish s) s.setWin(true);
            else if(fish instanceof BigFish b) b.setWin(true);
            DaBig = !DaBig;
        }
    }

    /**
     * Nome da Função: trataVitoria
     *
     * Descrição:
     *   Quando os peixes ganham esta funcao trata de passar de sala ou dar o jogo como acabado
     *
     * Observações:
     *   Trambem trata de chamar a funcao para fazer o rank do jogo e mostrar o main menu caso se tenha completado os niveis todos
     */
    private void trataVitoria(){
        currentRoomNumber++; //passa de 0 a 1, 1 a 2, etc... incrementamos a sala

        if(currentRoomNumber == 100){ // se o nivel do tubarao passou com sucesso
            currentRoomNumber = oldCurrentRoomNumber; //voltamos a sala anterior
            ImageGUI.getInstance().showMessage("TRALALERO TRALALA","UAU 2x1 e ganharam que surpresa");
        }
        if(currentRoomNumber < NUMBEROFROOMS) { //se for uma sala com numero valido
            String nomeProxSala = "room" + currentRoomNumber + ".txt"; //nome da sala
            if(!currentRoom.getExtra().equals("")){
                dj.stopBackgroundSound();
                dj.playBackgroundMusic("./Audio/retroBackground.wav");
            }
            currentRoom = rooms.get(nomeProxSala);// vai para o proximo ficheiro, ou seja, para a proxima room
            if(currentRoom==null)
                throw new RuntimeException("[Erro 001] A próxima sala não existe ou está corrompida!");
            if(currentRoomNumber == oldCurrentRoomNumber){ // se ficarmos na mesma sala e portque tivemos no nivel do tubarao entao apagamos o portal
                for(GameObject obj : currentRoom.getObjects())
                    if(obj instanceof Portal) currentRoom.tradeToWater(obj);
            }
            //Alterar a nova posicao dos peixes
            if(currentRoom.containsSeaHorse()){
                SeaHorse.getInstance().setPosition(currentRoom.getSeaHorseStartingPosition());
                SeaHorse.getInstance().setRoom(currentRoom);
                balaAtiva = null;
            }
            drunk = false;
            BigFish.getInstance().setPosition(currentRoom.getBigFishStartingPosition());
            SmallFish.getInstance().setPosition(currentRoom.getSmallFishStartingPosition());
            //Mudamos a room dos peixes
            SmallFish.getInstance().setRoom(currentRoom);
            BigFish.getInstance().setRoom(currentRoom);
            //E comecam obviamente num estado nao vitorioso
            BigFish.getInstance().setWin(false);
            SmallFish.getInstance().setWin(false);

            updateGUI(); // Pintamos a tela do zero
        }
        else{
            ImageGUI.getInstance().showMessage("VITORIA","Parabens ganhaste o FISH FILETS");
            while (true) {
                String name = ImageGUI.getInstance().showInputDialog("Nome de Jogador", "Digite o nome de jogador para guardar o seu score");
                if (name == null || name.isEmpty()) {
                    gui.showMessage("Erro","Nome Invalido");
                    continue;
                }
                setPlayName(name);
                break;
            }
            rankGame();
            dj.stopBackgroundSound();
            new MainMenu((v)-> System.exit(0), "CLOSE");
        }
    }

    /**
     * Nome da Função: GravidadeExplosoes
     *
     * Descrição:
     *   Aplica gravidade nos objetos e tira explosoes que estejam no mapa
     *
     * Observações:
     *   usa se a funcao tradeToWater que mete o obekto como agua para "Desaparecer"
     */
    private void GravivadeExplosoes(){
        for(GameObject GO : new ArrayList<>(currentRoom.getObjects())){ //como a boia tem um comportamento diferente optamos por fazer um for dado que para o JAVA correr 100/200 ciclos e muito rapido
            if(GO instanceof Buoy b) b.fall();                         // Desta forma fica melhor visualmente
        }
        ArrayList<GameObject> objects = new ArrayList<>(currentRoom.getObjects());
        for (int i = objects.size() - 1; i >= 0; i--) { //usamos uma copia para poder se odificar o array original ao longo do ciclo
            GameObject GO = objects.get(i);
            if(GO instanceof Moveable m && !(GO instanceof Buoy)){
                m.fall();
            }
            if(GO instanceof Explosion e){
                currentRoom.tradeToWater(e);
            }
            if(GO instanceof Krab k && !k.isAlive()){
                currentRoom.tradeToWater(GO);
            }
        }
    }

    /**
     * Nome da Função: convertTimetoSec
     *
     * Descrição:
     *   recebe uma string do tipo mm:ss e passa para o numero de segundos
     *
     * Parâmetros:
     *   @param time - tempo no formato mm:ss
     *
     * Retorno:
     *   @return numero de segundos
     *
     * Observações:
     *   caso o tempo nao venha em formato devolve -1, ou caso o tempo esteja corrompido o mesmo numero sera devolvido
     */
    private int convertTimetoSec(String time){
        if(time.split(":").length != 2) return -1;
        try {
            int min = Integer.parseInt(time.split(":")[0]);
            int sec = Integer.parseInt(time.split(":")[1]);
            return min*60 + sec;
        }
        catch (NumberFormatException e){
            return -1;
        }
    }
}
