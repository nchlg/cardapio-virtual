package facin.com.cardapio_virtual;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import facin.com.cardapio_virtual.ProductFragment.OnListFragmentInteractionListener;
import facin.com.cardapio_virtual.auxiliares.Restricao;

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder> {

    private final List<Product> mValues;
    private final OnListFragmentInteractionListener mListener;

    public ProductRecyclerViewAdapter(List<Product> items, OnListFragmentInteractionListener listener) {
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
            holder.mContentView.setText(mValues.get(position).getPrecoAsString());
            if (holder.mItem.getMapaRestricoes().get(Restricao.CONTAVEL)) {
                if (mValues.get(position).getQuantidade() > 1) {
                    holder.mQtdView.setText(
                            String.valueOf(mValues.get(position).getQuantidade()) +
                                    " " +
                                    holder.mView.getContext().getResources().getString(R.string.post_quantity_many).toLowerCase());
                } else {
                    holder.mQtdView.setText(
                            String.valueOf(mValues.get(position).getQuantidade()) +
                                    " " +
                                    holder.mView.getContext().getResources().getString(R.string.post_quantity_one).toLowerCase());
                }
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
            mIdView = (TextView) view.findViewById(R.id.restaurant_id);
            mContentView = (TextView) view.findViewById(R.id.preco);
            mQtdView = (TextView) view.findViewById((R.id.quantidade));

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
