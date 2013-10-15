package com.example.draft;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PlayerAdapter extends ArrayAdapter<Player>{
    private Context context;
    private int layoutResourceId;
    private List<Player> values = null;

    public PlayerAdapter(Context context, int layoutResourceId, List<Player> values) {
        super(context, layoutResourceId, values);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PlayerHolder holder = null;

        if(row == null){
            LayoutInflater inflater =((Activity)context).getLayoutInflater();

            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new PlayerHolder();
//            holder.img = (ImageView) row.findViewById(R.id.imageView);
            holder.pos = (TextView) row.findViewById(R.id.position);
            holder.label = (TextView) row.findViewById(R.id.label);

            row.setTag(holder);
        }
        else{
            holder = (PlayerHolder) row.getTag();
        }

        Player player = values.get(position);
        holder.label.setText(player.name);
//        holder.img.setImageResource(player.image);
        holder.pos.setText(Integer.toString(position+1));

        return row;
    }

    static class PlayerHolder{
//        ImageView img;
        TextView pos, label;
    }
}