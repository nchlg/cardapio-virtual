package facin.com.cardapio_virtual.auxiliares;

/**
 * Created by priscila on 08/05/17.
 */

public enum Restricao {
    CONTAVEL("Contável"),
    GLUTEN("Glúten"),
    LACTOSE("Lactose"),
    PRECO("Preço"),
    TEMGORDURAS("Tem gorduras"),
    TEMINGREDIENTE("Tem ingrediente"),
    TEMSAL("Tem sal"),
    VEGETARIANO("Vegetariano");

    private String descricao;

    private Restricao(String descricao) {
        this.descricao = descricao;
    }

    public final String getDescricao() {
        return descricao;
    }
}
