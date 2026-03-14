# 🐟 Fish Fillets NG — Java

Jogo inspirado no Fish Fillets NG, desenvolvido no âmbito da disciplina
de Programação Orientada aos Objetos (ISCTE-IUL, 2025/2026).

## Sobre o jogo

Dois peixes (grande e pequeno) navegam por níveis aquáticos, interagindo
com objetos para chegarem ambos à saída. Cada peixe tem capacidades
distintas de movimento e interação com o ambiente.

## Funcionalidades

- Múltiplos níveis carregados de ficheiro (`room0.txt`, `room1.txt`, ...)
- Dois peixes controláveis com regras de física distintas
- Gravidade aplicada a objetos móveis
- Objetos com comportamentos variados: taça, pedra, âncora, bomba, armadilha
- Reinício de nível (`R`), troca de peixe ativo (`Espaço`)
- Tabela de highscores persistente (top 10)
- Deteção de morte e reinício automático

## Controlos

| Tecla | Ação |
|-------|------|
| ↑ ↓ ← → | Mover peixe selecionado |
| Espaço | Trocar peixe ativo |
| R | Reiniciar nível |

## Tecnologias e conceitos

- **Java** — OOP, herança, classes abstratas, interfaces
- **Swing / ImageGUI** — framework gráfica fornecida
- Padrão **Observer** (atualização do estado do jogo)
- Padrão **Singleton** (GameEngine)
- Leitura/escrita de ficheiros, tratamento de exceções

## Como executar

Importar o projeto no Eclipse via `File > Import > Archive File` e correr
a classe principal.

## Estrutura de classes (simplificada)
```
GameObject (abstrata)
├── Fish
│   ├── BigFish
│   └── SmallFish
├── MovableObject
│   ├── Cup, Rock, Anchor, Bomb, Trap
└── FixedObject
    ├── Wall, SteelPipe, HoleWall, Log
```
