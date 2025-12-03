public abstract class Veiculo {
    private String modelo;
    private String placa;
    private String categoria;
    private String statusManutencao;
    protected double valorDaDiaria;

    public Veiculo(String modelo, String placa, String categoria, String statusManutencao, double valorDaDiaria) {
        this.modelo = modelo;
        this.placa = placa;
        this.categoria = categoria;
        this.statusManutencao = statusManutencao;
        this.valorDaDiaria = valorDaDiaria;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getStatusManutencao() {
        return statusManutencao;
    }

    public void setStatusManutencao(String statusManutencao) {
        this.statusManutencao = statusManutencao;
    }

    public double getValorDaDiaria() {
        return valorDaDiaria;
    }

    public abstract double calcularValorAlugel(int dias);

    public String toPersistString() {
        return String.join(";",
                placa,
                modelo,
                categoria,
                statusManutencao,
                String.valueOf(valorDaDiaria),
                this.getClass().getName()
        );
    }
}
