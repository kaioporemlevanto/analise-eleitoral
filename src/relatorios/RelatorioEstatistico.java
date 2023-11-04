package relatorios;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eleicao.Candidato;
import eleicao.Eleicao;

public class RelatorioEstatistico extends Relatorio {
    private RelatorioPartidos partidoRelatorio;

    public RelatorioEstatistico(Eleicao eleicao, RelatorioPartidos partidoRelatorio) {
        super(eleicao);
        this.partidoRelatorio = partidoRelatorio;
    }

    public void gerarRelatorioEstatistico() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DecimalFormat df = new DecimalFormat("#,###", symbols);
        List<Candidato> candidatos = eleicao.getCandidatos();
        List<Candidato> candidatosEleitos = eleicao.getCandidatosEleitos(candidatos);
        Map<String, Integer> candidatosEleitosPorFaixaEtaria = new HashMap<>();
        Map<String, Integer> faixaEtariaTotais = new HashMap<>();
        
        for (Candidato candidato : candidatosEleitos) {
            int idadeNaDataEleicao = calcularIdadeNaDataEleicao(candidato);
            String faixaEtaria = calcularFaixaEtaria(idadeNaDataEleicao);
            candidatosEleitosPorFaixaEtaria.put(faixaEtaria, candidatosEleitosPorFaixaEtaria.getOrDefault(faixaEtaria, 0) + 1);
        }

        int totalCandidatosEleitos = candidatosEleitos.size();

        for (Candidato candidato : candidatos) {
            int idadeNaDataEleicao = calcularIdadeNaDataEleicao(candidato);
            String faixaEtaria = calcularFaixaEtaria(idadeNaDataEleicao);
            faixaEtariaTotais.put(faixaEtaria, faixaEtariaTotais.getOrDefault(faixaEtaria, 0) + 1);
        }

        List<String> faixasEtariasOrdenadas = Arrays.asList(
        "Idade < 30", "30 <= Idade < 40", "40 <= Idade < 50", "50 <= Idade < 60", "60 <= Idade");

        //Relatório 8
        System.out.println("\n");
        System.out.println("Eleitos, por faixa etária (na data da eleição):");
        for (String faixaEtaria : faixasEtariasOrdenadas) {
            int qtCandidatosEleitos = candidatosEleitosPorFaixaEtaria.getOrDefault(faixaEtaria, 0);
            double porcentagem = (qtCandidatosEleitos / (double) totalCandidatosEleitos) * 100;
            if(faixaEtaria!= "60 <= Idade" && faixaEtaria != "Idade < 30")
                System.out.println(faixaEtaria + ": " + qtCandidatosEleitos + " (" + String.format("%.2f%%", porcentagem) + ")");
            else if(faixaEtaria=="Idade < 30")
                System.out.println("      "+faixaEtaria + ": " + qtCandidatosEleitos + " (" + String.format("%.2f%%", porcentagem) + ")");
            else if(faixaEtaria=="60 <= Idade")
                System.out.println(faixaEtaria + "     : " + qtCandidatosEleitos + " (" + String.format("%.2f%%", porcentagem) + ")");
        }

        int candidatosMasculinos = 0;
        int candidatosFemininos = 0;

        for (Candidato candidato : candidatosEleitos) {
            int genero = candidato.getCdGenero();
            if (genero == 2) {
                candidatosMasculinos++;
            } else if (genero == 4) {
                candidatosFemininos++;
            }
        }
        double porcentagemMasculinos = (candidatosMasculinos / (double) totalCandidatosEleitos) * 100;
        double porcentagemFemininos = (candidatosFemininos / (double) totalCandidatosEleitos) * 100;
        
        //Relatório 9
        System.out.println("\nEleitos, por gênero:");
        System.out.println("Feminino:  " + candidatosFemininos + " (" + String.format("%.2f%%", porcentagemFemininos) + ")");
        System.out.println("Masculino: " + candidatosMasculinos + " (" + String.format("%.2f%%", porcentagemMasculinos) + ")");
        
        long totalVotosValidos = partidoRelatorio.getVotosNominaisCounter() + partidoRelatorio.getVotosLegendaCounter();

        double percentualVotosNominais = (partidoRelatorio.getVotosNominaisCounter() / (double) totalVotosValidos) * 100;
        double percentualVotosLegenda = (partidoRelatorio.getVotosLegendaCounter() / (double) totalVotosValidos) * 100;
        
        //Relatório 10
        System.out.println("\nTotal de votos válidos: " + df.format(totalVotosValidos));
        System.out.println("Total de votos nominais: " + df.format(partidoRelatorio.getVotosNominaisCounter()) + " (" + String.format("%.2f%%", percentualVotosNominais) + ")");
        System.out.println("Total de votos de legenda: " + df.format(partidoRelatorio.getVotosLegendaCounter()) + " (" + String.format("%.2f%%", percentualVotosLegenda) + ")");
    
    }

    private String calcularFaixaEtaria(int idadeNaDataEleicao) {
        if (idadeNaDataEleicao < 30) {
            return "    Idade < 30";
        } else if (idadeNaDataEleicao < 40) {
            return "30 <= Idade < 40";
        } else if (idadeNaDataEleicao < 50) {
            return "40 <= Idade < 50";
        } else if (idadeNaDataEleicao < 60) {
            return "50 <= Idade < 60";
        } else {
            return "60 <= Idade    ";
        }
    }

    public int calcularIdadeNaDataEleicao(Candidato candidato) {
        if (candidato.getDtNascimento() != null && eleicao.getDataEleicao()!= null) {
            Date dataNascimento = candidato.getDtNascimento();
            Date dataEleicao = eleicao.getDataEleicao();
            long diff = dataEleicao.getTime() - dataNascimento.getTime();
            return (int) (diff / (1000 * 60 * 60 * 24 * 365.25));
        }
        return -1;
    }
}
