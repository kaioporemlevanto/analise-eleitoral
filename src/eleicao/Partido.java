package eleicao;

import java.util.ArrayList;
import java.util.List;

public class Partido {
    private int numeroPartido;
    private String siglaPartido;
    private List<Candidato> candidatos;
    private int qtVotos;

    public Partido(int numeroPartido, String siglaPartido) {
        this.numeroPartido = numeroPartido;
        this.siglaPartido = siglaPartido;
        this.candidatos = new ArrayList<>();
    }

    public int getTotalVotos() {
        int totalVotos = 0;
        for (Candidato candidato : candidatos) {
            if (candidato.getNmTipoDestinacaoVotos().equals("Válido")) {
                totalVotos += candidato.getQtVotos();
                this.incrementaVotos(totalVotos);
            }
            else if(!candidato.getNmTipoDestinacaoVotos().equals("Válido")){
                totalVotos += candidato.getQtVotos();
            }
        }
        totalVotos += this.getQtVotos();
        return totalVotos;
    }

    public int getTotalVotosNominais() {
        int totalVotosNominais = 0;
        for (Candidato candidato : candidatos) {
            totalVotosNominais += candidato.getQtVotos();
        }
        return totalVotosNominais;
    }

    public void adicionaCandidato(Candidato candidato) {
        this.candidatos.add(candidato);
    }

    public List<Candidato> getCandidatos() {
        return new ArrayList<Candidato>(candidatos);
    }

    public String getSiglaPartido() {
        return siglaPartido;
    }

    public int getNumeroPartido() {
        return numeroPartido;
    }

    public int getQtVotos() {
        return qtVotos;
    }

    public void incrementaVotos(int votos) {
        this.qtVotos += votos;
    }

}
