package facin.com.cardapio_virtual;

/**
 * Created by 13108306 on 13/01/2017.
 */
public class Restaurant {

    // private int primaryKey;
    private String nome;
    private String email;
    private String telefone;
    private String endereco;
    private double latitude;
    private double longitude;
    private String descricao;

    public Restaurant() {
        // primaryKey = 0;
        nome = "";
        email = "";
        telefone = "";
        endereco = "";
        latitude = 0;
        longitude = 0;
        descricao = "";
    }

    public Restaurant(String nome, String email, String telefone, String endereco,
                      double latitude, double longitude, String descricao) {
        // this.primaryKey = primaryKey;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.descricao = descricao;
        this.endereco = endereco;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Restaurant(Boolean teste) {
        if (teste) {
            // primaryKey = 100;
            nome = "Bar do 5";
            email = "bardo5@mail.com";
            telefone = "(XXX) XXXX-XXXX";
            endereco = "Av. Ipiranga, 6681";
            latitude = -30.059947;
            longitude = -51.174464;
            descricao = "Bar do prédio de História que oferece deliciosas opções vegetarianas e um ambiente aconchegante.";
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
