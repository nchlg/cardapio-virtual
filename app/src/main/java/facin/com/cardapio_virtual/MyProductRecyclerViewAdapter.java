package facin.com.cardapio_virtual;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import facin.com.cardapio_virtual.ProductFragment.OnListFragmentInteractionListener;
import facin.com.cardapio_virtual.auxiliares.Restricao;

import java.util.ArrayList;
import java.util.List;

public class MyProductRecyclerViewAdapter extends RecyclerView.Adapter<MyProductRecyclerViewAdapter.ViewHolder> {

    private final List<Product> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyProductRecyclerViewAdapter(List<Product> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        if (!holder.mItem.getOntClass().listSubClasses().toList().isEmpty()) {
            holder.mIdView.setText(
                    holder.mView.getContext().getResources().getString(R.string.category_prefix) +
                            " " +
                            holder.mItem.getNome());
            holder.mContentView.setVisibility(View.GONE);
            holder.mQtdView.setVisibility(View.GONE);
        } else {
            holder.mIdView.setText(holder.mItem.getNome());
            holder.mContentView.setText(String.valueOf(mValues.get(position).getPreco()));
            if (holder.mItem.getMapaRestricoes().get(Restricao.CONTAVEL)) {
                holder.mQtdView.setText(
                        holder.mView.getContext().getResources().getString(R.string.pre_quantity) +
                                ": " +
                                String.valueOf(mValues.get(position).getQuantidade()));
            } else {
                holder.mQtdView.setVisibility(View.GONE);
            }
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final TextView mQtdView;
        public Product mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.preco);
            mQtdView = (TextView) view.findViewById((R.id.quantidade));

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
