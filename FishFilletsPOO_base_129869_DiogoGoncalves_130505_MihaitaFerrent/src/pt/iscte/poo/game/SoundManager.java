package pt.iscte.poo.game;

import pt.iscte.poo.gui.ImageGUI;

import javax.sound.sampled.*;
import java.io.File;

/**
 * Nome da Classe: SoundManager
 *
 * Descrição:
 *   Classe reposavel por tocar musica de fundo ao longo do jogo
 * Observações:
 *   Foi usado Clip como player para rodar as musicas e optou-se por esta ser um singletton para nao haver mais que uma instancia a tocar musica no mesmo jogo
 */
public class SoundManager {

    private Clip backgroundClip;

    private static SoundManager instance = new SoundManager();
    private SoundManager(){}
    public static SoundManager getInstance() {return instance;}

    /**
     * Nome da Função: playBackgroundMusic
     *
     * Descrição:
     *   Funcao resposavel por reveber um nome de um ficheiro .wav e assim tocar essa musica
     *
     * Parâmetros:
     *   @param fileName - Nome do ficheiro
     *
     * Observações:
     *   Usa-se Clip.LOOP_CONTINUOUSLY para estar sempre a repetir a musica
     */
    public void playBackgroundMusic(String fileName){
        try{
            backgroundClip = AudioSystem.getClip(); //criamos um clip
            backgroundClip.open(AudioSystem.getAudioInputStream(new File(fileName))); //abrimos o ficheiro de musica
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY); //definimos para estar sempre a tocar
            backgroundClip.start(); //comeca a musica
        } catch (Exception e) { //perante algum erro mostrar mensagem de erro
            ImageGUI.getInstance().showMessage("Algo Correu Mal", "[Erro 002] Ficheiro de audio nao encotrado ou corrompido");
        }
    }

    /**
     * Nome da Função: stopBackgroundSound
     *
     * Descrição:
     *   Funcao responsavel por parar a musica
     *
     */
    public void stopBackgroundSound(){
        if(backgroundClip != null && backgroundClip.isRunning()) //se o clip nao for null e a muicsa realmente estiver a tocar entao podemos a parar
            backgroundClip.stop();
    }

}
