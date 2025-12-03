public class carroDeLuxo extends Veiculo{
    public carroDeLuxo(String modelo, String placa,String statusManutencao, double valorDaDiaria){
        super(modelo, placa, "Luxo", statusManutencao, valorDaDiaria);
    }
    @Override
    public double calcularValorAlugel(int dias){
        double valorBase = this.valorDaDiaria * dias;
        return valorBase * 1.20;
    }
}
