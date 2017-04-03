package facin.com.cardapio_virtual;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.SimpleIRIMapper;*/

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import static com.hp.hpl.jena.ontology.OntModelSpec.OWL_MEM;
import static com.hp.hpl.jena.ontology.OntModelSpec.OWL_MEM_MICRO_RULE_INF;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ProductFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private ArrayList<Product> products;
    private RecyclerView recyclerView;

    // Ontology
    private File lanchesFile;
    private File fileDir;
    private String fileName;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProductFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ProductFragment newInstance(int columnCount) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fileDir = getActivity().getApplicationContext().getFilesDir();
        fileName = "pizza.owl";
        lanchesFile = new File(fileDir, fileName);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            new FetchOntologyTask().execute((Void) null);
            //recyclerView.setAdapter(new MyFavouriteRecyclerViewAdapter(DummyContent.ITEMS, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Product product);
    }

    public class FetchOntologyTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // create the base model
                String protocol = "file:/";
                String SOURCE = protocol + fileDir + "/" + fileName;
                String NS = SOURCE + "#";
                OntModel base = ModelFactory.createOntologyModel(OWL_MEM);
                // base.read(new File(protocol + fileDir, fileName).toString());
                base.read(protocol + lanchesFile.toString());
//              base.read( SOURCE, "RDF/XML" );

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
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        /*protected OWLOntologyManager createOntologyManager() {
            OWLOntologyManager ontologyManager =
                    OWLManager.createOWLOntologyManager();
            ontologyManager.getIRIMappers().add(new AutoIRIMapper(
                    new File(getString(R.string.materialized_ontologies_file)), true));
            return ontologyManager;

        }*/

        @Override
        protected void onPostExecute(final Boolean result) {
            if (result) {
                // products = populaLista(restaurantsCursor);
                // recyclerView.setAdapter(new MyProductRecyclerViewAdapter(products, mListener));
            }
        }

         public ArrayList<Product> populaLista(Cursor cursor) {
            products = new ArrayList<>();
            // Cria produtos
            return products;
        }
    }
}
