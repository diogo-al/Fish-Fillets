package objects;

import pt.iscte.poo.game.Room;

public class PoliceOctopus extends GameObject{
    public PoliceOctopus(Room room) {
        super(room);
        setHasExtra(true);
    }

    @Override
    public String getName() {
        String nome = "polvoPolicia";
        if(getRoom().getEngine().isAngry()){
            nome = "PoliceOctopusAngry";
        }
        return hasExtra()? nome + getRoom().getExtra() : nome;
    }

    @Override
    public int getLayer() {
        return 1;
    }
}
