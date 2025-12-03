public class carroEconomico extends Veiculo{
    public carroEconomico(String modelo, String placa, String statusManutencao, double valorDaDiaria){
        super(modelo,placa, "Econômico",statusManutencao,valorDaDiaria);
    }
    @Override
    public double calcularValorAlugel(int dias){
        double valorBase = this.valorDaDiaria * dias;
        return valorBase * 1.10;
    }
}
