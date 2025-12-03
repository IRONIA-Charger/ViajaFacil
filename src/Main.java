import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {
    public static void main(String[] args) {
        Locadora locadora = new Locadora();
        Scanner scanner = new Scanner(System.in);
        int opcao = -1;


        System.out.println("+==================================+");
        System.out.println("| BEM VINDO AO SISTEMA VIAJA FÁCIL |");
        System.out.println("+==================================+");


        while (opcao != 0) {
            exibirMenu();
            try {
                IO.print("Escolha uma opção: ");
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        fazerReserva(locadora, scanner);
                        break;
                    case 2:
                        devolverVeiculo(locadora, scanner);
                        break;
                    case 3:
                        cadastrarCliente(locadora, scanner);
                        break;
                    case 4:
                        cadastrarVeiculo(locadora, scanner);
                        break;
                    case 5:
                        atualizarStatusManutencao(locadora, scanner);
                        break;
                    case 6:
                        locadora.gerarRelatorioReceita();
                        break;
                    case 7:
                        locadora.gerarRelatorioVeiculosEmUso();
                        break;
                    case 8:
                        consultarPontos(locadora, scanner);
                        break;
                    case 0:
                        IO.println("Encerrando o sistema. Até mais!");
                        break;
                    default:
                        IO.println("Opção inválida. Tente novamente.");
                }
            } catch (InputMismatchException e) {
                IO.println("ERRO: Entrada inválida. Por favor, digite um número.");
                scanner.nextLine();
                opcao = -1;
            }
        }
        scanner.close();
    }

    private static void exibirMenu() {
        IO.println("+===========---Funções---===========+");
        IO.println("| 1. Fazer Reserva                  |");
        IO.println("| 2. Devolver Veículo               |");
        IO.println("| 3. Cadastrar um novo Cliente      |");
        IO.println("| 4. Cadastrar um novo veículo      |");
        IO.println("| 5. Atualizar status da manutenção |");
        IO.println("| 6. Relatório de Receitas          |");
        IO.println("| 7. Relatório de Status de Veículos|");
        IO.println("| 8. Consultar Pontos               |");
        IO.println("| 0. Sair                           |");
        IO.println("+-----------------------------------+");
    }


    private static void cadastrarCliente(Locadora locadora, Scanner scanner) {
        IO.println("--------CADASTRO DE CLIENTE---------");


        IO.println("Digite o NOME do cliente: ");
        String nome = scanner.nextLine();

        IO.println("Digite o CPF do cliente: ");
        String cpf = scanner.nextLine();

        IO.println("Tipo de CLIENTE: ");
        String tipo = scanner.nextLine();

        Cliente cliente = new Cliente(nome, cpf, tipo);
        boolean sucesso = locadora.cadastrarCliente(cliente);
    }

    private static void cadastrarVeiculo(Locadora locadora, Scanner scanner) {
        IO.println("-------CADASTRO DE VEÍCULO-------");

        IO.println("Digite o MODELO do veículo: ");
        String modelo = scanner.nextLine();

        IO.println("Digte a PLACA do veículo: ");
        String placa = scanner.nextLine();

        IO.println("CATEGORIA(LUXO, ECONÔMICO OU SUV): ");
        String categoria = scanner.nextLine();

        try {
            IO.println("Valor da DIÁRIA: ");
            double valorDaDiaria = scanner.nextDouble();

            scanner.nextLine();

            IO.println("Status (ALUGADO OU DISPONÍVEl): ");

            String statusManutencao = scanner.nextLine();

            Veiculo veiculo;
            if (categoria.equalsIgnoreCase("Luxo")) {
                veiculo = new carroDeLuxo(modelo, placa, statusManutencao, valorDaDiaria);
            } else if (categoria.equalsIgnoreCase("Economico")) {
                veiculo = new carroEconomico(modelo, placa, statusManutencao, valorDaDiaria);
            } else if (categoria.equalsIgnoreCase("SUV")) {
                veiculo = new SUV(modelo, placa, statusManutencao, valorDaDiaria);
            } else {
                IO.println("Categoria inválida, Cadastro cancelado");
                return;
            }
            veiculo.setStatusManutencao(statusManutencao);

            boolean sucesso = locadora.cadastrarVeiculo(veiculo);
        } catch (InputMismatchException e) {
            IO.println("ERRO: O número da diária deve ser um número valido. Cadastro cancelado");
            scanner.nextLine();
        }
    }

    private static void atualizarStatusManutencao(Locadora locadora, Scanner scanner) {
        IO.println("-------ATUALIZAR STATUS-------");

        IO.println("Placa: ");
        String placa = scanner.nextLine();

        IO.println("Novo Status: ");
        String novoStatus = scanner.nextLine();

        locadora.atualizarStatusManutencao(placa, novoStatus);
    }

    private static void fazerReserva(Locadora locadora, Scanner scanner) {
        IO.println("-------- NOVA RESERVA --------");

        IO.println("Digite o CPF do Cliente: ");
        String cpf = scanner.nextLine();

        IO.println("Digite a Placa do Veículo: ");
        String placa = scanner.nextLine();

        LocalDate retirada;
        LocalDate devolucao;

        try {
            IO.println("Data Retirada: (AAAA-MM-DD)");
            retirada = LocalDate.parse(scanner.nextLine());

            IO.println("Data Prevista: (AAAA-MM-DD)");
            devolucao = LocalDate.parse(scanner.nextLine());
        } catch (DateTimeParseException e) {
            IO.println("ERRO DE ENTRADA: Formato de data inválido. Use AAAA-MM-DD.");
            return;
        }
        boolean sucesso = locadora.fazerReserva(cpf, placa, retirada, devolucao);

    }

    private static void devolverVeiculo(Locadora locadora, Scanner scanner) {

        IO.println("----- DEVOLUÇÃO DE VEÍCULO -----");

        IO.println("Digite o ID da Reserva a ser desenvolvida: (Ex: RES1)");
        String idReserva = scanner.nextLine().trim();

        IO.println("Data de Devolução Real: (AAAA-MM-DD)");
        LocalDate dataDevolucao = LocalDate.parse(scanner.nextLine());

        boolean sucesso = locadora.devolverVeiculo(idReserva, dataDevolucao);
    }

    private static void consultarPontos(Locadora locadora, Scanner scanner) {
        IO.println("----- CONSULTA DE PONTOS -----");

        IO.println("Digite o CPF do cliente: ");
        String cpf = scanner.nextLine();


        Cliente cliente = locadora.buscarClientePorCPF(cpf);

        if (cliente != null) {
            IO.println("Cliente: " + cliente.getNome());
            IO.println("Pontos: " + cliente.getPontosFidelidade());
        } else {
            IO.println("Cliente não encontrado");
        }
    }
}
