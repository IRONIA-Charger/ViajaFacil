import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Locadora {
    private List<Veiculo> veiculos;
    private List<Cliente> clientes;
    private List<Reserva> reservas;

    public Locadora() {
        this.veiculos = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.reservas = new ArrayList<>();
        carregarClientes();
        carregarVeiculos();
        carregarReservas();
    }

    private static final String ARQUIVO_VEICULOS = "veiculos.csv";
    private static final String ARQUIVO_RESERVAS = "reservas.csv";
    private static final String ARQUIVO_CLIENTES = "clientes.csv";

    /**
     * Cadastra um novo veículo na lista.
     *
     * @param veiculo O objeto Veiculo a ser cadastrado.
     * @return true se o cadastro foi realizado com sucesso.
     */

    public boolean cadastrarVeiculo(Veiculo veiculo) {
        // Validação (Tratamento de Erros)
        if (veiculo == null || veiculo.getPlaca() == null || veiculo.getPlaca().isEmpty()) {
            System.err.println("Erro: Dados do veículo inválidos.");
            return false;
        }

        if (veiculos.stream().anyMatch(v -> v.getPlaca().equals(veiculo.getPlaca()))) {
            System.err.println("Erro: Veículo com esta placa já cadastrado.");
            return false;
        }

        veiculos.add(veiculo);
        System.out.println("Veículo " + veiculo.getPlaca() + " cadastrado com sucesso.");
        return true;
    }

    /**
     * Cadastra um novo cliente na lista.
     *
     * @param cliente O objeto Cliente a ser cadastrado.
     */
    public boolean cadastrarCliente(Cliente cliente) {
        if (clientes.stream().anyMatch(c -> c.getCpf().equals(cliente.getCpf()))) {
            System.err.println("Erro: Cliente com este CPF já cadastrado.");
            return false;
        }

        clientes.add(cliente);
        System.out.println("Cliente " + cliente.getNome() + " cadastrado com sucesso.");
        return true;
    }

    /**
     * Altera o status de manutenção de um veículo (Requisito de Manutenção).
     *
     * @param placa      A placa do veículo a ser atualizado.
     * @param novoStatus O novo status
     */
    public void atualizarStatusManutencao(String placa, String novoStatus) {
        for (Veiculo v : veiculos) {
            if (v.getPlaca().equals(placa)) {
                v.setStatusManutencao(novoStatus);
                // salvarVeiculos();
                System.out.println("Status de manutenção de " + placa + " atualizado para " + novoStatus);
                return;
            }
        }
        System.err.println("Erro: Veículo com placa " + placa + " não encontrado.");
    }

    public Cliente buscarClientePorCPF(String cpfBusca) {
        String cpfLimpo = limparCPF(cpfBusca);
        for (Cliente c : clientes) {
            if (limparCPF(c.getCpf()).equals(cpfLimpo)) {
                return c;
            }
        }
        return null;
    }
    public Veiculo buscarCarroPorPlaca(String placaBusca) {
        String placaLimpa = limparPlaca(placaBusca);
        for (Veiculo v : veiculos) {
            if (limparPlaca(v.getPlaca()).equalsIgnoreCase(placaLimpa)) {
                return v;
            }
        }
        return null;
    }

    public Reserva buscarReservaPorID(String id) {
        String idBusca = id.trim();

        for (Reserva r : reservas) {
            if (r.getIdReserva().trim().equals(idBusca)) {
                return r;
            }
        }
        return null;
    }

    public void salvarVeiculo() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO_VEICULOS))) {
            for (Veiculo v : veiculos) {
                bw.write(v.toPersistString());
                bw.newLine();
            }
            System.out.println("SUCESSO. Véiculo salvo em: " + ARQUIVO_VEICULOS);
        } catch (IOException e) {
            System.err.println("ERRO DE PERSISTÊNCIA: Não foi possível salvar os veículos " + e.getMessage());
        }
    }

    public void salvarReserva() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO_RESERVAS))) {

            for (Reserva r : reservas) {
                bw.write(r.toCSVSTRING());
                bw.newLine();
            }
            System.out.println("SUCESSO. Reserva salva em: " + ARQUIVO_RESERVAS);
        } catch (IOException e) {
            System.err.println("ERRO DE PERSITÊNCIA: Não foi possível salvar a reserva " + e.getMessage());
        }

    }

    public void salvarClientes() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO_CLIENTES))) {

            for (Cliente c : clientes) {
                bw.write(c.toPersistString());
                bw.newLine();
            }
            System.out.println("SUCESSO. Cliente salvo (com pontos de fidelidade) em: " + ARQUIVO_CLIENTES);
        } catch (IOException e) {
            System.out.println("ERRO DE PERSISTÊNCIA: Não foi possível salvar os clientes " + e.getMessage());
        }
    }

    public boolean fazerReserva(String cpf, String placa, LocalDate retirada, LocalDate devolucao){

        Cliente cliente = buscarClientePorCPF(cpf);
        Veiculo veiculo = buscarCarroPorPlaca(placa);

        if (cliente == null || veiculo == null) {
            System.out.println("Erro! Cliente ou Veículo não encontrado");
            return false;
        }

        if (!veiculo.getStatusManutencao().equals("Disponível")) {
            System.out.println("Erro! " + placa + " não está disponível para alugel. (Status: " + veiculo.getStatusManutencao() + " ).");
            return false;
        }

        if (veiculo.getCategoria().equals("Luxo") && cliente.getTipo().equals("Regular")) {
            System.out.println("Erro! Carros da Categoria Luxo são exclusivos para Clientes VIP");
            return false;
        }

        long dias = ChronoUnit.DAYS.between(retirada, devolucao);

        if (dias <= 0) {
            System.out.println("Erro. A data de deolução deve ser inferior a de retirada");
            return false;
        }

        double valorEstimadoBruto = veiculo.calcularValorAlugel((int) dias);

        double valorComDesconto = valorEstimadoBruto;
        String motivoDesconto = "Nenhum";

        if (cliente.getTipo().equals("VIP")){
            double descontoVIP = 0.10;
            valorComDesconto = valorEstimadoBruto * (1-descontoVIP);
            motivoDesconto = "VIP (10%)";
        }

        System.out.println("Valor Estimado Bruto R$: " + String.format("%.2f", valorEstimadoBruto));

        System.out.println("Desconto aplicado: " + motivoDesconto);

        String novoID = "RES" + (reservas.size() + 1);

        Reserva novaReserva = new Reserva(novoID, cliente, veiculo, retirada, devolucao, valorComDesconto);

        reservas.add(novaReserva);

        veiculo.setStatusManutencao("Alugado");
        salvarReserva();
        salvarVeiculo();

        System.out.println("SUCESSO: Reserva " + novoID + " confirmada. Valor estimado: R$ " + String.format("%.2f", valorEstimadoBruto));

        return true;
    }

    public boolean devolverVeiculo(String idReserva, LocalDate dataDevolucaoReal) {
        Reserva reserva = buscarReservaPorID(idReserva);

        if (reserva == null) {
            System.out.println("Erro: Reserva com ID " + idReserva + " não existe");
            return false;
        }

        if (!reserva.getStatus().equals("Confirmado")) {
            System.out.println("Erro: Reserva já está com o status " + reserva.getStatus());
            return false;
        }

        LocalDate dataPrevista = reserva.getDataPrevistaDevolucao();
        long diasAtraso = 0;

        if (dataDevolucaoReal.isAfter(dataPrevista)) {
            diasAtraso = ChronoUnit.DAYS.between(dataPrevista, dataDevolucaoReal);
        }

        double valorBaseEstimado = reserva.getValorEstimado();

        double taxaMultaPorDia = 50;
        double valorMulta = diasAtraso * taxaMultaPorDia;
        double valorTotalFinal = valorBaseEstimado + valorMulta;


        System.out.println("\n--- DEVOLUÇÃO DA RESERVA " + idReserva + " ---");
        System.out.println("Dias de Atraso: " + diasAtraso);
        System.out.println("Valor da Multa: R$ " + String.format("%.2f", valorMulta));
        System.out.println("Valor Final a Pagar: R$ " + String.format("%.2f", valorTotalFinal));

        reserva.setValorPago(valorTotalFinal);
        reserva.setStatus("Concluído");

        Veiculo veiculo = reserva.getVeiculo();
        veiculo.setStatusManutencao("Disponível");


        adicionarPontosFidelidade(reserva.getCliente(), valorTotalFinal);

        salvarReserva();
        salvarVeiculo();
        salvarClientes();

        System.out.println("SUCESSO: Devolução concluida e veículo liberado");
        return true;
    }

    public void adicionarPontosFidelidade(Cliente cliente, double valorGasto) {
        int pontosGanhos = (int) (valorGasto / 10.0);

        if (pontosGanhos > 0) {
            int novosPontos = cliente.getPontosFidelidade() + pontosGanhos;
            cliente.setPontosFidelidade(novosPontos);

            System.out.println("FIDELIDADE: Cliente " + cliente.getNome() + " ganhou " + pontosGanhos + " pontos!");
            System.out.println("Saldo atual de pontos: " + novosPontos);


            salvarClientes();
        }
    }
    public void gerarRelatorioReceita(){
        double totalReceita = 0;

        System.out.println("\n==RELATÓRIO DE RECEITAS TOTAIS==");

        for (Reserva r : reservas){
            if (r.getStatus().equalsIgnoreCase("Concluído")){
                totalReceita += r.getValorPago();

                System.out.printf("ID %s | Cliente: %s | Veículo: %s | Valor Final: R$ %.2f%n",
                        r.getIdReserva(),
                        r.getCliente().getNome(),
                        r.getVeiculo().getPlaca(),
                        r.getValorPago());


            }
        }
        System.out.println("------------------------------------------");
        System.out.println("==TOTAL ACUMULADO RECEITAS: R$ .%.2f%n" + totalReceita);
    }

    private String normalizeLine(String linha) {
        if (linha == null) return "";

        linha = linha.replace("\uFEFF", "").trim();

        if (linha.contains(",") && linha.indexOf(",") < linha.indexOf(";")) {
            linha = linha.substring(linha.indexOf(",") + 1).trim();
        }
        return linha;
    }

    public void carregarClientes() {
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_CLIENTES))) {
            String linha;
            int cont = 0;

            while ((linha = br.readLine()) != null) {
                linha = normalizeLine(linha);
                if (linha.isEmpty()) continue;

                String[] dados = linha.split(";");
                if (dados.length >= 4) {
                    try {
                        String cpf = limparCPF(dados[0].trim());   // CPF é o primeiro
                        String nome = dados[1].trim();             // Nome é o segundo
                        String tipo = dados[2].trim();
                        int pontos = Integer.parseInt(dados[3].trim());

                        Cliente cliente = new Cliente(cpf, nome, tipo);
                        cliente.setPontosFidelidade(pontos);

                        this.clientes.add(cliente);
                        cont++;

                    } catch (NumberFormatException e) {
                        System.err.println("ERRO: Pontos inválidos na linha de cliente: " + linha);
                    }
                } else {
                    System.err.println("ERRO: Linha de cliente com formato inválido (pulada): " + linha);
                }
            }
            System.out.println("SUCESSO: " + cont + " clientes carregados");
        } catch (IOException e) {
            System.err.println("AVISO: Arquivo de clientes não encontrado. Iniciando sem clientes.");
        }
    }


    public void carregarVeiculos() {
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_VEICULOS))) {
            String linha;
            int cont = 0;

            while ((linha = br.readLine()) != null) {
                linha = normalizeLine(linha);
                if (linha.isEmpty()) continue;

                String[] dados = linha.split(";");
                if (dados.length >= 5) {
                    try {
                        String placa = limparPlaca(dados[0].trim());
                        String modelo = dados[1].trim();
                        String categoria = dados[2].trim();
                        String statusManutencao = dados[3].trim();
                        double diaria = Double.parseDouble(dados[4].trim());

                        Veiculo veiculo = null;

                        if (categoria.equalsIgnoreCase("Luxo")) {
                            veiculo = new carroDeLuxo(modelo, placa, categoria, statusManutencao, diaria);
                        }
                        else if (categoria.equalsIgnoreCase("Economico") || categoria.equalsIgnoreCase("Econômico")) {
                            veiculo = new carroEconomico(modelo, placa, categoria, statusManutencao, diaria);
                        }
                        else {
                            veiculo = new Veiculo(modelo, placa, categoria, statusManutencao, diaria) {
                                @Override
                                public double calcularValorAlugel(int dias) {
                                    return this.valorDaDiaria * dias;
                                }
                            };
                        }

                        this.veiculos.add(veiculo);
                        cont++;

                    } catch (NumberFormatException e) {
                        System.err.println("ERRO: Diária inválida na linha de veículo: " + linha);
                    }
                } else {
                    System.err.println("ERRO: Linha de veículo com formato inválido (pulada): " + linha);
                }
            }

            System.out.println("SUCESSO: " + cont + " veículos carregados.");

        } catch (IOException e) {
            System.err.println("AVISO: Arquivo de veículos não encontrado ou não pôde ser lido.");
        }
    }



    public void carregarReservas() {
        int contagemReservas = 0;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(ARQUIVO_RESERVAS), "UTF-8"))) {

            String linha;
            while ((linha = br.readLine()) != null) {
                linha = normalizeLine(linha);
                if (linha.isEmpty()) continue;

                try {
                    String[] dados = linha.split(";");
                    if (dados.length >= 8) {
                        String id = dados[0].trim();
                        String cpf = limparCPF(dados[1].trim());
                        String placa = limparPlaca(dados[2].trim());

                        LocalDate retirada = LocalDate.parse(dados[3].trim());
                        LocalDate devolucaoPrevista = LocalDate.parse(dados[4].trim());
                        double valorEstimado = Double.parseDouble(dados[5].trim());
                        double valorPago = Double.parseDouble(dados[6].trim());
                        String status = dados[7].trim();

                        Cliente cliente = buscarClientePorCPF(cpf);
                        Veiculo veiculo = buscarCarroPorPlaca(placa);

                        if (cliente != null && veiculo != null) {
                            Reserva reserva = new Reserva(id, cliente, veiculo, retirada, devolucaoPrevista, valorEstimado);
                            reserva.setValorPago(valorPago);
                            reserva.setStatus(status);
                            this.reservas.add(reserva);
                            contagemReservas++;
                        } else {
                            System.err.println("ERRO DE INCONSISTÊNCIA: Falha ao carregar a reserva " + id +
                                    ". Cliente (" + cpf + ") ou Veículo (" + placa + ") não encontrado nas listas.");
                        }
                    } else {
                        System.err.println("ERRO: Linha de reserva com formato inválido (pulada): " + linha);
                    }
                } catch (Exception e) {
                    System.err.println("ERRO DE DADO IGNORADO: Linha pulada. Formatação incorreta na linha: " + linha);
                }
            }
            System.out.println("SUCESSO: " + contagemReservas + " reservas carregadas.");
        } catch (IOException e) {
            System.err.println("AVISO: Erro ao carregar reservas. Arquivo não pode ser lido. " + e.getMessage());
        }
    }


    public void gerarRelatorioVeiculosEmUso(){

        System.out.println("== RELATÓRIO DE STATUS DE VEÍCULOS ==");
        System.out.println("------------------------------------------");

        for (Veiculo v: veiculos){

            if (!v.getStatusManutencao().equalsIgnoreCase("Disponível")){

                System.out.printf("Placa: %s | Modelo: %s (%s) | Status: %s%n",
                        v.getPlaca(),
                        v.getModelo(),
                        v.getCategoria(),
                        v.getStatusManutencao());

            }
        }
        System.out.println("------------------------------------------");
    }

    public void gerarRelatorioReceitas(){
        double totalReceita = 0;

        System.out.println("== RELÁTORIO DE RECEITAS  TOTAIS ==");
        System.out.println("-----------------------------------");

        for (Reserva r : reservas){
            if (r.getStatus().equalsIgnoreCase("Concluído")){
                totalReceita += r.getValorPago();
                System.out.printf("ID %s | Cliente: %s | Veículo: %s | Valor Final: R$ %.2f%n",
                        r.getIdReserva(),
                        r.getCliente().getNome(),
                        r.getVeiculo().getPlaca(),
                        r.getValorPago());
            }
        }
        System.out.println("----------------------------------");
        System.out.println("TOTAL ACUMULADO RECEITAS: R$: " + totalReceita);
    }
    private String limparCPF(String cpf) {
        if (cpf == null) return "";
        return cpf.replaceAll("[^0-9]", "").trim();
    }

    private String limparPlaca(String placa) {
        if (placa == null) return "";
        return placa.replaceAll("[^a-zA-Z0-9]", "").trim().toUpperCase();
    }
}

