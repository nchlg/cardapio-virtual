package facin.com.cardapio_virtual;

import java.util.ArrayList;

/**
 * Created by Priscila on 28/02/2017.
 */

public class Product {

    private String nome;
    private double preco;
    private ArrayList<String> ingredientes;
    private int quantidade;

    public Product(String nome, double preco, int quantidade) {
        this.nome = nome;
        this.preco = preco;
        ingredientes = new ArrayList<>();
        this.quantidade = quantidade;
    }

    public Product(String nome, double preco, ArrayList<String> ingredientes, int quantidade) {
        this.nome = nome;
        this.preco = preco;
        this.ingredientes = ingredientes;
        this.quantidade = quantidade;
    }

    public Product() {
        nome = "";
        preco = 0.0;
        ingredientes = new ArrayList<>();
        quantidade = 0;
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
}
