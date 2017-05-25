package facin.com.cardapio_virtual.auxiliares;

import android.support.annotation.Nullable;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    private double preco;
    private List<String> ingredientes;

    public Nodo(OntClass ontClass, List<Individual> individuos,
                Nodo mamae) {
        this.ontClass = ontClass;
        filhos = new ArrayList<>();
        temQuantidade = false;
        quantidade = 0;
        preco = Double.NEGATIVE_INFINITY;
        ingredientes = new ArrayList<>();
        verificaSuperClasses();
        intercalaIngredientes(mamae.getIngredientes());
        intercalaRestricoesParaCima(mamae.getMapaRestricoes());
        calculaQuantidade(individuos);
        intercalaPreco(mamae.getPreco());
    }

    // Construtor da raiz
    public Nodo(OntClass ontClass) {
        this.ontClass = ontClass;
        filhos = new ArrayList<>();
        mapaRestricoes = new HashMap<>();
        ingredientes = new ArrayList<>();
        for (Restricao r : Restricao.values()) {
            mapaRestricoes.put(r, null);
        }
    }

    private void intercalaIngredientes(List<String> ingredientesMamae) {
        for (String ingredienteMamae : ingredientesMamae) {
            if (!ingredientes.contains(ingredienteMamae)) {
                ingredientes.add(ingredienteMamae);
            }
        }
    }

    private void intercalaPreco(double precoMamae) {
        if(preco == Double.NEGATIVE_INFINITY) {
            preco = precoMamae;
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

    // Método tbm conhecido como "inicializa"
    private void verificaSuperClasses() {
        mapaRestricoes = new HashMap<>();
        for (Restricao r : Restricao.values()) {
            if (r != Restricao.TEMINGREDIENTE) {
                mapaRestricoes.put(r, null);
            }
        }
        for (Iterator<OntClass> superClasses = ontClass.listSuperClasses(); superClasses.hasNext(); ) {
            OntClass superClasse = superClasses.next();
            if (superClasse.isRestriction()) {
                Restriction restriction = superClasse.asRestriction();
                String label = restriction.getOnProperty().getLabel("pt");
                if (label != null) {
                    //CONTAVEL("Contável"),
                    if (label.equals(Restricao.CONTAVEL.getDescricao())) {
                        if (label.equals(Restricao.CONTAVEL.getDescricao())) {
                            mapaRestricoes.put(Restricao.CONTAVEL,
                                    restriction.asHasValueRestriction().getHasValue().as(Literal.class).getBoolean());
                        }
                    }
                    //GLUTEN("Glúten"),
                    else if (label.equals(Restricao.GLUTEN.getDescricao())) {
                        if (label.equals(Restricao.GLUTEN.getDescricao())) {
                            mapaRestricoes.put(Restricao.GLUTEN,
                                    restriction.asHasValueRestriction().getHasValue().as(Literal.class).getBoolean());
                        }
                    }
                    //LACTOSE("Lactose"),
                    else if (label.equals(Restricao.LACTOSE.getDescricao())) {
                        if (label.equals(Restricao.LACTOSE.getDescricao())) {
                            mapaRestricoes.put(Restricao.LACTOSE,
                                    restriction.asHasValueRestriction().getHasValue().as(Literal.class).getBoolean());
                        }
                    }
                    //PRECO("Preço"),
                    else if (label.equals(Restricao.PRECO.getDescricao())) {
                        if (label.equals(Restricao.PRECO.getDescricao())) {
                            mapaRestricoes.put(Restricao.PRECO, true);
                            preco = restriction.asHasValueRestriction().getHasValue().as(Literal.class).getDouble();
                        }
                    }
                    //TEMGORDURAS("Tem gorduras"),
                    else if (label.equals(Restricao.TEMGORDURAS.getDescricao())) {
                        if (label.equals(Restricao.TEMGORDURAS.getDescricao())) {
                            mapaRestricoes.put(Restricao.TEMGORDURAS,
                                    !restriction.asSomeValuesFromRestriction()
                                            .getSomeValuesFrom()
                                            .as(OntClass.class)
                                            .getLabel("pt")
                                            .equals("Não Gorduroso"));
                        }
                    }
                    //TEMINGREDIENTE("Tem ingrediente")
                    else if (label.equals(Restricao.TEMINGREDIENTE.getDescricao())) {
                        if (label.equals("Tem ingrediente")) {
                            ingredientes.add(restriction
                                    .asSomeValuesFromRestriction()
                                    .getSomeValuesFrom()
                                    .as(OntClass.class)
                                    .getLabel("pt"));
                        }
                    }
                    //TEMSAL("Tem sal"),
                    else if (label.equals(Restricao.TEMSAL.getDescricao())) {
                        if (label.equals(Restricao.TEMSAL.getDescricao())) {
                            mapaRestricoes.put(Restricao.TEMSAL,
                                    !restriction.asSomeValuesFromRestriction()
                                            .getSomeValuesFrom()
                                            .as(OntClass.class)
                                            .getLabel("pt")
                                            .equals("Pouco Salgado"));
                        }
                    }
                    //VEGETARIANO("Vegetariano");
                    else if (label.equals(Restricao.VEGETARIANO.getDescricao())) {
                        if (label.equals(Restricao.VEGETARIANO.getDescricao())) {
                            mapaRestricoes.put(Restricao.VEGETARIANO,
                                    restriction.asHasValueRestriction().getHasValue().as(Literal.class).getBoolean());
                        }
                    }
                }
            }
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

    public List<String> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<String> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public double getPreco() {
        return preco;
    }
}
