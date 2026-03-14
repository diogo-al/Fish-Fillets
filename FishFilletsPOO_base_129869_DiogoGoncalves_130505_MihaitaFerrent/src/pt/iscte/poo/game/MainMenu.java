package pt.iscte.poo.game;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * Nome da Classe: MainMenu
 *
 * Descrição:
 *   Classe resposavel pelo main menu que aparece no incio e no final do jogo
 *
 * Observações:
 *   Classe tambem resposavel pelo ranking de jogadores
 *   A explicacao da calsse aparece de forma mais detalhada ao longo da mesma
 */
public class MainMenu extends JFrame {
    //Dimensao da janela
    private int width = 480;
    private int height = 480;
    private List<String> listaJogadores = new ArrayList<>(); //lista de jogadores para o rank
    private boolean playMusic = true; //boolean para ativar ou desativar a musica
    private static final String separator = "#";
    public static String getSeparator(){return separator;}

    /**
     * Nome do Construtor: MainMenu
     *
     * Descrição:
     *   Resposavel pela criacao e colocacao de objetos no Main Menu
     *
     * Parâmetros:
     *   @param play - Consumer que tem como accept a execucao do jogo em si para qunado clicaco no botao rodar o jogo
     *   @param buttonName - Nome qeu aparece no botao
     *
     * Observações:
     *   Class feita de forma opcional pelos autores do projeto para deixar a experiência do utilizador mais proxima de um joo profissional.
     *   Esta classe tambem e responsavel por mostrar ao utilizador o rank global do jogo
     */
    public MainMenu(Consumer<Void> play, String buttonName){
        //## Alguimas configuracoes
        setPreferredSize(new Dimension(width,height)); //tamanho da janela
        setResizable(false); //nao pode ser redimensionada
        setDefaultCloseOperation(EXIT_ON_CLOSE); //fechar o programa no X
        setTitle("Main Menu");// Nome que aparece em cima
        Image icon = new ImageIcon("icon/Game_Icon.png").getImage(); //icon no canto superior
        setIconImage(icon);
        //###Criar o painel da janela
        JPanel panel = new JPanel();
        panel.setLayout(null); //layout manual
        panel.setBackground(new Color(5, 65, 119)); //cor de fundo do panel

        //### Adicionar elementos a janela
        //&&& Botao de jogar
        JButton playButton = new JButton(buttonName); //criacao de um botao
        playButton.addActionListener(e-> {dispose(); play.accept(null);}); //quando clica no botao corre o consumer
        playButton.setBackground(new Color(100, 200, 230)); //cor de fundo
        playButton.setBounds(165,350,150,50); // diemnsoes e posicao
        playButton.setBorder(BorderFactory.createLineBorder(new Color(254,198,115), 2)); //borda do botao
        playButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); //qunado o rato passa pelo botao fica com o formato de umamao deixando mais profissional
        panel.add(playButton); //adicionamos ao painel

        //&&& Botao de Mute
        JButton mute = new JButton("🔇");
        mute.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
        mute.setBounds(225,405,30,30);
        mute.setBackground(new Color(100, 200, 230));
        mute.addActionListener(e -> {
            if(playMusic) //se tiver a tocar musica para
                SoundManager.getInstance().stopBackgroundSound();
            else // se nao toca
                SoundManager.getInstance().playBackgroundMusic("Audio/retroBackground.wav");
            playMusic=!playMusic; //muda o estado da variavel
        });
        mute.setBorder(BorderFactory.createLineBorder(new Color(254,198,115), 2));
        mute.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(mute);

        //&&& Nome do Jogo Texto grande em cima do rank
        JLabel nomeJogo = new JLabel("Fish Fillets");
        nomeJogo.setText("Fish Fillets");
        nomeJogo.setFont(new Font("Monospaced", Font.BOLD, 28));
        nomeJogo.setForeground(new Color(255, 193, 113));
        nomeJogo.setHorizontalAlignment(SwingConstants.CENTER);
        nomeJogo.setBounds(0, 30, 480, 50);
        panel.add(nomeJogo);

        //&&& Panel Responsavel por mostrar ao utilizador o rank dos jogadores
        JTextPane JTP = new JTextPane();
        JTP.setBounds(140,75,200,250);
        JTP.setBackground(new Color(12, 0, 130));
        JTP.setBorder(BorderFactory.createLineBorder(new Color(254,198,115), 2));
        JTP.setEditable(false);
        JTP.setForeground(new Color(153, 209, 255));
        JTP.setFont(new Font("Monospaced", Font.BOLD, 14));
        JTP.setText(String.format("%-10s %-7s %-5s", "Nome", "Jogadas", "Tempo")); //informacao sobre as linhas da tabela

        //BRINCADEIRA aicionar um peixolas para ficar fofinho
        JLabel label = new JLabel(new ImageIcon("icon/gameIconMM.png"));
        label.setBounds(15,300,200,160);
        panel.add(label);
        //$$$ JOAO FRANGO TINHA DE ESTAR PRESENTE
        JLabel label3 = new JLabel(new ImageIcon("icon/Galo.png"));
        label3.setBounds(310,245,160,200);
        panel.add(label3);

        //Scanner para ler o ficheiro e assim fazer o rank
        //###########################################################################
        //############################   RANK    ####################################
        //###########################################################################
        try{
            Scanner scanner = new Scanner(new File("Rank/rankInfo.txt")); //criar um scannner para ler o ficheiro do rank
            while(scanner.hasNextLine()){ //para cada linha do ficheiro
                String jogador = scanner.nextLine();
                if(validLine(jogador))
                    listaJogadores.add(jogador); //colocamos cada linha na lista de jogadores sse esta for valida
            }
            scanner.close();

            listaJogadores.sort(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    String[] j1 = o1.split(separator);
                    String[] j2 = o2.split(separator);

                    int jogadas1 = Integer.parseInt(j1[1]);
                    int jogadas2 = Integer.parseInt(j2[1]);
                    int tempo1 = Integer.parseInt(j1[2].split(":")[0]) * 60 + Integer.parseInt(j1[2].split(":")[1]);
                    int tempo2 = Integer.parseInt(j2[2].split(":")[0]) * 60 + Integer.parseInt(j2[2].split(":")[1]);

                    if (jogadas1 == jogadas2){ //se as jogadas forem iguais ordenamos pelo tempo
                        if(tempo1 == tempo2)
                            return String.CASE_INSENSITIVE_ORDER.compare(j1[0],j2[0]);
                        return tempo1 - tempo2;
                    }
                    return jogadas1 - jogadas2;
                }
            }); //Classes Anonimas
        }
        catch (FileNotFoundException e){
            listaJogadores.add("Erro a ler o Ficheiro");
            printStr(listaJogadores, JTP); //lista com um unico elemento que e a mensagem de erro
        }
        printStr(listaJogadores, JTP);
        panel.add(JTP);
        //BRINCADEIRA aicionar um peixolas para ficar fofinho
        JLabel label2 = new JLabel(new ImageIcon("icon/peixePalhaco.png"));
        label2.setBounds(300,35,200,160);
        panel.add(label2);

        JLabel label4 = new JLabel(new ImageIcon("icon/CODY.png"));
        label4.setBounds(2,0,178,199);
        panel.add(label4);

        JLabel luzes1 = new JLabel(new ImageIcon("icon/luzes.gif"));
        luzes1.setBounds(-200,-50,900,240);
        panel.add(luzes1);

        JLabel luzes2 = new JLabel(new ImageIcon("icon/luzes.gif"));
        luzes2.setBounds(-200,200,900,240);
        panel.add(luzes2);
        add(panel);
        pack();
        setVisible(true);
        SoundManager.getInstance().playBackgroundMusic("Audio/retroBackground.wav");
    }

    /**
     * Nome da Função: printStr
     *
     * Descrição:
     *   Funcao que escreve no JTextPane o rank dos jogadores
     *
     * Parâmetros:
     *   @param jtp - JTextPane para colocar o rank
     *   @param line - Lista de strings para colocar no rank
     *
     * Observações:
     *   Garante com uma variavel i que apenas mostra os 10 primeiros, caso de eror mostra mensagem de erro no proprio JTP
     */
    private void printStr(List<String> line, JTextPane jtp){
        if(line.isEmpty()) jtp.setText("Seja o primeiro a jogar!!");
        else if(line.getFirst().equals("Erro a ler o Ficheiro")){ //se o a linha tiver o Erro a ler o fciheiro entao houve um erro e mostramos isos no rank
            jtp.setText("Ficheiro novo criado");
            return;
        }
        int i = 0;
        for(String aux : line){ //corremos todos os jogadores que estao no ficheiro e conseguentemente na lista
            if(i == 10) break;
            String[] info = aux.split(separator); // dividimos as informacoes num array
            if(info[0].length() >= 10) info[0] = info[0].substring(0,9);
            jtp.setText(jtp.getText() + "\n" + String.format("%-10s %-7s %-5s", info[0], info[1], info[2])); // Colocamos as Strings formatadas no JTextPane pas as mostras
            i++;
        }
    }

    /**
     * Nome da Função: validLine
     *
     * Descrição:
     *   Funcao que recebe uma linha dor ankl e verifica se esta e valida ou nao
     *
     * Parâmetros:
     *   @param line - linha do rank
     *
     * Retorno:
     *   @return true caso seja uma linha valida
     *
     */
    private static boolean validLine(String line){
        if(line.split(separator).length != 3 || line.split(separator)[2].split(":").length !=2) return false;
        String[] time = line.split(separator)[2].split(":");
        try{
            int min = Integer.parseInt(time[0]);
            int sec = Integer.parseInt(time[1]);
            if(sec > 60 || sec < 0 || min < 0) return false;
        }
        catch (NumberFormatException e){
            return false;
        }
        return true;
    }
}
