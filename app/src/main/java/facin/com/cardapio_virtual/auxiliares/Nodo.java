package facin.com.cardapio_virtual.auxiliares;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by priscila on 18/05/17.
 */

public class Nodo {
    private OntClass ontClass;
    private Map<Restricao, Boolean> mapaRestricoes;
    private List<Nodo> filhos;
    private boolean temQuantidade;
    private int quantidade;

    public Nodo(OntClass ontClass, Map<Restricao, Boolean> mapaRestricoesMamae, List<Individual> individuos) {
        this.ontClass = ontClass;
        filhos = new ArrayList<>();
        temQuantidade = false;
        quantidade = 0;
        inicializaMapaRestricoes();
        intercalaRestricoesParaCima(mapaRestricoesMamae);
        calculaQuantidade(individuos);
    }

    // Construtor da raiz
    public Nodo(OntClass ontClass) {
        this.ontClass = ontClass;
        filhos = new ArrayList<>();
        mapaRestricoes = new HashMap<>();
        for (Restricao r : Restricao.values()) {
            mapaRestricoes.put(r, null);
        }
    }

    private void calculaQuantidade(List<Individual> individuos) {
        if (ontClass.listSubClasses().toList().isEmpty()) {
            if (individuos != null) {
                for (Individual i : individuos) {
                    if (i.getOntClass().equals(ontClass)) {
                        temQuantidade = true;
                        quantidade++;
                    }
                }
            }
        }
    }

    private void inicializaMapaRestricoes() {
        mapaRestricoes = new HashMap<>();
        for (Restricao r : Restricao.values()) {
            Boolean valorDaRestricao = VerificadorDeRestricao.verificaRestricao(ontClass, r);
            mapaRestricoes.put(r, valorDaRestricao);
        }
    }

    private void intercalaRestricoesParaCima(Map<Restricao, Boolean> mapaRestricoesMamae) {
        for (Map.Entry<Restricao, Boolean> kvOrigem : mapaRestricoes.entrySet()) {
            // Se no mapaRestricoes o valor for nulo, substitui pelo valor do novo vetor
            if (kvOrigem.getValue() == null) {
                mapaRestricoes.put(kvOrigem.getKey(), mapaRestricoesMamae.get(kvOrigem.getKey()));
            }
        }
    }

    public void intercalaRestricoesParaBaixo(Map<Restricao, Boolean> mapaRestricoesFilho, boolean temQuantidadeFilho) {
        for (Map.Entry<Restricao, Boolean> kvOrigem : mapaRestricoes.entrySet()) {
            // Se no mapaOrigem o valor for nulo, substitui pelo valor do novo vetor
            if (kvOrigem.getValue() == null) {
                mapaRestricoes.put(kvOrigem.getKey(), mapaRestricoesFilho.get(kvOrigem.getKey()));
            } else if (mapaRestricoesFilho.get(kvOrigem.getKey()) != null) {
                // Se o valor não for nulo, mas for indesejado...
                if (verificaConjuntoRestricao(kvOrigem.getKey(), mapaRestricoesFilho.get(kvOrigem.getKey()))) {
                    mapaRestricoes.put(kvOrigem.getKey(), mapaRestricoesFilho.get(kvOrigem.getKey()));
                }
            }
        }
        if (temQuantidadeFilho)
            temQuantidade = true;
    }

    private boolean verificaConjuntoRestricao(Restricao restricao, Boolean valor) {
        switch (restricao) {
            case CONTAVEL:
                return !valor;
            case GLUTEN:
                return !valor;
            case LACTOSE:
                return !valor;
            case PRECO:
                return valor;
            case TEMGORDURAS:
                return !valor;
            case TEMSAL:
                return !valor;
            case VEGETARIANO:
                return valor;
            default:
                return false;
        }
    }

    public OntClass getOntClass() {
        return ontClass;
    }

    public void setOntClass(OntClass ontClass) {
        this.ontClass = ontClass;
    }

    public Map<Restricao, Boolean> getMapaRestricoes() {
        return mapaRestricoes;
    }

    public void setMapaRestricoes(Map<Restricao, Boolean> mapaRestricoes) {
        this.mapaRestricoes = mapaRestricoes;
    }

    public List<Nodo> getFilhos() {
        return filhos;
    }

    public void setFilhos(List<Nodo> filhos) {
        this.filhos = filhos;
    }

    public boolean isTemQuantidade() {
        return temQuantidade;
    }

    public void setTemQuantidade(boolean temQuantidade) {
        this.temQuantidade = temQuantidade;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
