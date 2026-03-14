=====================================================================
   FISH FILLETS NG - PROJETO DE POO (ANO LETIVO 25/26)
   ISCTE-IUL
=====================================================================

ALUNOS:
Diogo Alexandre Leonardo Gonçalves - 129869
Mihaita Danut Ferent - 130505

---------------------------------------------------------------------
INTRODUÇÃO
---------------------------------------------------------------------
O projeto consiste em criar um jogo inspirado no Fish Fillets NG, um jogo clássico de arcade,
onde controlamos 2 peixes, um pequeno e um grande, cada um com as suas características únicas,
resolvêmos puzzles e passamos de sala em sala. Para além dos requisitos base, implementámos também outros objetos,
alterámos todas as imagens, adicionámos um sistema de som e um menu inicial personalizado.
Como pedido em aula, no README vamos apenas abordar elementos diferentes dos pedidos no projeto!

---------------------------------------------------------------------
\Audio
---------------------------------------------------------------------
Criámos um novo diretório chamado 'Audio' que contem as músicas usadas ao longo do correr do jogo.

    *   Blues.wav           -> Musica tocada quando há uma interação com o objeto 'Whiskey'
    *   retroBackground.wav -> Musica tocada no fundo do jogo ate ser trocada por outra música ou até ser 'mutada' pelo jogador
    *   retroNavidade.wav   -> Musica tocada quando há uma interação com o objeto 'galoNavidad'

---------------------------------------------------------------------
\icon
---------------------------------------------------------------------
*   Criámos um novo diretório chamado 'icons' para personalizar a interface do jogo e o menu.

    *   CODY.png & Galo.png -> Relebrámos os nossos momentos de infância, e adicionámos dois amigos do filme Surf's Up que está
                               relacionados com água, o pinguin CODY(CODY.png) e o galo JOÃO(Galo.png)
    *   Game_Icon.png       -> Alteramos o IconImage da interface do jogo do canto superior esquerdo da imagem simples e sem piada do java,
                               para o peixe do lendário jogo MINECRAFT
    *   gameIconMM.png      -> Adicionámos um tubarão do filme "À procura de Nemo" no Main Menu
    *   luzes.png           -> Adicionámos duas luzes de Natal, para deixar o jogo com um tom mais natalício, por estarmos perto do Natal
    *   PeixePalhaco.png    -> Adicionámos também o peixe do jogo original no Main Menu

---------------------------------------------------------------------
\images
---------------------------------------------------------------------
*   Neste diretório realizámos uma reformulação total, alterando TODAS as imagens, não só visualmente, mas também a nível de posicionamento,
colocando os objetos para estarem perfeitamente numa TILE 48x48.

*   Substituímos também algumas das imagens estáticas originais por imagens em formato *GIF* para realizar umas animações
    simples mas que embelezam o jogo.

*   Para além da utilização de GIFs, também adicionámos direções para os objetos: BigFish, SmallFish, Krab e TralaleroTralala que ao adicionar
    o sufixo para direções no plano horizontal (LEFT & RIGHT) mudam a imagem para estar direcionado para o sítio mais indicado, e ainda
    adicionámos as direções no plano vertical (UP & DOWN) apenas para o BigFish e SmallFish.

****MODO NATAL****
*   Realizámos uma mecânica especial, quando o jogador interaje com o objeto 'GaloNovidad', o jogo 'ativa' o modo natalício, alterando a
    musica de fundo e adicionando o sufixo 'Natal' para a maioria dos objetos (bigFishDown, bigFishLeft, bigFishRight, bigFishUp, buoy,
    dummy, KrabLeft, KrabRight, PoliceOctopusAngry, polvoPolicia, smallFishDown, smallFishLeft, smallFishRight, smallFishUp, steelHorizontal,
    steelVertical, stone, TralaleroTralalaLeft, TralaleroTralalaRight, trunk, wall), adicionando algo expecial para a experiência do jogador.

---------------------------------------------------------------------
\src\objects
---------------------------------------------------------------------
*   Neste diretório encontram-se

*   ESTRUTURA BASE
    *   GameObject (abstract)       ->  Classe pai de todos os objetos
                                        *   Adicionámos o 'setExtra' para adicionar o sufixo 'Natal' nos objetos, no Handlers.trataGalo
                                        *   Adicionámos o 'isHeavy' para distinguir os objetos pesados dos leves
                                        *   Adicionámos também os respectivos Getters e Setters

    *   GameCharacter (abstract)    ->  Tem como super-classe o GameObject, é a classe responsável por construir os metodos dos objetos
                                        que sao personagens
                                        *   Adicionámos a String 'fileName' para que os personagens começem a olhar para a esquerda, mas
                                            que também possam ter as direções corretas conforme o comando dado, alterando o sufixo
                                        *   Adicionámos o boolean 'alive' e os métodos 'kill()' e 'Health()' que tratam da vida dos
                                            personagens, o 'kill()' matando-os e o 'Health()' revivendo-os, e consoante estes métodos
                                            o boolean 'alive' é alterado
                                        *   Adicionámos também uma String[] 'frases' e o método 'move()'. O 'move()' detecta se o personagem
                                            tenta sair dos limites do mapa que não correspondem à saída pretendida, empurrando os peixes na
                                            posição contrária e invocando o 'PoliceOctopus'. Após 3 tentativas de fugam alteramos a imagem
                                            do polvo para uma zangada, e este envia mensagens.

*   OBJETOS E MECÂNICAS NOVAS
    *   Bullet              ->  Pequeno Objeto letal: qualquer GameCharacter do tipo peixe que colida com este objeto morre instantaneamente.
    *   Dummy               ->  Personagem Inofensivo: Um personagem ao interagir com o 'Dummy' recebe umas frases filosóficas
    *   Explosion           ->  Objeto visual: apenas e usado quando uma bomba explode subsituindo os objetos explodidos por ele
    *   GaloNovidad         ->  Personagem Inofensivo: Um personagem ao interagir com o 'GaloNavidad' altera a skin do mapa (i.e) muda
                                o extra da room para "Natal"
    *   Handlers            ->  Class Auxiliar responsavel por guardar alguns metodos auxiliares à função canMove da classe Moveable
    *   IronTrap            ->  Objeto que impede a saida dos peixes, sendo necessario coletar a 'Key' para esta se abrir
    *   Key                 ->  Objeto estatico que quando interagido abre as IronTraps que estejam no Mapa
    *   Moveable            ->  Class que define objetos moviveis com metodos responsaveis pelo movimento (i.e. canMove-Verifica se Pode
                                se mover, fall->aplica gravidade, move-> move de facto os objetos)
    *   PoliceOctopus       ->  Personagem Inofensivo: que impede a saida dos Peixes caso estes tentem sair do mapa, por um ponto que nao
                                seja o WinPoint
    *   Portal              ->  Objeto inovador: Ao interagir com este portal misterioso, os peixes são mandados para uma sala secreta,
                                onde têm que fugir do 'TralaleroTralala'
    *   SeaHorse            ->  GameObject Singleton responsável por fazer o disparo da 'Bullet'
    *   TralaleroTralala    ->  O vilão do nível secreto (room99.txt). Usa o 'autoMove()' que calcula a cada TICK (aprox. 0,5 segundos)
                                a distância para ambos os peixes, indo atrás do mais próximo para o matar
    *   Whiskey             ->  Objeto inovador: Um personagem ao consumir uma garrafa de Jack Daniel's altera o movimento de todos os
                                personagens

---------------------------------------------------------------------
\src\pt.iscte.poo\game\GameEngine
---------------------------------------------------------------------
*   A GameEngine é a classe reposável por correr o jogo em si, trata de receber os inputs do utilizador tratar vitória e recolhas de
    informacções. Ela dá implements da interface Observer para reagir às ações do jogador, atualizando o jogo.

*   Funcionalidades Principais:

    *   update()        ->  É utilizado a cada ação. Para além da movimentação básica, também gere o modo 'DRUNK', verifica de um dos peixes
                            morreu e atualiza a StatusMessage conforme o peixe, tempo e número de jogadas
    *   processTick()   ->  Aplica gravidade aos elementos e remove as explosões. Controla a velocidade dos 'Bullets' e do 'TralaleroTralala'
    *   fireBullet()    ->  Mecânica inovadora: Verifica a existência de um 'SeaHorse', e se existir cria um objeto 'Bullet' no bloco da
                            frente do 'SeaHorse' e esta movimenta-se até bater num objeto e desaparece
    *   sharkLevel()    ->  Nível Secreto: Prepara a room99.txt e guarda a sala anterior para permitir o retorno após vencer o tubarão
    *   resetRoom()     ->  Reinicia o nível atual, caso clique no 'R' ou caso um peixe morra, lendo o ficheiro original. Reinicia também
                            os estados iniciais como 'Drunk' e o estado natalício.

---------------------------------------------------------------------
\src\pt.iscte.poo\game\Main
---------------------------------------------------------------------
*   A Main é a classe responsável por inicializar o programa e arrancar todos os componentes essenciais,
    começando por mostrar o MainMenu e a partir desse iniciar o engine do jogo.

*   Funcionalidades Principais:

    *   main()
        -> Método estático que serve como ponto de entrada da aplicação.

---------------------------------------------------------------------
\src\pt.iscte.poo\game\MainMenu
---------------------------------------------------------------------
*   O MainMenu é uma classe criada para centralizar toda a lógica relacionada com o menu inicial e final do jogo.
    O seu objetivo é proporcionar uma interface intuitiva, organizada e visualmente agradável para o utilizador,
    mantendo a estrutura do jogo simples e modular.

*   Funcionalidades Principais:

    *   MainMenu()
        -> Construtor da classe responsável pela criação do frame principal do menu através de Java Swing.
          Dentro deste frame é criado um painel que contém: botoes, iamgens, textPanes...
          Estes elementos são essenciais para apresentar informação ao jogador e permitir a navegação pelas opções do jogo.

    *   printStr()
        -> Escreve no TextPane a informação recebida como argumento, permitindo atualizar mensagens no menu de forma simples e direta.

---------------------------------------------------------------------
\src\pt.iscte.poo\game\Room
---------------------------------------------------------------------
*   A classe Room representa visualmente os níveis do jogo e guarda as informações dos objetos pertencentes a essa Room entre outras
    carctersiticas dela como extra e o winpoint

*   Os novos objetos e as suas representações nas 'rooms' são: IronTrap('I'), Key('K'), SeaHorse('s'), Dummy('D'), TralaleroTralala('t'),
    Portal('P'), GaloNovidad('g'), Whiskey('w') e Buoy('O')

*   Funcionalidades Principais:

    *   tradeToWater()      ->  Método Auxiliar que trata da remoção das explosões, a morte de GameObjects exceto os Characters
    *   containsSeaHorse()  ->  Método Auxiliar que verifica se existe um 'SeaHorse' no mapa

---------------------------------------------------------------------
\src\pt.iscte.poo\game\SoundManager
---------------------------------------------------------------------
*   SoundManager é uma classe criada de forma opcional pelos desenvolvedores para centralizar toda a lógica relacionada com o áudio do jogo.
    O objetivo é manter o código organizado e permitir um controlo fácil e consistente das músicas de fundo.

*   Funcionalidades Principais:

    *   playBackgroundMusic()
        -> Inicia a reprodução da música de fundo do jogo.

    *   stopBackgroundMusic()
        -> Interrompe imediatamente a música de fundo.

---------------------------------------------------------------------
NOTAS FINAIS
---------------------------------------------------------------------
*   Como o esperado, o projeto cumpre todos os requisitos obrigatórios, para além de funcionar com os itens adicionais como os objetos
    novos e o som.