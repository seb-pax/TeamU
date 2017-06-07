package fr.pacreau.teamu.rendezvous;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.pacreau.teamu.R;
import fr.pacreau.teamu.dao.RendezvousDao;
import fr.pacreau.teamu.model.Rendezvous;
import fr.pacreau.teamu.rendezvous.RendezvousDetailActivity;
import fr.pacreau.teamu.rendezvous.RendezvousDetailFragment;
import fr.pacreau.teamu.util.DateUtil;

/**
 * Created by spacreau on 02/06/2017.
 */

public class RendezvousItemRecyclerViewAdapter
        extends RecyclerView.Adapter<RendezvousItemRecyclerViewAdapter.ViewHolder> {

    private final List<Rendezvous> mValues;
    private Context context;

    public RendezvousItemRecyclerViewAdapter(Context p_oContext) {
        mValues = new ArrayList<Rendezvous>();
        context = p_oContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rendezvous_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        if (holder.mItem!= null) {
            holder.mDateView.setText(DateUtil.formatDate(holder.mItem.getDate()));
            holder.mAddressView.setText(holder.mItem.getAddress());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, RendezvousDetailActivity.class);
                    intent.putExtra(RendezvousDetailFragment.ARG_ITEM_ID, holder.mItem.getUid());
                    context.startActivity(intent);
                }
            });
            holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View v) {
                    v.setBackgroundColor(v.getResources().getColor(android.R.color.holo_red_dark));
                    showDialog(holder,v);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void addItem(Rendezvous p_oRendezvous) {
        if (!mValues.contains(p_oRendezvous)) {
            mValues.add(p_oRendezvous);
        }
        this.notifyDataSetChanged();
    }

    public void removeItem(Rendezvous p_oRendezvous) {
        if (mValues.contains(p_oRendezvous)) {
            mValues.remove(p_oRendezvous);
        }
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mDateView;
        public final TextView mAddressView;
        public Rendezvous mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDateView = (TextView) view.findViewById(R.id.date);
            mAddressView = (TextView) view.findViewById(R.id.address);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDateView.getText() + "'"+ mAddressView.getText() + "'";
        }
    }

    private void showDialog(final ViewHolder pholder, final View p_oView) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.popup_supp_donnees_profil_title))
                .setMessage(context.getString(R.string.popup_supp_donnees_profil_text))
                .setPositiveButton(R.string.popup_supp_donnees_profil_yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                RendezvousDao.getInstance().deleteRendezvous(pholder.mItem.getUid());
                            }
                        })
                .setNegativeButton(R.string.popup_supp_donnees_profil_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        p_oView.setBackgroundColor(p_oView.getResources().getColor(android.R.color.white));
                    }
                }).create();
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, android.R.color.holo_blue_dark));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, android.R.color.holo_blue_dark));
            }
        });
        dialog.show();

    }
}
