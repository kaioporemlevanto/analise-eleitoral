package eleicao;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Eleicao {
    private boolean isEstadual;
    private Map<Integer, Candidato> candidatoMap;//<NmCandidato, candidato> surge logo após a leitura de votações, e seta o qtVotos de cada candidato
    private List<Candidato> candidatos;//Surge após a leitura do csv de candidatos
    private List<Votacao> votacoes; //NmVotavel e QtVotos, ela já relacionou cada voto ao seu candidato que está em candidatoMap
    private Map<Integer, Integer> votosPorNumeroVotavel = new HashMap<>(); //<Nvotavel, Qt votos> surge após csv de votação
    private Date dataEleicao;

    public boolean isEstadual() {
        return isEstadual;
    }

    public Date getDataEleicao() {
        return dataEleicao;
    }

    public List<Votacao> getVotacoes() {
        return new ArrayList<Votacao>(votacoes);
    }

    public List<Candidato> getCandidatos() {
        return new ArrayList<Candidato>(candidatos);
    }

    public Eleicao(boolean isEstadual, String arquivoCandidatos, String arquivoVotacao, String dataEleicao) {
        this.isEstadual = isEstadual;
        candidatoMap = new HashMap<>();
        candidatos = lerCandidatos(arquivoCandidatos);
        votacoes = lerVotacoes(arquivoVotacao);
        try {
            this.dataEleicao = new SimpleDateFormat("dd/MM/yyyy").parse(dataEleicao);
        } catch (ParseException e) {
            this.dataEleicao = null;
        }
    }

    private List<Candidato> lerCandidatos(String arquivoCandidatos) {
        List<Candidato> candidatos = new ArrayList<>();

        try (BufferedReader candidatosReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(arquivoCandidatos), "ISO-8859-1"))) {
            String candidatosLine;
            boolean firstLineCandidatos = true;
            while ((candidatosLine = candidatosReader.readLine()) != null) {

                if (firstLineCandidatos) {
                    firstLineCandidatos = false;
                    continue;
                }

                String[] candidatosData = candidatosLine.split(";");
                int cargo = Integer.parseInt(candidatosData[13].replace("\"", ""));

                if ((isEstadual && cargo == 7) || (!isEstadual && cargo == 6)) {
                    Candidato candidato = new Candidato(
                            cargo,
                            Integer.parseInt(candidatosData[68].replace("\"", "")), // CD_SITUACAO_CANDIDATO_TOT
                            Integer.parseInt(candidatosData[16].replace("\"", "")), // NR_CANDIDATO
                            candidatosData[18], // NM_URNA_CANDIDATO
                            Integer.parseInt(candidatosData[27].replace("\"", "")), // NR_PARTIDO
                            candidatosData[28], // SG_PARTIDO
                            Integer.parseInt(candidatosData[30].replace("\"", "")), // NR_FEDERACAO
                            candidatosData[42], // DT_NASCIMENTO
                            Integer.parseInt(candidatosData[56].replace("\"", "")), // CD_SIT_TOT_TURNO
                            Integer.parseInt(candidatosData[45].replace("\"", "")), // CD_GENERO
                            candidatosData[67] // NM_TIPO_DESTINACAO_VOTOS
                    );
                    candidatos.add(candidato);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return candidatos;
    }

    private List<Votacao> lerVotacoes(String arquivoVotacao) {
        List<Votacao> votacoes = new ArrayList<>();

        try (BufferedReader votacaoReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(arquivoVotacao), "ISO-8859-1"))) {
            String votacaoLine;
            boolean firstLineVotacao = true;

            while ((votacaoLine = votacaoReader.readLine()) != null) {
                if (firstLineVotacao) {
                    firstLineVotacao = false;
                    continue;
                }

                String[] votacaoData = votacaoLine.split(";");
                int cargo = Integer.parseInt(votacaoData[17].replace("\"", ""));

                if ((isEstadual && cargo == 7) || (!isEstadual && cargo == 6)) {
                    int numeroVotavel = Integer.parseInt(votacaoData[19].replace("\"", ""));
                    int quantidadeVotos = Integer.parseInt(votacaoData[21].replace("\"", ""));

                    if (votosPorNumeroVotavel.containsKey(numeroVotavel)) {
                        quantidadeVotos += votosPorNumeroVotavel.get(numeroVotavel);
                    }

                    votosPorNumeroVotavel.put(numeroVotavel, quantidadeVotos);
                }
            }

            for (Map.Entry<Integer, Integer> entry : votosPorNumeroVotavel.entrySet()) {
                Votacao votacao = new Votacao(entry.getKey(), entry.getValue());
                votacoes.add(votacao);
            }

            relacionarVotosCandidatos(candidatos, votacoes);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return votacoes;
    }

    public Map<Integer, Partido> agruparCandidatosPorPartido(List<Candidato> candidatos, List<Votacao> votacoes) {
        Map<Integer, Partido> partidosMap = new HashMap<>();

        for (Candidato candidato : candidatos) {
            int numeroPartido = candidato.getNrPartido();
            if (!partidosMap.containsKey(numeroPartido)) {
                Partido novoPartido = new Partido(numeroPartido, candidato.getSgPartido());
                partidosMap.put(numeroPartido, novoPartido);
            }
        }

        for (Votacao votacao : votacoes) {
            int numeroPartido = votacao.getNrVotavel();
            if (partidosMap.containsKey(numeroPartido)) {
                Partido partido = partidosMap.get(numeroPartido);
                partido.incrementaVotos(votacao.getQtVotos());
            }
        }

        for (Candidato candidato : candidatos) {
            int numeroPartido = candidato.getNrPartido();
            if (partidosMap.containsKey(numeroPartido)) {
                Partido partido = partidosMap.get(numeroPartido);
                partido.adicionaCandidato(candidato);
            }
        }

        return partidosMap;
    }

    public List<Candidato> getCandidatosEleitos(List<Candidato> candidatos) {
        List<Candidato> candidatosEleitos = new ArrayList<>();

        for (Candidato candidato : candidatos) {
            int situacaoTurno = candidato.getCdSitTotTurno();

            if (situacaoTurno == 2 || situacaoTurno == 3) {
                candidatosEleitos.add(candidato);
            }
        }

        return getCandidatosMaisVotados(candidatosEleitos);
    }

    public void relacionarVotosCandidatos(List<Candidato> candidatos, List<Votacao> votacoes) {

        for (Candidato candidato : candidatos) {
            candidatoMap.put(candidato.getNrCandidato(), candidato);
        }

        for (Votacao votacao : votacoes) {
            int nrVotavel = votacao.getNrVotavel();

            if (candidatoMap.containsKey(nrVotavel)) {
                int qtVotos = votacao.getQtVotos();
                Candidato candidato = candidatoMap.get(nrVotavel);
                candidato.setQtVotos(qtVotos);
            }
        }
    }

    public List<Candidato> getCandidatosMaisVotados(List<Candidato> candidatos) {
        candidatos.sort(Comparator.comparingInt(Candidato::getQtVotos).reversed());
        return candidatos;
    }

    // public List<Candidato> getCandidatosMaisVotados(List<Candidato> candidatos) {
    //     List<Candidato> candidatosVotosDecrescentes = this.getCandidatos();
    //     candidatosVotosDecrescentes.sort(Comparator.comparingInt(Candidato::getQtVotos).reversed());
    //     return candidatosVotosDecrescentes;
    // }

    public int getQtDeputadosEleitos() {
        return this.getCandidatosEleitos(candidatos).size();
    }
}
