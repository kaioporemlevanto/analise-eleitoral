package eleicao;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Candidato {
    private int cdCargo;
    private int cdSituacaoCandidato;
    private int nrCandidato;
    private String nmUrnaCandidato;
    private int nrPartido;
    private String sgPartido;
    private int nrFederacao;
    private Date dtNascimento;
    private int cdSitTotTurno;
    private int cdGenero;
    private String nmTipoDestinacaoVotos;
    private int qtVotos;
    private int Indice;

    public Candidato(int cdCargo, int cdSituacaoCandidato, int nrCandidato, String nmUrnaCandidato,
    int nrPartido, String sgPartido, int nrFederacao, String dtNascimento, int cdSitTotTurno,
    int cdGenero, String nmTipoDestinacaoVotos) {
        this.cdCargo = cdCargo;
        this.cdSituacaoCandidato = cdSituacaoCandidato;
        this.nrCandidato = nrCandidato;
        this.nmUrnaCandidato = nmUrnaCandidato;
        this.nrPartido = nrPartido;
        this.sgPartido = sgPartido;
        this.nrFederacao = nrFederacao;
        this.cdSitTotTurno = cdSitTotTurno;
        this.cdGenero = cdGenero;
        this.nmTipoDestinacaoVotos = nmTipoDestinacaoVotos;
        dtNascimento = dtNascimento.replace("\"", "");
        try {
            this.dtNascimento = new SimpleDateFormat("dd/MM/yyyy").parse(dtNascimento);
        } catch (ParseException e) {
            this.dtNascimento = null;
        }
    }
    
    public int getIndice() {
        return Indice;
    }

    public void setIndice(int indice) {
        Indice = indice;
    }

    public int getCdCargo() {
        return cdCargo;
    }

    public int getCdSituacaoCandidato() {
        return cdSituacaoCandidato;
    }

    public int getNrCandidato() {
        return nrCandidato;
    }

    public String getNmUrnaCandidato() {
        return nmUrnaCandidato;
    }

    public int getNrPartido() {
        return nrPartido;
    }

    public String getSgPartido() {
        return sgPartido;
    }

    public int getNrFederacao() {
        return nrFederacao;
    }

    public Date getDtNascimento() {
        return dtNascimento;
    }

    public int getCdSitTotTurno() {
        return cdSitTotTurno;
    }

    public int getCdGenero() {
        return cdGenero;
    }

    public String getNmTipoDestinacaoVotos() {
        return nmTipoDestinacaoVotos;
    }

    public void setQtVotos(int qtVotos) {
        this.qtVotos = qtVotos;
    }
    public int getQtVotos() {
        return qtVotos;
    }
}
