package com.example.musicapp;

import android.database.Cursor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SongAdapter  extends BaseAdapter {

    private Cursor data;

    public SongAdapter(Cursor data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.getCount();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

       view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout,viewGroup,false);
        TextView nameText = view.findViewById(R.id.song_item_name);
        data.moveToPosition(position);
        String name = data.getString(data.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
        nameText.setText(name);
        return view;
    }
}
