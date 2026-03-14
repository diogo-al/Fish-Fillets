package objects;

import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

import java.sql.SQLOutput;

/**
 * Nome da Classe: Moveable
 *
 *
 * Descrição:
 *   class abstrata extendida por todos os objetos moviveis esta classe tem funcoes responsaveis pelo movimento tal como o move, canmove e o fall
 *
 *
 * Observações:
 *   Objetos Moveable tem gravidade
 */

public abstract class Moveable extends GameObject{
    /**
     * Nome do Construtor: Moveable
     *
     * Descrição:
     *   Instancia um objeto Moveable numa room dada
     *
     * Parâmetros:
     *   @param room - Room onde o objeto será colocado
     */
    public Moveable(Room room) {
        super(room);
    }

    /**
     * Nome da Função: isWaterUnder
     *
     * Descrição:
     *   Verifica se há água ou espaço vazio imediatamente abaixo do objeto.
     * Parâmetros:
     *   @param obj - objeto a verificar
     *   @param p - posição do objeto
     *   @return true se houver água ou espaço vazio abaixo
     */
    public static boolean isWaterUnder(GameObject obj, Point2D p){ //verifcar se em baixo tem agua, ou seja se pode cair
        return obj.getRoom().getObjectByPoint(new Point2D(p.getX(), p.getY() + 1)) == null || obj.getRoom().getObjectByPoint(new Point2D(p.getX(), p.getY() + 1)).getName().equals("water");
    }
    /**
     * Nome da Função: isFishUnder
     *
     * Descrição:
     *   Verifica se há um GameCharacter abaixo do objeto.
     * Parâmetros:
     *   @param obj - objeto a verificar
     *   @param p - posição do objeto
     *   @return true se houver GameCharacter em baixo
     */
    public boolean isFishUnder(GameObject obj,Point2D p){ //verifcar se em baico tem peixe
        return obj.getRoom().getObjectByPoint(new Point2D(p.getX(), p.getY()+1)) instanceof GameCharacter;
    }

    /**
     * Nome da Função: canMove
     *
     * Descrição:
     *   Esta funcao e responsavel por verificar se um objeto pode se mover para um dado sítio
     *
     * Parâmetros:
     *   @param obj - GameObject que se deseja mover
     *   @param point2D - Posicao para onde se vai mover
     *   @param dir - direcao do movimento
     *   @param GC - GameCharacter que provocou o movimento incial (null caso haja um movimento sem origem num GC (i.e Krab))
     *
     * Retorno:
     *   @return - boolean para confirmar se pode ou não se mover
     *
     * Observações:
     *   Tambem e responsavel por certas interacoes com outros objetos (i.e Galo, Dummy, etc...)
     *   E chamada quase de forma recursiva pelos objetos a frente para assim verificar se num todo o movimento e possivel
     */
    public static boolean canMove(GameObject obj, Point2D point2D, Vector2D dir, GameCharacter GC){
        GameObject GO = obj.getRoom().getObjectByPoint(point2D); //a partir de uma funcao auxiliar vemos o objeto para onde o movimento nos leva
        String objectName = (GO==null)? " " : GO.getName(); // obtemos o nome do objeto tendo em conta que este pode ser null
        if(!(obj instanceof GameCharacter) && objectName.equals(" ")) return false; // objetos que não são GC não podem sair do mapa
        if(!(obj instanceof GameCharacter) && GC instanceof SmallFish && GO instanceof Moveable) return false; //se o peixe pequeno originou o movimento e temos pelo menos dois objetos que vao mover entao nao ha moviemtno

        if(objectName.equals("water") || objectName.equals(" ") || objectName.equals("explosion") || GO instanceof PoliceOctopus)
            return true; // se for agua, vazio, explosao ou Policia pode se mexer

        //Caso tenhamos um dos objetos especiais entao usamos uma funcao dedidcada na classe Handlers para tratar essa interacao
        if(GO instanceof Key || GO instanceof GaloNavidad || GO instanceof Dummy || GO instanceof Whiskey || GO instanceof Portal) return Handlers.trataInteracoes(obj , GO);

        if(Handlers.canPassThrough(obj,GO)) return true;

        if( obj instanceof BigFish && (objectName.equals("trap"))) {BigFish.getInstance().kill(); return false;}
        Boolean colisoes = Handlers.trataColisoes(obj, GO); //Boolean para aceitar null
        if(colisoes != null) return colisoes; // se nao for null entao temos um output e usamos-lo, se for null entao continuamos com a verificacao
        if(GO instanceof Moveable && GC != null) return Handlers.trataMover(obj,GO,dir,GC);

        //Pode se passar caso seja um peixe vitorioso
        if(BigFish.getInstance().didWin() && GO instanceof BigFish) return true;
        if(SmallFish.getInstance().didWin() && GO instanceof SmallFish) return true;

        return false;
    }

    /**
     * Nome da Função: move
     *
     * Descrição:
     *   Funcao responsavel por fazer o movimento em si do objeto esta verifica primeiro se o movimento e possivel
     *   e caso seja entao e colocada posicao como a posicao inicial mais o vetor de movimento
     *
     * Parâmetros:
     *   @param  dir - Vetor de movimento
     *   @param  GC - GameCharacter responsavel pelo movimento inicial
     *
     * Observações:
     *   Esta funcao e em alguns casos e sobrescrita para mudar o seu funcionamento
     *   GC serve para não se perder quem originou o movimento para analisar casos como por exemplo se um peixe pequeno pode empurrar dois objetos
     */
    public void move(Vector2D dir, GameCharacter GC){
        if(canMove(this ,this.getPosition().plus(dir), dir,GC)) {// verifica se pode se mover antes de se mover assim tambem faz com que a funcao canMove seja chamda para o objeto as eguir como se fosse uma recursividade que faz todoas a frente se mexerem
            setPosition(this.getPosition().plus(dir));
            if(this instanceof Krab k){
                if(dir.getX()<0)
                    k.setFileName("Left");
                else if(dir.getX()>0)
                    k.setFileName("Right") ;
            }
        }
    }

    /**
     * Nome da Função: fall
     *
     * Descrição:
     *   Funcao responsavel por mover os objetos devido à gravidade
     *
     * Observações:
     *   Esta funcao e chamada continuamente em cada tick para assim os objetos moviveis terem gravidade
     */
    public void fall(){
        move(Direction.DOWN.asVector(), null);
        //se for um objeto pesado e em baxio estiver um peixe pequeno ele mata o peixe
        GameObject bellow = getRoom().getObjectByPoint(new Point2D(getPosition().getX(), getPosition().getY()+1));
        if(this.isHeavy() && bellow instanceof SmallFish) SmallFish.getInstance().kill();
        if(bellow instanceof GameCharacter b){
            if(getRoom().getObjectByPoint(new Point2D(getPosition().getX(), getPosition().getY()-1)) instanceof Moveable && b instanceof SmallFish) //um peixe pequeno so pode suportar um objeto leve
                b.kill();
            else if (moreThanOneHeavy(getPosition())) {
                b.kill();
            }
        }
        if(bellow instanceof Trunk && this.isHeavy()) getRoom().tradeToWater(bellow); //Tronco nao aguenta com objetos pesados
    }

    /**
     * Nome da Função: moreThanOneHeavy
     *
     * Descrição:
     *   Funcao resposansavel por verificar se o peixe tem mais de dois objetos pesados em cima
     *
     * Parâmetros:
     *   @param p - ponto do primeiro o objeto em cima
     *
     * Retorno:
     *   @return true caso haja masi qeu dois objetos pesados na pilha de objetos em cima dele
     *
     */
    private boolean moreThanOneHeavy(Point2D p){
        int count = 0;
        while(GameEngine.insideBoard(p)){
            if(getRoom().getObjectByPoint(p).isHeavy()) count++;
            if(count == 2) return true;
            p=p.plus(Direction.UP.asVector());
        }
        return false;
    }
}
