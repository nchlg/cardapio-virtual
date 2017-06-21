package facin.com.cardapio_virtual;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import facin.com.cardapio_virtual.auxiliares.SimpleDividerItemDecoration;
import facin.com.cardapio_virtual.data.DatabaseContract;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class QuestionFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private static boolean perguntasInicializadas = false;
    private RecyclerView recyclerView;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public QuestionFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static QuestionFragment newInstance(int columnCount) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            new FetchQuestionTask().execute((Void) null);
            // recyclerView.setAdapter(new QuestionRecyclerViewAdapter(DummyContent.ITEMS, mListener));
        }
        return view;
    }

    public class FetchQuestionTask extends AsyncTask<Void, Void, Boolean> {

        Cursor perguntasCursor;
        List<Question> perguntas;

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                if (!perguntasInicializadas) {
                    inserePerguntas();
                    perguntasInicializadas = true;
                }

                perguntasCursor = getActivity().getContentResolver().query(
                        DatabaseContract.DuvidasEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                );
                if (perguntasCursor != null) {
                    return true;
                }
            } catch (UnsupportedOperationException e) {
                e.printStackTrace();
                return false;
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            if (result) {
                populaLista(perguntasCursor);
            }
            Collections.sort(perguntas, new Comparator<Question>() {
                @Override
                public int compare(Question q1, Question q2) {
                    return q1.getQuestion().compareTo(q2.getQuestion());
                }
            });
            recyclerView.setAdapter(new QuestionRecyclerViewAdapter(perguntas, mListener));
        }

        protected void populaLista(Cursor cursor) {
            perguntas = new ArrayList<>();
            DatabaseUtils.dumpCursor(cursor);
            /* Cria restaurantes */
            while(cursor.moveToNext()) {
                Question pergunta = new Question(
                        cursor.getString(1),
                        cursor.getString(2)
                );
                perguntas.add(pergunta);
            }
        }

        protected void inserePerguntas() {
            List<ContentValues> perguntas = new ArrayList<>();

            ContentValues p1 = new ContentValues();
            p1.put("_id", (byte[]) null);
            p1.put("pergunta", "Como adiciono um restaurante aos meus favoritos?");
            p1.put("resposta", "\t\tPara adicionar um restaurante a sua lista de favoritos, primeiramente acesse a aba de Restaurantes na tela inicial da aplicação." +
                    "\n\t\tEm seguida, selecione o restaurante que deseja favoritar. Note que a tela das informações adicionais do restaurante abrirá." +
                    "\n\t\tNavegue nesta tela em busca de um botão de \"Favoritar\" e clique nele.\n\t\tDesta forma, o restaurante será favoritado e aparecerá" +
                    " na aba de Favoritos na tela inicial.");
            perguntas.add(p1);

            ContentValues p2 = new ContentValues();
            p2.put("_id", (byte[]) null);
            p2.put("pergunta", "Por que alguns produtos apresentam uma \"quantidade indisponível\"?");
            p2.put("resposta", "\t\tAlguns produtos estão sempre disponíveis nos bares. Por este motivo, suas quantidades não são contabilizadas.");
            perguntas.add(p2);

            ContentValues p3 = new ContentValues();
            p3.put("_id", (byte[]) null);
            p3.put("pergunta", "Como acesso o cardápio de um restaurante?");
            p3.put("resposta", "\t\tPara acessar o cardápio de um restaurante, acesse a página de informações adicionais do restaurante desejado." +
                    "\n\t\tApós isso, busque pelo botão \"Cardápio\" e selecione-o. Desta forma, você será direcionado ao cardápio na aplicação.");
            perguntas.add(p3);

            ContentValues p4 = new ContentValues();
            p4.put("_id", (byte[]) null);
            p4.put("pergunta", "Como filtro o cardápio por produtos vegetarianos? Quais são as opções de filtragem disponíveis?");
            p4.put("resposta", "\t\tPara filtrar qualquer produto, vá ao cardápio de um restaurante de sua escolha e clique na opção \"Filtrar\" no menu." +
                    "\n\t\tEm seguida, escolha um ou mais itens da lista e selecione a opção \"Ok\"." +
                    "\n\t\tAs opções de filtragem são: produtos sem glúten, sem lactose, com pouca gordura, com pouco sal e vegetarianos.");
            perguntas.add(p4);

            ContentValues p5 = new ContentValues();
            p5.put("_id", (byte[]) null);
            p5.put("pergunta", "O que significa ordenar o cardápio por \"mais acessados\"?");
            p5.put("resposta", "\t\tCada vez que você acessa um produto do cardápio, o número de acessos a este produto é incrementado." +
                    "\n\t\tOrdenando o cardápio por acessos, os produtos mais acessados por você serão mostrados primeiro." +
                    "\n\t\tDesta forma, você não precisará procurá-los por toda a lista de produtos e o acesso será mais rápido e prático.");
            perguntas.add(p5);

            ContentValues p6 = new ContentValues();
            p6.put("_id", (byte[]) null);
            p6.put("pergunta", "Como procuro um restaurante através do sistema de buscas?");
            p6.put("resposta", "\t\tAlém de buscar restaurantes na lista de restaurantes, você também pode buscá-los através do sistema de buscas da aplicação." +
                    "\n\t\tPara isso, busque pelo botão \"Buscar Restaurantes\" na tela inicial da aplicação. Este botão está representado por uma lupa na interface gráfica." +
                    "\n\t\tAo selecioná-lo, você pode inserir o nome ou o endereço do local desejado." +
                            "Caso tenha digitado sua busca, selecione o botão de avançar para ver o resultado de sua busca. Em seguida, basta selecionar o restaurante desejado.");
            perguntas.add(p6);

            try {
                Cursor perguntasCursor = getActivity().getContentResolver().query(
                        DatabaseContract.DuvidasEntry.CONTENT_URI,
                        new String[]{DatabaseContract.DuvidasEntry.COLUMN_PERGUNTA},
                        null,
                        null,
                        null
                );

                List<String> perguntasAdvindasDoCursor = new ArrayList<>();

                if (perguntasCursor != null) {
                    while(perguntasCursor.moveToNext()) {
                        perguntasAdvindasDoCursor.add(perguntasCursor.getString(0));
                        Log.d("3", perguntasAdvindasDoCursor.toString());
                    }
                    for (ContentValues cv : perguntas) {
                        if (!perguntasAdvindasDoCursor.contains(cv.getAsString("pergunta"))) {
                            getActivity().getContentResolver().insert(DatabaseContract.DuvidasEntry.CONTENT_URI, cv);
                        } else {
                            ContentValues respostaAtualizada = new ContentValues();
                            respostaAtualizada.put("resposta", cv.getAsString("resposta"));
                            getActivity().getContentResolver().update(
                                    DatabaseContract.DuvidasEntry.CONTENT_URI,
                                    respostaAtualizada,
                                    DatabaseContract.DuvidasEntry.COLUMN_PERGUNTA + "= ?",
                                    new String[]{cv.getAsString("pergunta")}
                            );
                        }
                    }
                    perguntasCursor.close();
                }
            } catch (UnsupportedOperationException e) {
                e.printStackTrace();
            }
        }
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
        void onListFragmentInteraction(Question item);
    }
}
