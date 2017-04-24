package facin.com.cardapio_virtual;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static com.hp.hpl.jena.ontology.OntModelSpec.OWL_MEM;

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
    private ArrayList<Product> produtos;
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
        // criaArquivoMetodo1();
        criaArquivoMetodo2();
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    protected void criaArquivoMetodo1() {
        fileName = "lanches2.owl";
        lanchesFile = new File(fileDir, fileName);
    }

    protected void criaArquivoMetodo2() {
        try {
            fileName = "pizza.owl";
            String content = getActivity().getApplicationContext().getAssets().open("pizza.owl").toString();

            FileOutputStream outputLanches;
            outputLanches = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
            outputLanches.write(content.getBytes());
            outputLanches.close();
        } catch (IOException e) {
            e.printStackTrace();
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

        OntProperty temIngrediente;

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // Caminhos dos arquivos
                InputStream assetFile = getActivity().getApplicationContext().getAssets().open("lanches2.owl");
                String outputFilePath = fileDir + "/" + fileName;

                // create the base model
                String protocol = "file:/";
                // String SOURCE = "http://www.co-ode.org/ontologies/pizza/pizza.owl";
                String SOURCE = "http://www.semanticweb.org/priscila/ontologies/2017/3/untitled-ontology-3";
                String NS = SOURCE + "#";
                //JenaOWLModel owlModel = ProtegeOWL.createJenaOWLModel();
                OntModel ontModel = ModelFactory.createOntologyModel(OWL_MEM);
                // Read the file
                // base.read(new File(protocol + fileDir, fileName).toString());
                // InputStream inputLanches = new FileInputStream(fileDir + "/" + fileName);
                // InputStream inputLanches = new FileInputStream(getActivity().getApplicationContext().getAssets().open("pizza.owl"));
                // Carrega o arquivo dos assets para a pasta de arquivos do aplicativo
                carregaArquivoInicial(assetFile, outputFilePath);
                // Lê a ontologia
                ontModel.read(new FileInputStream(outputFilePath), "OWL");
                // Teste: criando uma propriedade
                temIngrediente = ontModel.createOntProperty(NS + "temIngrediente");
                // create the reasoning model using the base
                // OntModel inf = ModelFactory.createOntologyModel(OWL_MEM_MICRO_RULE_INF, ontModel);
                // Transforma as OntClasses em Products e popula o vetor (Array List) com produtos
                populaListaProdutos(pegaClassesAPartirDeIndividuos(ontModel.listIndividuals().toSet()));
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        // protected boolean saveFile(File file)

        @Override
        protected void onPostExecute(final Boolean result) {
            if (result) {
                recyclerView.setAdapter(new MyProductRecyclerViewAdapter(produtos, mListener));
            }
        }

        private void carregaArquivoInicial(InputStream inputStream, String outputPath) throws IOException {
            BufferedReader br = null;

            BufferedWriter bw = null;
            try {
                br = new BufferedReader(new InputStreamReader(inputStream));
                bw = new BufferedWriter(new FileWriter(outputPath));
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    bw.write(inputLine);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (br != null)
                    br.close();
                if (bw != null)
                    bw.close();
            }
        }

        private HashMap<OntClass, Integer> pegaClassesAPartirDeIndividuos(Set<Individual> individuals) {
            HashMap<OntClass, Integer> ontClasses = new HashMap<>();
            Boolean contemClasse = false;
            for (Individual i : individuals) {
                for (Map.Entry<OntClass, Integer> kv : ontClasses.entrySet()) {
                    if (kv.getKey().equals(i.getOntClass())) {
                        contemClasse = true;
                        kv.setValue(kv.getValue() + 1);
                    }
                    if (contemClasse)
                        break;
                }
                if (!contemClasse)
                    ontClasses.put(i.getOntClass(), 1);

            }
            return ontClasses;
        }

        private void populaListaProdutos(HashMap<OntClass, Integer> ontClasses) {
            produtos = new ArrayList<>();
            for (Map.Entry<OntClass, Integer> kv : ontClasses.entrySet()) {
                produtos.add(transformaOntClassEmProduto(kv.getKey(), kv.getValue()));
            }
        }

        private Product transformaOntClassEmProduto(OntClass ontClass, int quantidade) {
            return new Product(
                    ontClass.getLabel("pt"),
                    3.50,
                    populaIngredientes(ontClass),
                    quantidade
            );
        }


        private ArrayList<String> populaIngredientes(OntClass ontClass) {
            ArrayList<String> ingredientes = new ArrayList<>();
//            StmtIterator nodeIterator = ontClass.listProperties(temIngrediente);
            //Set<RDFNode> nodeSet = nodeIterator.toSet();
//            if (nodeIterator != null) {
//                // RDFList nodoList = nodeList.as(RDFList.class);
//                while (nodeIterator.hasNext()) {
//                    ingredientes.add(nodeIterator.nextStatement().toString());
//                }
//            }
            for (Iterator<OntClass> superClasses = ontClass.listSuperClasses(); superClasses.hasNext(); ) {
                String ingrediente = displayType(superClasses.next());
                if (ingrediente != null) {
                    ingredientes.add(ingrediente);
                    Log.d("Ingr. log", ingrediente);
                }
            }
            return ingredientes;
        }

        @Nullable
        private String displayType(OntClass superClass) {
            if (superClass.isRestriction()) {
                return displayRestriction(superClass.asRestriction());
            }
            return null;
        }

        @Nullable
        private String displayRestriction(Restriction restriction) {
            if (restriction.isSomeValuesFromRestriction()) {
                if (restriction.getOnProperty().equals(temIngrediente)) {
//                    Log.d("Property", restriction.getOnProperty().toString());
//                    Log.d("Constraint", restriction.asSomeValuesFromRestriction().getSomeValuesFrom().toString());
                    return restriction.asSomeValuesFromRestriction()
                            .getSomeValuesFrom()
                            .as(OntClass.class)
                            .getLabel("pt");
                }
            } else if (restriction.isAllValuesFromRestriction()) {
                return displayRestriction("all",
                        restriction.getOnProperty(),
                        restriction.asAllValuesFromRestriction().getAllValuesFrom());
            }
            return null;
        }

        @Nullable
        private String displayRestriction(String qualifier, OntProperty ontProperty, Resource constraint) {
            return null;
        }

//        protected Object renderConstraint( Resource constraint ) {
//            if (constraint.canAs( UnionClass.class )) {
//                UnionClass uc = constraint.as( UnionClass.class );
//                // this would be so much easier in ruby ...
//                String r = "union{ ";
//                for (Iterator<? extends OntClass> i = uc.listOperands(); i.hasNext(); ) {
//                    r = r + " " + renderURI( i.next() );
//                }
//                return r + "}";
//            }
//            else {
//                return renderURI( constraint );
//            }
//        }
//
//        protected Object renderURI( Resource onP ) {
//            String qName = onP.getModel().qnameFor( onP.getURI() );
//            return qName == null ? onP.getLocalName() : qName;
//        }
    }
}
