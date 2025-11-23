public class carroDeLuxo extends Veiculo{
    public carroDeLuxo(String modelo, String placa, String categoria, String statusManutencao, double valorDadiaria){
        super(modelo, placa, "Luxo", "Disponível", valorDadiaria);
    }
    @Override
    public double calcularValorAlugel(int dias){
        double valorBase = this.valorDaDiaria * dias;
        return valorBase * 1.20;
    }
}
