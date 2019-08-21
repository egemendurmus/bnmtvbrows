package com.mobilfabrikator.goaltivibrowser.AdapterPack;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import com.mobilfabrikator.goaltivibrowser.RoomDB.Task;
import com.mobilfabrikator.goaltivibrowser.R;

/**
 * Created by TULPAR on 18.08.2017.
 */

public class GridFavorilerimAdapter extends BaseAdapter implements Filterable {

    private LayoutInflater mInflater;
    private List<Task> mTaskListesi;
    private List<Task> mTaskFilterListesi;


    public GridFavorilerimAdapter(Activity activity, List<Task> kisiler) {
        //XML'i alıp View'a çevirecek inflater'ı örnekleyelim
        mInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        //gösterilecek listeyi de alalım
        mTaskListesi = kisiler;
        mTaskFilterListesi = kisiler;
    }

    @Override
    public int getCount() {
        return mTaskFilterListesi.size();
    }

    @Override
    public Task getItem(int position) {
        //şöyle de olabilir: public Object getItem(int position)
        return mTaskFilterListesi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View satirView;

        satirView = mInflater.inflate(R.layout.grid_item, null);
        TextView textView =
                satirView.findViewById(R.id.isimsoyisim);
        ImageView imageView =
                satirView.findViewById(R.id.simge);

        Task kisi = mTaskFilterListesi.get(position);

        textView.setText(kisi.header);

        if (kisi.cover != null && !kisi.cover.isEmpty()) {

            Picasso.with(satirView.getContext())
                    .load(kisi.cover)
                    .placeholder(R.drawable.tv_icon_adapter)
                    .fit()
                    .into(imageView);
        }

        return satirView;
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mTaskFilterListesi = mTaskListesi;
                } else {
                    List<Task> filteredList = new ArrayList<>();
                    for (Task row : mTaskListesi) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.header.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    mTaskFilterListesi = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mTaskFilterListesi;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mTaskFilterListesi = (ArrayList<Task>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}