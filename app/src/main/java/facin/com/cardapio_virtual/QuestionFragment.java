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

import facin.com.cardapio_virtual.data.DatabaseContract;
import facin.com.cardapio_virtual.dummy.DummyContent;
import facin.com.cardapio_virtual.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            new FetchQuestionTask().execute((Void) null);
            // recyclerView.setAdapter(new MyQuestionRecyclerViewAdapter(DummyContent.ITEMS, mListener));
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
            recyclerView.setAdapter(new MyQuestionRecyclerViewAdapter(perguntas, mListener));
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
            p1.put("resposta", "Para adicionar um restaurante a sua lista de favoritos, primeiramente acesse a aba de Restaurantes na tela inicial da aplicação." +
                    " Em seguida, selecione o restaurante que deseja favoritar. Note que a tela das informações adicionais do restaurante abrirá." +
                    " Navegue nesta tela em busca de um botão de \"Favoritar\" e clique nele. Desta forma, o restaurante será favoritado e aparecerá" +
                    " na aba de Favoritos na tela inicial.");
            perguntas.add(p1);

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
