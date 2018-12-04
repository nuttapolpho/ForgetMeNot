package com.example.fogetmenot;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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
        Button deleteBtn;
        Button editBtn;
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
    public View getView(final int position, View view, ViewGroup parent) {
        View row = view;
        ViewHolder holder = new ViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.textView = (TextView) row.findViewById(R.id.item_name);
            holder.imageView = (ImageView) row.findViewById((R.id.item_pic));
            holder.editBtn = (Button) row.findViewById(R.id.edit_btn_member_item);
            holder.deleteBtn = (Button) row.findViewById(R.id.delete_btn_member_item);
            row.setTag(holder);
        }else{
            holder = (ViewHolder) row.getTag();
        }

        final Person person = personList.get(position);
        holder.textView.setText(person.getName());
        holder.imageView.setImageBitmap(person.getImage());
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                new AlertDialog.Builder(context)
                        .setTitle("ลบ" + person.getName())
                        .setMessage("ต้องการลบ " + person.getName() + " ออกจากรายการหรือไม่")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("ไม่", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                return;
                            }})
                        .setNegativeButton("ใช่", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Database db = new Database(context);
                                int id = person.getId();
                                if(db.deteleMember(id)){
                                    personList.remove(position);
                                    notifyDataSetChanged();
                                }else{
                                    Log.d("DELETE", "Can't delete " + person.getName());
                                }
                            }}).show();

            }
        });

        return row;
    }
}
