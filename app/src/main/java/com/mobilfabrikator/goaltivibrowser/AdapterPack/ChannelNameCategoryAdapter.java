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
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilfabrikator.goaltivibrowser.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import com.mobilfabrikator.goaltivibrowser.DataPack.ChannelStreamData;
import com.mobilfabrikator.goaltivibrowser.VodORStreamActivity;

/**
 * Created by TULPAR on 18.08.2017.
 */

public class ChannelNameCategoryAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<ChannelStreamData> mChannelStreamDataListesi;

    public ChannelNameCategoryAdapter(Activity activity, List<ChannelStreamData> kisiler) {
        //XML'i alıp View'a çevirecek inflater'ı örnekleyelim
        mInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        //gösterilecek listeyi de alalım
        mChannelStreamDataListesi = kisiler;
    }

    @Override
    public int getCount() {
        return mChannelStreamDataListesi.size();
    }

    @Override
    public ChannelStreamData getItem(int position) {
        //şöyle de olabilir: public Object getItem(int position)
        return mChannelStreamDataListesi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View satirView;

        satirView = mInflater.inflate(R.layout.list_item, null);
        TextView textView =
                satirView.findViewById(R.id.isimsoyisim);
        ImageView imageView =
                satirView.findViewById(R.id.simge);

        ChannelStreamData kisi = mChannelStreamDataListesi.get(position);

        textView.setText(kisi.getChannelName());
        if (kisi.getImageURL() != null && !kisi.getImageURL().isEmpty()) {
            if (VodORStreamActivity.vodType.equals("vod")) {
                Picasso.with(satirView.getContext())
                        .load(kisi.getImageURL())
                        .fit()
                        .placeholder(R.drawable.no_poster)
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
}