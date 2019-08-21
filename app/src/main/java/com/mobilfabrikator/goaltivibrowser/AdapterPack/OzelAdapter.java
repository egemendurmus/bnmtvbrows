package com.mobilfabrikator.goaltivibrowser.AdapterPack;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.mobilfabrikator.goaltivibrowser.DataPack.ChannelCategoryData;
import com.mobilfabrikator.goaltivibrowser.R;

/**
 * Created by TULPAR on 18.08.2017.
 */

public class OzelAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<ChannelCategoryData> mChannelCategoryDataListesi;

    public OzelAdapter(Activity activity, List<ChannelCategoryData> kisiler) {
        //XML'i alıp View'a çevirecek inflater'ı örnekleyelim
        mInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        //gösterilecek listeyi de alalım
        mChannelCategoryDataListesi = kisiler;
    }

    @Override
    public int getCount() {
        return mChannelCategoryDataListesi.size();
    }

    @Override
    public ChannelCategoryData getItem(int position) {
        //şöyle de olabilir: public Object getItem(int position)
        return mChannelCategoryDataListesi.get(position);
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
                (TextView) satirView.findViewById(R.id.isimsoyisim);
        ImageView imageView =
                (ImageView) satirView.findViewById(R.id.simge);

        ChannelCategoryData kisi = mChannelCategoryDataListesi.get(position);

        textView.setText(kisi.getCategoryName());


      /*  if (kisi.isKadinMi()) {
            imageView.setImageResource(R.drawable.kadin_simge);
        }
        else {
            imageView.setImageResource(R.drawable.adam_simge);
        }*/
        return satirView;
    }

}