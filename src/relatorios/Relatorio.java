package relatorios;

import eleicao.Eleicao;

public abstract class Relatorio {
    protected Eleicao eleicao;

    public Relatorio(Eleicao eleicao) {
        this.eleicao = eleicao;
    }
}
