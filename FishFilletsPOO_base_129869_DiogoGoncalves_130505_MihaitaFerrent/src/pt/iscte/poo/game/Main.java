package pt.iscte.poo.game;

import pt.iscte.poo.gui.ImageGUI;

public class Main {

	public static void main(String[] args) {
        System.setProperty("sun.java2d.uiScale", "1.0");
        new MainMenu((NA) -> { //Usamos consumer que recebe void para assim conseguir fazer de forma eficiente a passagem do menu para o jogo
            ImageGUI gui = ImageGUI.getInstance();
            GameEngine engine = new GameEngine();
            gui.setStatusMessage("Good luck!");
            gui.registerObserver(engine);
            gui.go();
        }, "✨PLAY✨");
	}
	
}
