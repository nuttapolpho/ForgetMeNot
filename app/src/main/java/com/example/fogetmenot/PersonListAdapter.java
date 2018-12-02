package com.example.fogetmenot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PersonListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Person> personList;

    private class ViewHolder{
        ImageView imageView;
        TextView textView;
    }

    public PersonListAdapter(Context context, int layout, ArrayList<Person> personList) {
        this.context = context;
        this.layout = layout;
        this.personList = new ArrayList<>();
        this.personList = personList;
    }

    @Override
    public int getCount() {
        return personList.size();
    }

    @Override
    public Object getItem(int position) {
        return personList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View row = view;
        ViewHolder holder = new ViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.textView = (TextView) row.findViewById(R.id.item_name);
            holder.imageView = (ImageView) row.findViewById((R.id.item_pic));
            row.setTag(holder);
        }else{
            holder = (ViewHolder) row.getTag();
        }

        Person person = personList.get(position);
        holder.textView.setText(person.getName());
        holder.imageView.setImageBitmap(person.getImage());

        return row;
    }
}
