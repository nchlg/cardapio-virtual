package facin.com.cardapio_virtual;

import java.util.List;

/**
 * Created by priscila on 03/05/17.
 */

public class Filtros {
    private static class FiltroGluten implements FiltroInterface {
        @Override
        public List<Product> filtra(List<Product> produtos) {
            return produtos;
        }
    }
    private static class FiltroLactose implements FiltroInterface {
        @Override
        public List<Product> filtra(List<Product> produtos) {
            return produtos;
        }
    }
    private static class FiltroVegetariano implements FiltroInterface {
        @Override
        public List<Product> filtra(List<Product> produtos) {
            return produtos;
        }
    }

    public static FiltroInterface getFiltroGluten() {
        return new FiltroGluten();
    }
    public static FiltroInterface getFiltroLactose() {
        return new FiltroLactose();
    }
    public static FiltroInterface getFiltroVegetariano() {
        return new FiltroVegetariano();
    }
}
