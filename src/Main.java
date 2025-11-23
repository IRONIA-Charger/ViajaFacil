import java.util.Scanner;
import java.time.LocalDate;
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
                System.out.print("Escolha uma opção: ");
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
                        locadora.gerarRelatorioReceitas();
                        break;
                    case 4:
                        locadora.gerarRelatorioVeiculosEmUso();
                        break;
                    case 5:
                        consultarPontos(locadora, scanner);
                        break;
                    case 0:
                        System.out.println("Encerrando o sistema. Até mais!");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("ERRO: Entrada inválida. Por favor, digite um número.");
                scanner.nextLine();
                opcao = -1;
            }
        }
        scanner.close();
    }
        private static void exibirMenu(){
            System.out.println("+===========---Funções---===========+");
            System.out.println("| 1. Fazer Reserva                  |");
            System.out.println("| 2. Devolver Veículo               |");
            System.out.println("| 3. Relatório de Receitas          |");
            System.out.println("| 4. Relatório de Status de Veículos|");
            System.out.println("| 5. Consultar Pontos               |");
            System.out.println("| 0. Sair                           |");
            System.out.println("+-----------------------------------+");
        }

        private static void fazerReserva(Locadora locadora, Scanner scanner){
            System.out.println("-------- NOVA RESERVA --------");

            System.out.println("Digite o CPF do Cliente: ");
            String cpf = scanner.nextLine();

            System.out.println("Digite a Placa do Veículo: ");
            String placa = scanner.nextLine();

            System.out.println("Data Retirada: (AAAA/MM/DD)");
            LocalDate retirada = LocalDate.parse(scanner.nextLine());

            System.out.println("Data Prevista: (AAAA/MM/DD)");
            LocalDate devolucao = LocalDate.parse(scanner.nextLine());

            locadora.fazerReserva(cpf, placa, retirada, devolucao);

        }
        private static void devolverVeiculo(Locadora locadora, Scanner scanner){

            System.out.println("----- DEVOLUÇÃO DE VEÍCULO -----");

            System.out.println("Digite o ID da Reserva a ser desenvolvida: (Ex: RES1)");
            String idReserva = scanner.nextLine().trim();

            System.out.println("Data de Devolução Real: (AAAA/MM/DD)");
            LocalDate dataDevolucao = LocalDate.parse(scanner.nextLine());

            locadora.devolverVeiculo(idReserva, dataDevolucao);
        }
        private static void consultarPontos(Locadora locadora, Scanner scanner){
            System.out.println("----- CONSULTA DE PONTOS -----");

            System.out.println("Digite o CPF do cliente: ");
            String cpf = scanner.nextLine();


            Cliente cliente = locadora.buscarClientePorCPF(cpf);

            if (cliente != null){
                System.out.println("Cliente: " + cliente.getNome());
                System.out.println("Pontos: " + cliente.getPontosFidelidade());
            }else {
                System.out.println("Cliente não encontrado");
            }
        }
    }
