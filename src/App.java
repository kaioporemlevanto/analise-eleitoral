import eleicao.Eleicao;
import relatorios.RelatorioCandidato;
import relatorios.RelatorioEstatistico;
import relatorios.RelatorioPartidos;

public class App {
    public static void main(String[] args) throws Exception {
        // if (args.length != 4) {
        //     System.out.println(
        //             "Uso correto: java -jar deputados.jar [--estadual|--federal] [arquivo_candidatos.csv] [arquivo_votacao.csv] [data_eleicao]");
        //     return;
        // }

        String tipoEleicao = args[0];
        String arquivoCandidatos = args[1];
        String arquivoVotacao = args[2];
        String dataEleicao = args[3];
        boolean isEstadual = tipoEleicao.equals("--estadual");

        //Gera Eleição
        Eleicao eleicao = new Eleicao(isEstadual, arquivoCandidatos, arquivoVotacao, dataEleicao);

        //Gera Relatórios
        RelatorioCandidato relatorio = new RelatorioCandidato(eleicao);
        relatorio.imprimirRelatorio();

        RelatorioPartidos relatorioPartidos = new RelatorioPartidos(eleicao);
        relatorioPartidos.gerarRelatorioPartidos();

        RelatorioEstatistico relatorioEstatistico = new RelatorioEstatistico(eleicao, relatorioPartidos);
        relatorioEstatistico.gerarRelatorioEstatistico();
        
    }
}
