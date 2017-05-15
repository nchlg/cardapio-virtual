package facin.com.cardapio_virtual;

/**
 * Created by 13108306 on 13/01/2017.
 */
public class Restaurant {

    private int primaryKey;
    private String nome;
    private String email;
    private String telefone;
    private String endereco;
    private double latitude;
    private double longitude;
    private String descricao;
    private boolean favorito;

    public Restaurant(int primaryKey) {
        this.primaryKey = primaryKey;
        nome = "";
        email = "";
        telefone = "";
        endereco = "";
        latitude = 0;
        longitude = 0;
        descricao = "";
        favorito = false;
    }

    public Restaurant(int primaryKey, String nome, String email, String telefone, String endereco,
                      double latitude, double longitude, String descricao, boolean favorito) {
        this.primaryKey = primaryKey;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.descricao = descricao;
        this.endereco = endereco;
        this.latitude = latitude;
        this.longitude = longitude;
        this.favorito = favorito;
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

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }
}
