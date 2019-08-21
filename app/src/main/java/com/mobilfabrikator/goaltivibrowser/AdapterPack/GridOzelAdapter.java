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

import java.util.ArrayList;
import java.util.List;

import com.mobilfabrikator.goaltivibrowser.DataPack.ChannelCategoryData;
import com.mobilfabrikator.goaltivibrowser.R;

/**
 * Created by TULPAR on 18.08.2017.
 */

public class GridOzelAdapter extends BaseAdapter implements Filterable {

    private LayoutInflater mInflater;
    private List<ChannelCategoryData> mChannelCategoryDataListesi;
    private List<ChannelCategoryData> mChannelCategoryDataFilterListesi;


    public GridOzelAdapter(Activity activity, List<ChannelCategoryData> kisiler) {
        //XML'i alıp View'a çevirecek inflater'ı örnekleyelim
        mInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        //gösterilecek listeyi de alalım
        mChannelCategoryDataListesi = kisiler;
        mChannelCategoryDataFilterListesi=kisiler;
    }

    @Override
    public int getCount() {
        return mChannelCategoryDataFilterListesi.size();
    }

    @Override
    public ChannelCategoryData getItem(int position) {
        //şöyle de olabilir: public Object getItem(int position)
        return mChannelCategoryDataFilterListesi.get(position);
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

        ChannelCategoryData kisi = mChannelCategoryDataFilterListesi.get(position);


        try {
            textView.setText(kisi.getCategoryName());
        } catch (Exception e) {
            e.printStackTrace();
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
                    mChannelCategoryDataFilterListesi = mChannelCategoryDataListesi;
                } else {
                    List<ChannelCategoryData> filteredList = new ArrayList<>();
                    for (ChannelCategoryData row : mChannelCategoryDataListesi) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCategoryName().toLowerCase().contains(charString.toLowerCase()) ) {
                            filteredList.add(row);
                        }
                    }

                    mChannelCategoryDataFilterListesi = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mChannelCategoryDataFilterListesi;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mChannelCategoryDataFilterListesi = (ArrayList<ChannelCategoryData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}