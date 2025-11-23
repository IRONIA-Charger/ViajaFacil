public class carroEconomico extends Veiculo{
    public carroEconomico(String modelo, String placa, String categoria, String statusManutencao, double valorDadiaria){
        super(modelo,placa, "Econômico", "Disponível", valorDadiaria);
    }
    @Override
    public double calcularValorAlugel(int dias){
        double valorBase = this.valorDaDiaria * dias;
        return valorBase * 1.10;
    }
}
