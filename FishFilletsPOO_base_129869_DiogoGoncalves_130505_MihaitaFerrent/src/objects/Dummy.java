package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;

import javax.swing.*;
import java.io.File;
import java.util.Arrays;

public class Dummy extends GameObject{
    private String[] frases = {
            "Não pescas nada disto",
            "o amor é uma chama que arde sem se ver",
            "Não ligues, há muito peixe no mar",
            "Se a vida te dá algas, faz uma salada",
            "A falta de noção é assustadora",
            "A vida é assim uns dias ta difícil, uns dias não está fácil",
            "Existe vida inteligente neste planeta? Nem sei se há neste...",
            "Eu podia tentar… mas não me apetece.",
            "Ta aí esse bófia sempre a me chatear o juízo",
            "Queres um génio? Chama Afonso Da Cota",
            "Devias ter visto aquele padre maluco a tentar falar connosco",
            "CINCUN? e com fraturas"
    };
    public Dummy(Room room) {
        super(room);
        setHasExtra(true);
    }

    @Override
    public String getName() {
        return hasExtra()? "dummy" + getRoom().getExtra() : "dummy";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    public void talk(){
        ImageGUI gui = ImageGUI.getInstance();
        gui.showMessage("Peixestóteles", frases[(int)(Math.random() * frases.length)]);
    }
}
