package facin.com.cardapio_virtual;

import android.app.ProgressDialog;
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

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
    private ProgressDialog progressDialog;

    // Ontology
    private File lanchesFile;
    private File fileDir;
    private String fileName;
    private List<Individual> individuos;

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
        individuos = null;
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
            fileName = "lanches2.owl";
            String content = getActivity().getApplicationContext().getAssets().open("lanches2.owl").toString();

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
            progressDialog = ProgressDialog.show(getActivity(), getResources().getText(R.string.progress_dialog_product_title),
                    getResources().getText(R.string.progress_dialog_product_message), true, false);
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
        OntProperty preco;
        OntProperty contavel;
        Map<OntClass, Integer> relacaoClasseIndividuo = new HashMap<>();

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // Caminhos dos arquivos
                InputStream assetFile = getActivity().getApplicationContext().getAssets().open("lanches2.owl");
                String outputFilePath = fileDir + "/" + fileName;
                String protocol = "file:/";
                String SOURCE = "http://www.semanticweb.org/priscila/ontologies/2017/3/untitled-ontology-3";
                String NS = SOURCE + "#";
                OntModel ontModel = ModelFactory.createOntologyModel(OWL_MEM);

                // Carrega o arquivo dos assets para a pasta de arquivos do aplicativo
                carregaArquivoInicial(assetFile, outputFilePath);

                // Lê a ontologia
                ontModel.read(new FileInputStream(outputFilePath), "OWL");

                // Cria propriedades
                temIngrediente = ontModel.createOntProperty(NS + "temIngrediente");
                contavel = ontModel.createOntProperty(NS + "contavel");
                preco = ontModel.createOntProperty(NS + "preco");

                // Pega indivíduos
                individuos = ontModel.listIndividuals().toList();
                relacaoClasseIndividuo = pegaClassesAPartirDeIndividuos(individuos);

                // Transforma as OntClasses em Products e popula a lista com produtos
                //HashMap<OntClass, Integer> classesContaveis = pegaClassesAPartirDeIndividuos(ontModel.listIndividuals().toSet());
                //populaListaProdutos(classesContaveis);
                //ArrayList<OntClass> classesNaoContaveis = pegaClassesPorAtributo(ontModel.listClasses().toSet(), contavel, false);
                // populaListaProdutos(classesNaoContaveis);

                populaListaProdutos(pegaFilhosDaRaiz(ontModel.getOntClass(((MenuActivity) getActivity()).getIntentOntClassURI())));
                Log.d("URI recebida", ((MenuActivity) getActivity()).getIntentOntClassURI() != null ? ((MenuActivity) getActivity()).getIntentOntClassURI() : "null");
                // populaListaProdutos(pegaFilhosDaRaiz(ontModel.getOntClass(NS + "Produto")));
                Log.d("URI padrão", NS + "Produto");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        private List<OntClass> pegaFilhosDaRaiz(OntClass raiz) {
            return raiz.listSubClasses().toList();
        }

        // protected boolean saveFile(File file)

        @Override
        protected void onPostExecute(final Boolean result) {
            if (result) {
                Collections.sort(produtos, new Comparator<Product>() {
                    @Override
                    public int compare(Product p1, Product p2) {
                        // Se p1 tem filhos e p2 NÃO tem filhos:
                        if (!p1.getOntClass().listSubClasses().toList().isEmpty()
                                && p2.getOntClass().listSubClasses().toList().isEmpty()) {
                            return -1;
                        }
                        // Se p1 NÃO tem filhos e p2 tem filhos:
                        else if (p1.getOntClass().listSubClasses().toList().isEmpty()
                                && !p2.getOntClass().listSubClasses().toList().isEmpty()) {
                            return 1;
                        }
                        // Se os dois estão na mesma categoria (ambos têm filhos ou ambos não têm filhos):
                        else {
                            String nomeP1 = p1.getNome();
                            String nomeP2 = p2.getNome();
                            Log.d("Nomes P1 P2", (p1.getNome() != null ? p1.getNome() : "-")
                                    + (p2.getNome() != null ? p2.getNome() : "-"));
                            return nomeP1.compareTo(nomeP2);
                        }
                    }
                });
                progressDialog.dismiss();
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

        private ArrayList<OntClass> pegaClassesPorAtributo(Set<OntClass> ontClasses, OntProperty property, boolean filtro) {
            ArrayList<OntClass> classesQuePossuemTalAtributo = new ArrayList<>();

            for (OntClass oc : ontClasses) {
                for (Iterator<OntClass> superClasses = oc.listSuperClasses(); superClasses.hasNext(); ) {
                    OntClass superClasse = superClasses.next();
                    Log.d("SC:", superClasse.getLabel("pt") != null ? superClasse.getLabel("pt") : "-");
                    if (superClasse.isRestriction()) {
                        Restriction restriction = superClasse.asRestriction();
                        if (restriction.isHasValueRestriction()) {
                            if (restriction.getOnProperty().equals(property)) {
                                //if (restriction.asHasValueRestriction().getHasValue())
                                Log.d("HasValue", restriction.asHasValueRestriction().getHasValue().toString() != null ?
                                        restriction.asHasValueRestriction().getHasValue().toString() : "-");
                                classesQuePossuemTalAtributo.add(oc);
                                break;
                            }
                        }
                    }
                }
            }
            return classesQuePossuemTalAtributo;
        }

        private Map<OntClass, Integer> pegaClassesAPartirDeIndividuos(List<Individual> individuals) {
            Map<OntClass, Integer> mapa = new HashMap<>();
            Boolean contemClasse = false;
            for (Individual i : individuals) {
                for (Map.Entry<OntClass, Integer> kv : mapa.entrySet()) {
                    if (kv.getKey().equals(i.getOntClass())) {
                        contemClasse = true;
                        kv.setValue(kv.getValue() + 1);
                    }
                    if (contemClasse)
                        break;
                }
                if (!contemClasse)
                    mapa.put(i.getOntClass(), 1);

            }
            return mapa;
        }

//        private void populaListaProdutos(Map<OntClass, Integer> ontClasses) {
//            produtos = new ArrayList<>();
//            for (Map.Entry<OntClass, Integer> kv : ontClasses.entrySet()) {
//                produtos.add(transformaOntClassEmProdutoContavel(kv.getKey(), kv.getValue()));
//            }
//        }

        /*private void populaListaProdutos(List<OntClass> ontClasses) {
            produtos = new ArrayList<>();
            for (OntClass oc : ontClasses) {
                produtos.add(transformaOntClassEmProdutoNaoContavel(oc));
            }
        }*/

        private boolean isClasseContavel(OntClass ontClass) {
            for (Iterator<OntClass> superClasses = ontClass.listSuperClasses(); superClasses.hasNext(); ) {
                OntClass superClasse = superClasses.next();
                if (superClasse.isRestriction()) {
                    Restriction restriction = superClasse.asRestriction();
                    if (restriction.isHasValueRestriction()) {
                        if (restriction.getOnProperty().equals(contavel)) {
                            if (restriction.asHasValueRestriction().getHasValue().as(Literal.class).getBoolean()) {
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }

        private boolean temRestricao(OntClass ontClass, OntProperty restricao) {
            for (Iterator<OntClass> superClasses = ontClass.listSuperClasses(); superClasses.hasNext(); ) {
                OntClass superClasse = superClasses.next();
                if (superClasse.isRestriction()) {
                    Restriction restriction = superClasse.asRestriction();
                    if (restriction.isHasValueRestriction()) {
                        if (restriction.getOnProperty().equals(restricao)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        private Double temRestricaoPreco(OntClass ontClass) {
            for (Iterator<OntClass> superClasses = ontClass.listSuperClasses(); superClasses.hasNext(); ) {
                OntClass superClasse = superClasses.next();
                if (superClasse.isRestriction()) {
                    Restriction restriction = superClasse.asRestriction();
                    if (restriction.isHasValueRestriction()) {
                        if (restriction.getOnProperty().equals(preco)) {
                            return restriction.asHasValueRestriction().getHasValue().as(Literal.class).getDouble();
                        }
                    }
                }
            }
            return null;
        }

        private boolean temFilhosComQuantidade(OntClass ontClass) {
            Deque<OntClass> filinha = new ArrayDeque<>();
            filinha.add(ontClass);
            OntClass classeAtual;
            while (!filinha.isEmpty()) {
                classeAtual = filinha.pop();
                if (classeAtual.listSubClasses().toList().isEmpty()) {
                    if (individuos != null) {
                        for (Individual i : individuos) {
                            if (i.getOntClass().equals(classeAtual)) {
                                return true;
                            }
                        }
                    }
                } else {
                    filinha.addAll(classeAtual.listSubClasses().toList());
                }
            }
            return false;
        }

        private boolean temFilhosIncontaveis(OntClass ontClass) {
            Deque<OntClass> filinha = new ArrayDeque<>();
            filinha.add(ontClass);
            OntClass classeAtual;
            while (!filinha.isEmpty()) {
                classeAtual = filinha.pop();
                if (temRestricao(classeAtual, contavel) && !isClasseContavel(classeAtual)) {
                    return true;
                } else {
                    if (!classeAtual.listSubClasses().toList().isEmpty()) {
                        filinha.addAll(classeAtual.listSubClasses().toList());
                    }
                }
            }
            return false;
        }

        private OntClass pegaSuperClasse(OntClass ontClass) {
            for (Iterator<OntClass> superClasses = ontClass.listSuperClasses(); superClasses.hasNext(); ) {
                OntClass superClasse = superClasses.next();
                if (!superClasse.isRestriction() && superClasse.getLabel("pt") != null) {
                    return superClasse;
                }
            }
            return null;
        }

        private void populaListaProdutos(List<OntClass> ontClasses) {
            produtos = new ArrayList<>();
            for (OntClass oc : ontClasses) {
                OntClass classeAtual = oc;
                boolean isContavel = false;
                boolean temRestricaoContavel = temRestricao(classeAtual, contavel);
                if (temRestricaoContavel)
                    isContavel = isClasseContavel(classeAtual);

                while (classeAtual != null && !isContavel && !temRestricaoContavel) {
                    classeAtual = pegaSuperClasse(classeAtual);
                    if (classeAtual != null) {
                        temRestricaoContavel = temRestricao(classeAtual, contavel);
                        if (temRestricaoContavel)
                            isContavel = isClasseContavel(classeAtual);
                    }
                }
                if ((temRestricaoContavel && !isContavel) || temFilhosIncontaveis(oc) || temFilhosComQuantidade(oc)) {
                    if (!oc.listSubClasses().toList().isEmpty() && isContavel) {
                        if (individuos != null) {
                            produtos.add(transformaOntClassEmProdutoIntermediario(oc));
                        }
                    } else if (oc.listSubClasses().toList().isEmpty() && isContavel) {
                        if (individuos != null) {
                            for (Map.Entry<OntClass, Integer> kv : relacaoClasseIndividuo.entrySet()) {
                                produtos.add(transformaOntClassEmProdutoContavel(oc, kv.getValue()));
                            }
                        }
                    } else if (!oc.listSubClasses().toList().isEmpty() && !isContavel) {
                        produtos.add(transformaOntClassEmProdutoIntermediario(oc));
                    } else if (oc.listSubClasses().toList().isEmpty() && !isContavel) {
                        produtos.add(transformaOntClassEmProdutoNaoContavel(oc));
                    }
                }
            }
        }

        private Product transformaOntClassEmProdutoContavel(OntClass ontClass, int quantidade) {
            return new Product(
                    ontClass.getLabel("pt"),
                    getPreco(ontClass),
                    populaIngredientes(ontClass),
                    quantidade,
                    ontClass
            );
        }

        private Product transformaOntClassEmProdutoNaoContavel(OntClass ontClass) {
            return new Product(
                    ontClass.getLabel("pt"),
                    getPreco(ontClass),
                    populaIngredientes(ontClass),
                    ontClass
            );
        }

        private Product transformaOntClassEmProdutoIntermediario(OntClass ontClass) {
            return new Product(
                    ontClass.getLabel("pt"),
                    ontClass
            );
        }

        private double getPreco(OntClass ontClass) {
            OntClass classeAtual = ontClass;
            Double restricaoPreco = temRestricaoPreco(classeAtual);
            if (restricaoPreco != null)
                return restricaoPreco;
            while (restricaoPreco == null && classeAtual != null) {
                classeAtual = classeAtual.getSuperClass();
                if (classeAtual != null) {
                    restricaoPreco = temRestricaoPreco(classeAtual);
                }
            }

            return restricaoPreco != null ? restricaoPreco : 0.0;
        }

        private ArrayList<String> populaIngredientes(OntClass ontClass) {
            ArrayList<String> ingredientes = new ArrayList<>();

            for (Iterator<OntClass> superClasses = ontClass.listSuperClasses(); superClasses.hasNext(); ) {
                String ingrediente = displayType(superClasses.next(), temIngrediente);
                if (ingrediente != null) {
                    ingredientes.add(ingrediente);
                }
            }
            return ingredientes;
        }

        @Nullable
        private String displayType(OntClass superClass, OntProperty property) {
            if (superClass.isRestriction()) {
                return displayRestriction(superClass.asRestriction(), property);
            }
            return null;
        }

        @Nullable
        private String displayRestriction(Restriction restriction, OntProperty property) {
            if (restriction.isSomeValuesFromRestriction()) {
                if (restriction.getOnProperty().equals(property)) {
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
    }
}
