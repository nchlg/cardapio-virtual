package facin.com.cardapio_virtual.owlModels;

//import com.hp.hpl.jena.ontology.OntClass;
//import com.hp.hpl.jena.ontology.OntModel;
//import com.hp.hpl.jena.query.Query;
//import com.hp.hpl.jena.query.QueryExecution;
//import com.hp.hpl.jena.query.QueryExecutionFactory;
//import com.hp.hpl.jena.query.QueryFactory;
//import com.hp.hpl.jena.query.ResultSet;
//import com.hp.hpl.jena.rdf.model.ModelFactory;
//import com.hp.hpl.jena.rdf.model.Property;


import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

import java.io.File;
import java.util.Iterator;

import static com.hp.hpl.jena.ontology.OntModelSpec.OWL_MEM;
import static com.hp.hpl.jena.ontology.OntModelSpec.OWL_MEM_MICRO_RULE_INF;

/**
 * Created by Priscila on 01/04/2017.
 */

public class OntologyInitializer {

    public static void setUp(File file, File fileDir) {
        // create the base model
        String fileName = "pizza.owl";
        String protocol = "file:/";
        String SOURCE = protocol + fileDir + "/" + fileName;
        String NS = SOURCE + "#";
        OntModel base = ModelFactory.createOntologyModel(OWL_MEM);
        // base.read(new File(protocol + fileDir, fileName).toString());
        base.read(protocol + file.toString());
//        base.read( SOURCE, "RDF/XML" );

        // create the reasoning model using the base
        OntModel inf = ModelFactory.createOntologyModel( OWL_MEM_MICRO_RULE_INF, base );

        // create a dummy paper for this example
        OntClass paper = base.getOntClass( NS + "American" );
        Individual p1 = base.createIndividual( NS + "american1", paper );

        // list the asserted types
        for (Iterator<Resource> i = p1.listRDFTypes(true); i.hasNext(); ) {
            System.out.println( p1.getURI() + " is asserted in class " + i.next() );
        }

        // list the inferred types
        p1 = inf.getIndividual( NS + "paper1" );
        for (Iterator<Resource> i = p1.listRDFTypes(true); i.hasNext(); ) {
            System.out.println( p1.getURI() + " is inferred to be in class " + i.next() );
        }
    }

}
