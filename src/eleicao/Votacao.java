package eleicao;
public class Votacao {
    private int numeroVotavel;
    private int quantidadeVotos;

    public Votacao(int numeroVotavel, int quantidadeVotos) {
        this.numeroVotavel = numeroVotavel;
        this.quantidadeVotos = quantidadeVotos;
    }

    public int getNrVotavel() {
        return numeroVotavel;
    }

    public int getQtVotos() {
        return quantidadeVotos;
    }
}
