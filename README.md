# ViajaFacil


Este projeto simula um sistema de gerenciamento de locadora de veículos, implementado em Java, utilizando Programação Orientada a Objetos (POO) e persistência de dados via arquivos CSV.
 Requisitos de Instalação e Execução

Para rodar este projeto, você precisa ter:

1.  **JDK (Java Development Kit)**: Versão 8 ou superior instalada.
2.  **IDE INSTALADA**:Preferencialmente Intellij 

 Como Executar

1.   Clone o repositório em sua maquina: git clone [link do repositório]
2.    Abrir o Projeto: Abra a pasta raiz `alugelCarros` na sua IDE.
3.  Configurar os Dados: Certifique-se de que os arquivos de dados iniciais (`clientes.csv`, `veiculos.csv`, `reservas.csv`) estão localizados na raiz do projeto (junto à pasta `src`).
4.  Executar: Compile e execute a classe **`Main.java`**.

---

Funcionalidades

O sistema oferece as seguintes opções através do menu de console:

| Opção | Funcionalidade | Descrição |
| :---: | :--- | :--- |
| 1 | Fazer Reserva | Permite registrar o aluguel, aplicando descontos VIP e validando a disponibilidade do veículo. |
| 2 | Devolver Veículo | Registra o fim do aluguel, calcula multas por atraso (R$ 50/dia) e atualiza os pontos de fidelidade do cliente. |
| 3 | Relatório de Receitas | Exibe o valor total arrecadado com as reservas que estão no status "Concluída". |
| 4 | Relatório de Status | Lista todos os veículos que não estão "Disponíveis" (ou seja, estão "Alugado" ou "Manutenção"). |

---
Os dados são salvos e carregados automaticamente nos seguintes arquivos:

clientes.csv: Armazena clientes e seus pontos de fidelidade.
veiculos.csv: Armazena veículos, suas diárias e status atual (Disponível, Alugado, Manutenção).
reservas.csv: Armazena o histórico e o status das reservas.
