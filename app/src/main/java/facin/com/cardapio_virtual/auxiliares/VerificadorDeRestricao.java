package facin.com.cardapio_virtual.auxiliares;

import android.util.Log;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.rdf.model.Literal;

import java.util.Iterator;

import facin.com.cardapio_virtual.Restaurant;

/**
 * Created by priscila on 08/05/17.
 */

public class VerificadorDeRestricao {
    public static Boolean verificaRestricao(OntClass ontClass, Restricao restricao) {
        switch (restricao) {
            case CONTAVEL:
                return verificaContavel(ontClass);
            case GLUTEN:
                return verificaGluten(ontClass);
            case LACTOSE:
                return verificaLactose(ontClass);
            case PRECO:
                return verificaPreco(ontClass);
            case TEMGORDURAS:
                return verificaGordura(ontClass);
            case TEMSAL:
                return verificaSal(ontClass);
            case VEGETARIANO:
                return verificaVegetariano(ontClass);
            default:
                return null;
        }
    }

    private static Boolean verificaPreco(OntClass ontClass) {
        for (Iterator<OntClass> superClasses = ontClass.listSuperClasses(); superClasses.hasNext(); ) {
            OntClass superClasse = superClasses.next();
            if (superClasse.isRestriction()) {
                Restriction restriction = superClasse.asRestriction();
                String label = restriction.getOnProperty().getLabel("pt");
                if (label != null) {
                    if (label.equals(Restricao.PRECO.getDescricao())) {
                        return true;
                    }
                }
            }
        }
        return null;
    }

    private static Boolean verificaContavel(OntClass ontClass) {
        for (Iterator<OntClass> superClasses = ontClass.listSuperClasses(); superClasses.hasNext(); ) {
            OntClass superClasse = superClasses.next();
            if (superClasse.isRestriction()) {
                Restriction restriction = superClasse.asRestriction();
                String label = restriction.getOnProperty().getLabel("pt");
                if (label != null) {
                    if (label.equals(Restricao.CONTAVEL.getDescricao())) {
                        return restriction.asHasValueRestriction().getHasValue().as(Literal.class).getBoolean();
                    }
                }
            }
        }
        return null;
    }


    private static Boolean verificaGluten(OntClass ontClass) {
        for (Iterator<OntClass> superClasses = ontClass.listSuperClasses(); superClasses.hasNext(); ) {
            OntClass superClasse = superClasses.next();
            if (superClasse.isRestriction()) {
                Restriction superClassRestriction = superClasse.asRestriction();
                String label = superClassRestriction.getOnProperty().getLabel("pt");
                if (label != null) {
                    if (label.equals(Restricao.GLUTEN.getDescricao())) {
                        return superClassRestriction.asHasValueRestriction().getHasValue().as(Literal.class).getBoolean();
                    }
                }
            }
        }
        return null;
    }

    private static Boolean verificaLactose(OntClass ontClass) {
        for (Iterator<OntClass> superClasses = ontClass.listSuperClasses(); superClasses.hasNext(); ) {
            OntClass superClasse = superClasses.next();
            if (superClasse.isRestriction()) {
                Restriction superClassRestriction = superClasse.asRestriction();
                String label = superClassRestriction.getOnProperty().getLabel("pt");
                if (label != null) {
                    if (label.equals(Restricao.LACTOSE.getDescricao())) {
                        return superClassRestriction.asHasValueRestriction().getHasValue().as(Literal.class).getBoolean();
                    }
                }
            }
        }
        return null;

    }

    private static Boolean verificaVegetariano(OntClass ontClass) {
        for (Iterator<OntClass> superClasses = ontClass.listSuperClasses(); superClasses.hasNext(); ) {
            OntClass superClasse = superClasses.next();
            if (superClasse.isRestriction()) {
                Restriction superClassRestriction = superClasse.asRestriction();
                String label = superClassRestriction.getOnProperty().getLabel("pt");
                if (label != null) {
                    if (label.equals(Restricao.VEGETARIANO.getDescricao())) {
                        return superClassRestriction.asHasValueRestriction().getHasValue().as(Literal.class).getBoolean();
                    }
                }
            }
        }
        return null;

    }

    private static Boolean verificaGordura(OntClass ontClass) {
        for (Iterator<OntClass> superClasses = ontClass.listSuperClasses(); superClasses.hasNext(); ) {
            OntClass superClasse = superClasses.next();
            if (superClasse.isRestriction()) {
                Restriction superClassRestriction = superClasse.asRestriction();
                String label = superClassRestriction.getOnProperty().getLabel("pt");
                if (label != null) {
                    if (label.equals(Restricao.TEMGORDURAS.getDescricao())) {
                        return !superClassRestriction.asSomeValuesFromRestriction()
                                .getSomeValuesFrom()
                                .as(OntClass.class)
                                .getLabel("pt")
                                .equals("Não Gorduroso");
                    }
                }
            }
        }
        return null;

    }

    private static Boolean verificaSal(OntClass ontClass) {
        for (Iterator<OntClass> superClasses = ontClass.listSuperClasses(); superClasses.hasNext(); ) {
            OntClass superClasse = superClasses.next();
            if (superClasse.isRestriction()) {
                Restriction superClassRestriction = superClasse.asRestriction();
                String label = superClassRestriction.getOnProperty().getLabel("pt");
                if (label != null) {
                    if (label.equals(Restricao.TEMSAL.getDescricao())) {
                        return !superClassRestriction.asSomeValuesFromRestriction()
                                .getSomeValuesFrom()
                                .as(OntClass.class)
                                .getLabel("pt")
                                .equals("Pouco Salgado");
                    }
                }
            }
        }
        return null;

    }

}
