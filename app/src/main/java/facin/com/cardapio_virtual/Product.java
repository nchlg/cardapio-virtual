package facin.com.cardapio_virtual;

import android.util.Log;

import com.hp.hpl.jena.ontology.OntClass;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import facin.com.cardapio_virtual.auxiliares.Restricao;

/**
 * Created by Priscila on 28/02/2017.
 */

public class Product {

    private String nome;
    private double preco;
    private List<String> ingredientes;
    private int quantidade;
    private int preferencia;
    private OntClass ontClass;
    // Filtros
    private Map<Restricao, Boolean> mapaRestricoes;
    // Ordenação
    private int acessos;


    // Sem ingredientes
    public Product(String nome, double preco, int quantidade, OntClass ontClass,
                   Map<Restricao, Boolean> mapaRestricoes) {
        this(nome, preco, new ArrayList<String>(), quantidade, ontClass, mapaRestricoes);
    }

    // Sem quantidade
    public Product(String nome, double preco, List<String> ingredientes, OntClass ontClass,
                   Map<Restricao, Boolean> mapaRestricoes) {
        this(nome, preco, ingredientes, 0, ontClass, mapaRestricoes);

    }

    // Produto intermediário
    public Product(String nome, OntClass ontClass,
                   Map<Restricao, Boolean> mapaRestricoes) {
        this(nome, 0, new ArrayList<String>(), 0, ontClass, mapaRestricoes);
    }

    // Contrutor vazio
    public Product() {
        this("", 0, new ArrayList<String>(), 0, null, new HashMap<facin.com.cardapio_virtual.auxiliares.Restricao, Boolean>());
    }

    // Contrutor completo
    public Product(String nome, double preco, List<String> ingredientes, int quantidade, OntClass ontClass,
                   Map<Restricao, Boolean> mapaRestricoes) {
        this.nome = nome;
        this.preco = preco;
        this.ingredientes = ingredientes;
        this.quantidade = quantidade;
        preferencia = 0;
        this.ontClass = ontClass;
        // Propriedades da Ontologia
        // Filtros
        this.mapaRestricoes = mapaRestricoes;
    }

    public String getIngredientesAsString() throws NullPointerException {
        String ingredientesAsString = "";
        for (String ingr : ingredientes) {
            ingredientesAsString = ingredientesAsString + ingr.toLowerCase();
            if (!ingredientes.get(ingredientes.size() - 1).equals(ingr))
                ingredientesAsString = ingredientesAsString + ", ";
        }
        return ingredientesAsString;
    }

    // TODO: Verificar String format
    public String getPrecoAsString() {

//        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.);
        NumberFormat numberFormat = new DecimalFormat("R$###,##0.00");
//        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
//        decimalFormat.applyPattern("R$###,##0.00");
        return numberFormat.format(preco);
//        return "R$" + String.format("%.2f", preco).replaceAll("\\.", ",");
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public List<String> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(ArrayList<String> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getPreferencia() {
        return preferencia;
    }

    public void setPreferencia(int preferencia) {
        this.preferencia = preferencia;
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

    public int getAcessos() {
        return acessos;
    }

    public void setAcessos(int acessos) {
        this.acessos = acessos;
    }
}
