package facin.com.cardapio_virtual.auxiliares;

import java.util.ArrayList;
import java.util.List;

import facin.com.cardapio_virtual.Product;

/**
 * Created by priscila on 03/05/17.
 */

public class Filtros {
    private static class FiltroGluten implements FiltroInterface {
        @Override
        public List<Product> filtra(List<Product> produtos) {
            List<Product> produtosFiltrados = new ArrayList<>();
            for (Product p : produtos) {
                if (!p.getMapaRestricoes().get("gluten"))
                    produtosFiltrados.add(p);
            }
            return produtosFiltrados;
        }
    }
    private static class FiltroLactose implements FiltroInterface {
        @Override
        public List<Product> filtra(List<Product> produtos) {
            List<Product> produtosFiltrados = new ArrayList<>();
            for (Product p : produtos) {
                if (!p.getMapaRestricoes().get("lactose"))
                    produtosFiltrados.add(p);
            }
            return produtosFiltrados;
        }
    }
    private static class FiltroVegetariano implements FiltroInterface {
        @Override
        public List<Product> filtra(List<Product> produtos) {
            List<Product> produtosFiltrados = new ArrayList<>();
            for (Product p : produtos) {
                if (p.getMapaRestricoes().get("vegetariano"))
                    produtosFiltrados.add(p);
            }
            return produtosFiltrados;
        }
    }
    private static class FiltroSal implements FiltroInterface {
        @Override
        public List<Product> filtra(List<Product> produtos) {
            List<Product> produtosFiltrados = new ArrayList<>();
            for (Product p : produtos) {
                if (!p.getMapaRestricoes().get("temSal"))
                    produtosFiltrados.add(p);
            }
            return produtosFiltrados;
        }
    }
    private static class FiltroGordura implements FiltroInterface {
        @Override
        public List<Product> filtra(List<Product> produtos) {
            List<Product> produtosFiltrados = new ArrayList<>();
            for (Product p : produtos) {
                if (!p.getMapaRestricoes().get("temGorduras"))
                    produtosFiltrados.add(p);
            }
            return produtosFiltrados;
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
    public static FiltroInterface getFiltroSal() {
        return new FiltroSal();
    }
    public static FiltroInterface getFiltroGordura() {
        return new FiltroGordura();
    }
}
