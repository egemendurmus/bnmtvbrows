package com.mobilfabrikator.goaltivibrowser.AdapterPack;

/**
 * Created by TULPAR on 18.08.2017.
 */


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

import com.mobilfabrikator.goaltivibrowser.DataPack.ChannelStreamData;
import com.mobilfabrikator.goaltivibrowser.VodORStreamActivity;
import com.mobilfabrikator.goaltivibrowser.R;

/**
 * Created by TULPAR on 18.08.2017.
 */

public class ChannelNameCategoryGridAdapter extends BaseAdapter implements Filterable {

    private LayoutInflater mInflater;
    private List<ChannelStreamData> mChannelStreamDataListesi;
    private List<ChannelStreamData> mChannelStreamDataFilterListesi;

    public ChannelNameCategoryGridAdapter(Activity activity, List<ChannelStreamData> kisiler) {
        //XML'i alıp View'a çevirecek inflater'ı örnekleyelim
        mInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        //gösterilecek listeyi de alalım
        mChannelStreamDataListesi = kisiler;
        mChannelStreamDataFilterListesi = kisiler;

    }


    @Override
    public int getCount() {
        return mChannelStreamDataFilterListesi.size();
    }

    @Override
    public ChannelStreamData getItem(int position) {
        //şöyle de olabilir: public Object getItem(int position)
        return mChannelStreamDataFilterListesi.get(position);
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

        ChannelStreamData kisi = mChannelStreamDataFilterListesi.get(position);

        try {
            textView.setText(kisi.getChannelName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (kisi.getImageURL() != null && !kisi.getImageURL().isEmpty()) {
            if (VodORStreamActivity.vodType.equals("vod")) {
                Picasso.with(satirView.getContext())
                        .load(kisi.getImageURL())
                        .placeholder(R.drawable.no_poster)
                        .fit()
                        .into(imageView);
            } else {
                Picasso.with(satirView.getContext())
                        .load(kisi.getImageURL())
                        .fit()
                        .placeholder(R.drawable.no_poster)
                        .into(imageView);
            }


        }

      /*  if (kisi.isKadinMi()) {
            imageView.setImageResource(R.drawable.kadin_simge);
        }
        else {
            imageView.setImageResource(R.drawable.adam_simge);
        }*/
        return satirView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mChannelStreamDataFilterListesi = mChannelStreamDataListesi;
                } else {
                    List<ChannelStreamData> filteredList = new ArrayList<>();
                    for (ChannelStreamData row : mChannelStreamDataListesi) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getChannelName().toLowerCase().contains(charString.toLowerCase()) ) {
                            filteredList.add(row);
                        }
                    }

                    mChannelStreamDataFilterListesi = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mChannelStreamDataFilterListesi;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mChannelStreamDataFilterListesi = (ArrayList<ChannelStreamData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}