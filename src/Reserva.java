import java.io.Serializable;
import java.time.LocalDate;

public class Reserva implements Serializable{
    private final String idReserva;
    private final Cliente cliente;
    private final Veiculo veiculo;
    private final LocalDate dataRetirada;
    private final LocalDate dataPrevistaDevolucao;
    private final double valorEstimado;
    private double valorPago;
    private String status;

    public Reserva(String idReserva, Cliente cliente, Veiculo veiculo, LocalDate dataRetirada,
                   LocalDate dataPrevistaDevolucao, double valorEstimado) {
        this.idReserva = idReserva;
        this.cliente = cliente;
        this.veiculo = veiculo;
        this.dataRetirada = dataRetirada;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
        this.valorPago = 0.0;
        this.valorEstimado = valorEstimado;
        this.status = "Confirmado";
    }

    public String getIdReserva() {
        return idReserva;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public LocalDate getDataRetirada() {
        return dataRetirada;
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public double getValorEstimado() {
        return valorEstimado;
    }

    public double getValorPago() {
        return valorPago;
    }
    public void setValorPago(double valorPago) {
        this.valorPago = valorPago;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String toCSVSTRING(){
        return String.join(";", idReserva, cliente.getCpf(), veiculo.getPlaca(),
                dataRetirada.toString(), dataPrevistaDevolucao.toString(),
                String.valueOf(this.valorEstimado), String.valueOf(this.valorPago), status);
    }

}
