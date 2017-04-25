package facin.com.cardapio_virtual;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Priscila on 28/02/2017.
 */

public class Product {

    private String nome;
    private double preco;
    private ArrayList<String> ingredientes;
    private int quantidade;
    private boolean contavel;
    private int preferencia;

    // Sem ingredientes
    public Product(String nome, double preco, int quantidade, boolean contavel) {
        this.nome = nome;
        this.preco = preco;
        ingredientes = new ArrayList<>();
        this.quantidade = quantidade;
        this.contavel = contavel;
        preferencia = 0;
    }

    // Sem quantidade
    public Product(String nome, double preco, ArrayList<String> ingredientes, boolean contavel) {
        this.nome = nome;
        this.preco = preco;
        ingredientes = ingredientes;
        this.quantidade = 0;
        this.contavel = contavel;
        preferencia = 0;
    }

    public Product(String nome, double preco, ArrayList<String> ingredientes, int quantidade, boolean contavel) {
        this.nome = nome;
        this.preco = preco;
        this.ingredientes = ingredientes;
        this.quantidade = quantidade;
        this.contavel = contavel;
        preferencia = 0;
    }

    public Product() {
        nome = "";
        preco = 0.0;
        ingredientes = new ArrayList<>();
        quantidade = 0;
        preferencia = 0;
    }

    public String getIngredientesAsString() {
        String ingredientesAsString = "";
        for (String ingr : ingredientes) {
            ingredientesAsString = ingredientesAsString + ingr.toLowerCase();
            if (!ingredientes.get(ingredientes.size() - 1).equals(ingr))
                ingredientesAsString = ingredientesAsString + ", ";
        }
        return ingredientesAsString;
    }

    public String getPrecoAsString() {

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        return "R$ " + decimalFormat.format(preco);
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

    public ArrayList<String> getIngredientes() {
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

    public boolean isContavel() {
        return contavel;
    }

    public void setContavel(boolean contavel) {
        this.contavel = contavel;
    }
}
