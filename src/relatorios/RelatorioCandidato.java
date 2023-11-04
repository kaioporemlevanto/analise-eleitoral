package relatorios;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import eleicao.Candidato;
import eleicao.Eleicao;

public class RelatorioCandidato extends Relatorio {
    
    public RelatorioCandidato(Eleicao eleicao) {
        super(eleicao);
    }

    public void imprimirRelatorio() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DecimalFormat df = new DecimalFormat("#,###", symbols);
        int vagasDeputadosEleitos = eleicao.getQtDeputadosEleitos();
        List<Candidato> candidatos = eleicao.getCandidatos();
        List<Candidato> candidatosEleitos = eleicao.getCandidatosEleitos(candidatos);
        List<Candidato> candidatosMaisVotados = eleicao.getCandidatosMaisVotados(candidatos).subList(0, vagasDeputadosEleitos);

        //Relatório 1
        System.out.println("Número de vagas: " + vagasDeputadosEleitos +"\n");

        //Relatório 2
        System.out.println("Deputados " + (eleicao.isEstadual() ? "estaduais" : "federais") + " eleitos:");
        
        for (int i = 0; i < candidatosEleitos.size(); i++) {
            Candidato candidato = candidatosEleitos.get(i);
            String nomeCandidato = candidato.getNmUrnaCandidato().replace("\"", "");
            String nomePartido = candidato.getSgPartido().replace("\"", "");
            int votos = candidato.getQtVotos();
            int numeroFed = candidato.getNrFederacao();

            if (numeroFed != -1) {
                System.out.println((i + 1) + " - *" + nomeCandidato + " (" + nomePartido + ", " + df.format(votos) + " votos)");
            } else {
                System.out.println((i + 1) + " - " + nomeCandidato + " (" + nomePartido + ", " + df.format(votos) + " votos)");
            }
        }

        //Relatório 3
        System.out.println("\nCandidatos mais votados (em ordem decrescente de votação e respeitando número de vagas):");
        
        for (int i = 0; i < candidatosMaisVotados.size(); i++) {
            Candidato candidato = candidatosMaisVotados.get(i);
            String nomeCandidato = candidato.getNmUrnaCandidato().replace("\"", "");
            String nomePartido = candidato.getSgPartido().replace("\"", "");
            int votos = candidato.getQtVotos();
            int numeroFed = candidato.getNrFederacao();

            if (numeroFed != -1) {
                System.out.println((i + 1) + " - *" + nomeCandidato + " (" + nomePartido + ", " + df.format(votos) + " votos)");
            } else {
                System.out.println((i + 1) + " - " + nomeCandidato + " (" + nomePartido + ", " + df.format(votos) + " votos)");
            }
        }

        //Relatório 4
        System.out.println("\nTeriam sido eleitos se a votação fosse majoritária, e não foram eleitos:\n(com sua posição no ranking de mais votados)");

        for (int i = 0; i < candidatosMaisVotados.size(); i++) {
            Candidato candidato = candidatosMaisVotados.get(i);
            boolean noRelatorio1 = candidatosEleitos.contains(candidato);

            if (!noRelatorio1) {
                String nomeCandidato = candidato.getNmUrnaCandidato().replace("\"", "");
                String nomePartido = candidato.getSgPartido().replace("\"", "");
                int votos = candidato.getQtVotos();
                int numeroFed = candidato.getNrFederacao();
        
                if (numeroFed != -1) {
                    System.out.println((i + 1) + " - *" + nomeCandidato + " (" + nomePartido + ", " + df.format(votos) + " votos)");
                } else {
                    System.out.println((i + 1) + " - " + nomeCandidato + " (" + nomePartido + ", " + df.format(votos) + " votos)");
                }
            }
        }

        //Relatório 5
        System.out.println("\nEleitos, que se beneficiaram do sistema proporcional:\n(com sua posição no ranking de mais votados)");

        for (int i = 0; i < candidatosEleitos.size(); i++) {
            Candidato candidato = candidatosEleitos.get(i);
            
            if (!candidatosMaisVotados.contains(candidato)) {
                int index = candidatos.indexOf(candidato);
                String nomeCandidato = candidato.getNmUrnaCandidato().replace("\"", "");
                String nomePartido = candidato.getSgPartido().replace("\"", "");
                int votos = candidato.getQtVotos();
                int numeroFed = candidato.getNrFederacao();
        
                if (numeroFed != -1) {
                    System.out.println((index + 1) + " - *" + nomeCandidato + " (" + nomePartido + ", " + df.format(votos) + " votos)");
                } else {
                    System.out.println((index + 1) + " - " + nomeCandidato + " (" + nomePartido + ", " + df.format(votos) + " votos)");
                }
            }
        }
    }
}
