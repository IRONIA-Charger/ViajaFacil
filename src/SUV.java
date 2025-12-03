public class SUV extends Veiculo{
    public SUV(String modelo, String placa,String statusManutencao, double valorDaDiaria) {
        super(modelo, placa, "SUV", statusManutencao, valorDaDiaria);
    }
    @Override
    public double calcularValorAlugel(int dias) {
        double taxaSUV = 1.20;
        return (this.valorDaDiaria * taxaSUV) * dias;
    }
}
