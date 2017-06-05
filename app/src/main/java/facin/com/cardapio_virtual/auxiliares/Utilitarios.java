package facin.com.cardapio_virtual.auxiliares;

import android.location.Location;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import facin.com.cardapio_virtual.Restaurant;

/**
 * Created by priscila on 16/05/17.
 */

public class Utilitarios {

    public static void ordenaRestaurantes(List<Restaurant> restaurantes, final Location localizacaoAtual) {
        Collections.sort(restaurantes, new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant r1, Restaurant r2) {

                Location locationR1 = new Location("R1");
                locationR1.setLatitude(r1.getLatitude());
                locationR1.setLongitude(r1.getLongitude());

                Location locationR2 = new Location("R2");
                locationR2.setLatitude(r2.getLatitude());
                locationR2.setLongitude(r2.getLongitude());
                if (localizacaoAtual == null) {
                    String nomeR1 = r1.getNome();
                    String nomeR2 = r2.getNome();
                    return nomeR1.compareTo(nomeR2);
                } else {
                    // Se r1 está mais próximo que r2
                    if (locationR1.distanceTo(localizacaoAtual) < locationR2.distanceTo(localizacaoAtual)) {
                        return -1;
                    } else if (locationR1.distanceTo(localizacaoAtual) > locationR2.distanceTo(localizacaoAtual)) {
                        return 1;
                    }
                    // Se os dois têm a mesma distância:
                    else {
                        String nomeR1 = r1.getNome();
                        String nomeR2 = r2.getNome();
                        return nomeR1.compareTo(nomeR2);
                    }
                }
            }
        });
    }
}
