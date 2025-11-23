import java.io.Serializable;

public class Cliente implements Serializable{
    private String cpf;
    private String nome;
    private String tipo;
    private int pontosFidelidade;

    public Cliente(String cpf, String nome, String tipo) {
        this.cpf = cpf;
        this.nome = nome;
        this.tipo = tipo;
        this.pontosFidelidade = 0;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getPontosFidelidade() {
        return pontosFidelidade;
    }

    public void setPontosFidelidade(int pontosFidelidade) {
        this.pontosFidelidade = pontosFidelidade;
    }

    public String toPersistString(){
        return String.join(";", cpf, nome, tipo, String.valueOf(pontosFidelidade));
    }


}
