package objects;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

/**
 * Nome da Classe: Handlers
 *
 * Descrição:
 *   Classe auxiliar resposavel por guardar os metodos usados pelo canMove da classe Moveable para manter o codigo organizado e legivel
 *
 * Observações:
 *  Metodos são static, pois canMOve e static, este final tem assinatura static para ser possivel ser acedido, por exemplo, pelo GameCharacter
 */

public class Handlers {

    /**
     * Nome da Função: trataInteracoes
     *
     * Descrição:
     *   Funcao dedicada a tratar interacoes entre um objeto e outro, (p.e) Key abre a porta, galo muda o mapa, etc
     *
     * Parâmetros:
     *   @param GO - Objeto de destino com oq vamos interagir
     *   @param obj - Objeto que interage
     *
     * Retorno:
     *   @return true se pode haver movimento para esse objeto
     *
     * Observações:
     *   Usa outras funcoes auxiliares para facilitar e leitura do codigo
     */
    public static boolean trataInteracoes(GameObject obj, GameObject GO){
        switch (GO.getName()){
            case "key" -> {return trataKey((Key) GO);}
            case "portal" -> { return trataPortal(obj); }
            case "galo" -> { return trataGalo(obj); }
            case "whiskey" -> { return trataWhiskey(obj, (Whiskey) GO); }
        }
        if(GO instanceof Dummy d){ //contato com dummy apenas provoca a fala deste, nao sendo possivel o atravessar
            d.talk();
            return false;
        }
        return false;
    }

    /**
     * Nome da Função: trataKey
     *
     * Descrição:
     *   Funcao dedicada a tratar a interacao com a Key
     *
     * Parâmetros:
     *   @param GO - Key com que se vai interagir
     *
     * Retorno:
     *   @return true, pois apanhamos a chave e podemos ir para onde ela estava
     *
     * Observações:
     *   procuramos pela porta nos objetos e apagamo-la
     */
    private static boolean trataKey(Key GO){
        GO.getRoom().tradeToWater(GO); //apagamos a key
        for(GameObject gmObj : GO.getRoom().getObjects())
            if(gmObj instanceof IronTrap){
                gmObj.getRoom().tradeToWater(gmObj); //apagar porta
            }
        return true;
    }

    /**
     * Nome da Função: trataPortal
     *
     * Descrição:
     *   Funcao dedicada a tratar a interacao com o Portal
     *
     * Parâmetros:
     *   @param obj - Objeto que esta a interagir com o portal
     *
     * Retorno:
     *   @return false, pois nao vamos para cima do portal, apenas vai se para outro mapa
     *
     * Observações:
     *   Mostramos Pop up com uma pergunta e dependedo da resposta fazemos algo
     */
    private static boolean trataPortal(GameObject obj){
        if(!(obj instanceof GameCharacter)) return false;
        String opcao = ImageGUI.getInstance().showInputDialog("AVISO","Tens mesmo a certeza REI? [sim/nao]"); //mostra pop up
        if(opcao == null) return false; //se a string e null ou seja se clicou no cancel entao nao fazemos nada
        opcao = opcao.toLowerCase().trim(); // deixamos sem espacoes e tudo minusculo
        if(opcao.equals("sim")){ //se for sim vamos para o nivel especial
            obj.getRoom().getEngine().sharkLevel();
        }
        else if (opcao.equals("nao")) {
            ImageGUI.getInstance().showMessage("puff hahahaha", "Fraquinho");
        }
        else{
            ImageGUI.getInstance().showMessage("PARABENS", "Bato Palmas");
        }
        return false;
    }

    /**
     * Nome da Função: trataGalo
     *
     * Descrição:
     *   Funcao dedicada a tratar a interacao com o Galo
     *
     * Parâmetros:
     *   @param obj - Objeto que esta a interagir com o Galo
     *
     * Retorno:
     *   @return false, pois nao vamos para cima do Galo, apenas muda a imagem de alguns objetos
     *
     * Observações:
     *   Muda a muscia de fundo e a skin de alguns objetos
     */
    private static boolean trataGalo(GameObject obj){
        if(obj instanceof GameCharacter){ //se quem esta a interagir e um GC
            obj.getRoom().setExtra("Natal"); //mudamos o extra das skins para o extra de natal
            obj.getRoom().getEngine().getDJ().stopBackgroundSound(); //paramos a musica atual
            obj.getRoom().getEngine().getDJ().playBackgroundMusic("Audio/retroNavidade.wav"); //tocamos a musica de natal
            ImageGUI.getInstance().showMessage("NATAL", "\uD83C\uDF1F CÓCÓRICÓÓÓ QUE O NATAL JÁ CHEGOU!\n" +
                    "\uD83C\uDF1F QUE HAJA MUITO BACALHAU, SONHOS E ZERO CÓDIGOS A DAR BUGS!\n" + "Feliz Natal, ó campeão!\uD83D\uDC14"); //Pop UP
        }
        return false;
    }

    /**
     * Nome da Função: trataWhiskey
     *
     * Descrição:
     *   Funcao dedicada a tratar a interacao com o Whiskey
     *
     * Parâmetros:
     *   @param obj - Objeto que esta a interagir com o Whiskey
     *
     * Retorno:
     *   @return false, pois nao vamos para cima do Whiskey
     *
     * Observações:
     *   Muda a muscia de fundo e coloca os peixes como Drunk, ou seja os controlos invertem
     */
    private static boolean trataWhiskey(GameObject obj, Whiskey whiskey){
        if(!(obj instanceof GameCharacter)) return false;
        obj.getRoom().getEngine().getDJ().stopBackgroundSound();
        obj.getRoom().getEngine().getDJ().playBackgroundMusic("Audio/blues.wav");
        obj.getRoom().getEngine().setDrunk(true); //muda a caracteristica do jogo
        ImageGUI.getInstance().showMessage("Boa Pedrada","⚠️ O peixe ativou o modo BLUES! Controles invertidos, toca a improvisar!");
        obj.getRoom().tradeToWater(whiskey);
        return true;
    }

    /**
     * Nome da Função: trataColisoes
     *
     * Descrição:
     *   Funcao dedicada a tratar as colisoes entre objetos do jogo, reposavel tambem por matar os peixes se assim as regras mandarem
     *
     * Parâmetros:
     *   @param obj - Objeto que esta a interagir com ou objeto
     *   @param GO - objeto com que o obj esta a interagir
     *
     * Retorno:
     *   @return false se nao pudermos ir para cima do objeto que engloba todos os casos e null caso nao seja uma colisao ou caso depois da colisao, seja para continuar as verificacoes
     *
     * Observações:
     *   Muda a muscia de fundo e coloca os peixes como Drunk, ou seja os controlos invertem
     */
    public static Boolean trataColisoes(GameObject obj, GameObject GO){
        if(obj instanceof TralaleroTralala && GO instanceof GameCharacter g){ g.kill();return false; } //tubarao do nivel especial mata o peixe que toca

        if(obj instanceof SmallFish && GO instanceof Krab || obj instanceof Krab && GO instanceof SmallFish) { SmallFish.getInstance().kill(); return false;} //krab contacto com SF mata SF
        if(obj instanceof BigFish && GO instanceof Krab k) {k.kill(); return false;} //peixe grande mata krab
        if(obj instanceof Krab k && GO instanceof BigFish) {k.kill(); return false;}
        if(obj instanceof Trap && GO instanceof BigFish b) {b.kill(); return false;} //se a trap andar e tocar no BigFish este morre
        if(obj instanceof Krab k && GO instanceof Trap) {k.kill(); return false;} //Trap mata Krab
        if(obj instanceof Trap && GO instanceof SmallFish) return true;
        if(obj instanceof GameCharacter g && GO instanceof Bullet) {g.kill(); return false;} //bala mata GC

        return null;
    }

    /**
     * Nome da Função: trataMover
     *
     * Descrição:
     *   Funcao resposanvel pelo movimento de objetos que sao empurrados
     *
     * Parâmetros:
     *   @param obj - Objeto que esta tentar empurrar
     *   @param GO - objeto que vai ser empurrado
     *   @param dir -direcao do movimento
     *   @param GC - GC resposavel pelo movimento incial
     *
     * Retorno:
     *   @return true se mode se movimentar, false se nao pode o movimentar
     *
     * Observações:
     *   e criado o krab nesta funcao caso a pedra se mova
     */
    public static boolean trataMover(GameObject obj, GameObject GO, Vector2D dir, GameCharacter GC){
        if(!(GO instanceof Moveable m)) return false; //objetos nao moviveis nao podem ser empurrados

        Point2D destination = new Point2D(GO.getPosition().getX() + dir.getX(), GO.getPosition().getY() + dir.getY()); //destino do objeto que se vai mover

        if(GO instanceof Krab || obj instanceof Krab) return false; //krab nao pode ser empurrado ou empurrar
        if(GC instanceof SmallFish && (GO.isHeavy())) return false; //peixe pequeno nao empurra objetos pesados
        if(GC instanceof SmallFish && GO instanceof Buoy && dir.getX() == 0) return false; //SF nao empurra verticalmente a boia
        if(GC instanceof BigFish && obj instanceof Moveable && dir.getY() != 0) return false;

        if(Moveable.canMove(m,destination,dir, GC)){ // se o objeto for movivel e esse poder se mover entao movemos o
            m.move(dir,GC);
            if(m instanceof Stone s && dir.getY() == 0) s.createKrab(); //criamos Krab
            return true;
        }
        return false;
    }

    /**
     * Nome da Função: canPassThrough
     *
     * Descrição:
     *   Fucnao auxiliar para ver se um obj pode passar por um objeto GO
     *
     * Parâmetros:
     *   @param GO - objeto para onde vai
     *   @param obj - Objeto que esta a mover-se
     *
     * Retorno:
     *   @return true caso possa passar por dentro
     *
     */
    public static boolean canPassThrough(GameObject obj, GameObject GO){
        String objectName = GO.getName();
        if(obj instanceof Krab && GO instanceof HoledWall) return true;
        if( (obj instanceof SmallFish || obj instanceof Cup) && (objectName.equals("holedWall") || objectName.equals("trap"))) return true;

        return false;
    }
}
