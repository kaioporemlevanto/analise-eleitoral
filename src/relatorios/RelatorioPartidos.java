package relatorios;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import eleicao.Candidato;
import eleicao.Eleicao;
import eleicao.Partido;

public class RelatorioPartidos extends Relatorio {
    private long votosNominaisCounter;
    private long votosLegendaCounter;

    public RelatorioPartidos(Eleicao eleicao) {
        super(eleicao);
    }

    public long getVotosLegendaCounter() {
        return votosLegendaCounter;
    }

    public long getVotosNominaisCounter() {
        return votosNominaisCounter;
    }

    public void gerarRelatorioPartidos() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DecimalFormat df = new DecimalFormat("#,###", symbols);
        List<Candidato> candidatos = eleicao.getCandidatos();
        List<Candidato> candidatosEleitos = eleicao.getCandidatosEleitos(candidatos);
        Map<Integer, Partido> partidosMap = eleicao.getPartidosMap();
        List<Partido> partidos = new ArrayList<>(partidosMap.values());

        partidos.sort(Comparator.comparingInt(Partido::getTotalVotos).reversed());

        // Relatório 6
        System.out.println("\nVotação dos partidos e número de candidatos eleitos:");

        for (int i = 0; i < partidos.size(); i++) {
            Partido partido = partidos.get(i);
            String nomePartido = partido.getSiglaPartido().replace("\"", "");
            int totalVotos = partido.getTotalVotos();
            int numeroPartido = partido.getNumeroPartido();
            int votosNominais = partido.getTotalVotosNominais();
            int votosLegenda = partido.getQtVotos();
            int candidatosEleitosNoPartido = contarCandidatosEleitosNoPartido(partido, candidatosEleitos);

            this.votosLegendaCounter += votosLegenda;
            this.votosNominaisCounter += votosNominais;
            String candidatosText = (candidatosEleitosNoPartido <= 1) ? "candidato eleito" : "candidatos eleitos";
            System.out.println((i + 1) + " - " + nomePartido + " - " + numeroPartido + ", " + df.format(totalVotos)
                    + " votos (" + df.format(votosNominais) + " nominais e " + df.format(votosLegenda)
                    + " de legenda), " + df.format(candidatosEleitosNoPartido) + " " + candidatosText);
        }

        // Relatório 7
        System.out.println("\nPrimeiro e último colocados de cada partido:");
        int i = 0;
        partidos.sort((partido1, partido2) -> {
            List<Candidato> candidatosPartido1 = partido1.getCandidatos();
            List<Candidato> candidatosPartido2 = partido2.getCandidatos();

            candidatosPartido1.sort(Comparator.comparingInt(Candidato::getQtVotos).reversed());
            candidatosPartido2.sort(Comparator.comparingInt(Candidato::getQtVotos).reversed());

            if (!candidatosPartido1.isEmpty() && !candidatosPartido2.isEmpty()) {
                int votosPartido1 = candidatosPartido1.get(0).getQtVotos();
                int votosPartido2 = candidatosPartido2.get(0).getQtVotos();
                return Integer.compare(votosPartido2, votosPartido1);
            } else if (!candidatosPartido1.isEmpty()) {
                return -1;
            } else if (!candidatosPartido2.isEmpty()) {
                return 1;
            } else {
                return 0;
            }
        });
        for (Partido partido : partidos) {
            List<Candidato> candidatosDoPartido = partido.getCandidatos();
            candidatosDoPartido.sort(Comparator.comparingInt(Candidato::getQtVotos).reversed());

            Candidato primeiroColocado = candidatosDoPartido.get(0);
            Candidato ultimoColocado = candidatosDoPartido.get(candidatosDoPartido.size() - 1);

            if (!(primeiroColocado.getQtVotos() == 0 && ultimoColocado.getQtVotos() == 0)) {
                String primeiroColocadoTexto = (primeiroColocado.getQtVotos() <= 1) ? "voto" : "votos";
                String ultimoColocadoTexto = (ultimoColocado.getQtVotos() <= 1) ? "voto" : "votos";

                System.out.print((i + 1) + " - " + partido.getSiglaPartido().replace("\"", "") + " - "
                        + partido.getNumeroPartido() + ", ");
                i += 1;
                System.out.println(primeiroColocado.getNmUrnaCandidato().replace("\"", "") + " ("
                        + primeiroColocado.getNrCandidato() + ", " + df.format(primeiroColocado.getQtVotos()) + " "
                        + primeiroColocadoTexto + ") / " + ultimoColocado.getNmUrnaCandidato().replace("\"", "") + " ("
                        + ultimoColocado.getNrCandidato() + ", " + df.format(ultimoColocado.getQtVotos()) + " "
                        + ultimoColocadoTexto + ")");
            }
        }
    }

    private int contarCandidatosEleitosNoPartido(Partido partido, List<Candidato> candidatosEleitos) {
        int contador = 0;
        for (Candidato candidato : partido.getCandidatos()) {
            if (candidatosEleitos.contains(candidato)) {
                contador++;
            }
        }
        return contador;
    }
}
