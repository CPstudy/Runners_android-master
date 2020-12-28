package com.guk2zzada.runnerswar;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class DataActivity extends AppCompatActivity {

    ArrayList<SingleList> arrSingle = new ArrayList<>();
    DatabaseManager dm;
    ListAdapter adapter;

    ListView list;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        dm = new DatabaseManager(getApplicationContext());
        list = findViewById(R.id.list);
        btnAdd = findViewById(R.id.btnAdd);

        arrSingle = dm.selectAllData();
        adapter = new ListAdapter(this, R.layout.item_data, arrSingle);
        list.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog(false, 0);
            }
        });

    }

    public void alertDialog(final boolean boolEdit, final int position) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setContentView(R.layout.dialog_data);

        final EditText edtWeek = dialog.findViewById(R.id.edtWeek);
        final EditText edtDate = dialog.findViewById(R.id.edtDate);
        final EditText edtStart = dialog.findViewById(R.id.edtStart);
        final EditText edtEnd = dialog.findViewById(R.id.edtEnd);
        final EditText edtDistance = dialog.findViewById(R.id.edtDistance);
        Button btnFin = dialog.findViewById(R.id.btnFin);

        DateManager dateManager = new DateManager();
        edtWeek.setText("" + dateManager.getYear() + dateManager.getMonth() + dateManager.getWeek());

        if(boolEdit) {
            edtWeek.setText(arrSingle.get(position).timeWeek);
            edtDate.setText(arrSingle.get(position).timeDate);
            edtStart.setText(arrSingle.get(position).timeStart);
            edtEnd.setText(arrSingle.get(position).timeEnd);
            edtDistance.setText("" + arrSingle.get(position).distance);
        }

        btnFin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(boolEdit) {
                    dm.updateData(arrSingle.get(position)._id,
                            edtWeek.getText().toString(),
                            edtDate.getText().toString(),
                            edtStart.getText().toString(),
                            edtEnd.getText().toString(),
                            Integer.parseInt(edtDistance.getText().toString()),
                            ""
                    );

                    arrSingle.get(position).timeWeek = edtWeek.getText().toString();
                    arrSingle.get(position).timeDate = edtDate.getText().toString();
                    arrSingle.get(position).timeStart = edtStart.getText().toString();
                    arrSingle.get(position).timeEnd = edtEnd.getText().toString();
                    arrSingle.get(position).distance = Integer.parseInt(edtDistance.getText().toString());

                } else {
                    dm.insertData(
                            edtWeek.getText().toString(),
                            edtDate.getText().toString(),
                            edtStart.getText().toString(),
                            edtEnd.getText().toString(),
                            Integer.parseInt(edtDistance.getText().toString()),
                            ""
                    );

                    arrSingle = dm.selectAllData();
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    class ListAdapter extends BaseAdapter {

        Context context;
        LayoutInflater inflater;
        ArrayList<SingleList> array;
        int layout;

        ListAdapter(Context context, int layout, ArrayList<SingleList> array) {
            this.context = context;
            this.layout = layout;
            this.array = array;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return array.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(layout, parent, false);
            }

            TextView txtDate = convertView.findViewById(R.id.txtDate);
            TextView txtStart = convertView.findViewById(R.id.txtStart);
            TextView txtEnd = convertView.findViewById(R.id.txtEnd);
            TextView txtDistance = convertView.findViewById(R.id.txtDistance);

            txtDate.setText("날짜: " + array.get(position).timeDate);
            txtStart.setText("시작 시간: " + array.get(position).timeStart);
            txtEnd.setText("도착 시간: " + array.get(position).timeEnd);
            txtDistance.setText("거리: " + array.get(position).distance);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog(true, position);
                }
            });

            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    dm.removeData(array.get(position)._id);
                    arrSingle.clear();
                    arrSingle = dm.selectAllData();
                    adapter.notifyDataSetChanged();
                    return false;
                }
            });

            return convertView;
        }
    }
}
