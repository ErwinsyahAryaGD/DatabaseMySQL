package com.example.databasemysql.adapter;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.databasemysql.EditTeman;
import com.example.databasemysql.MainActivity;
import com.example.databasemysql.R;
import com.example.databasemysql.app.AppController;
import com.example.databasemysql.database.Teman;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TemanAdapter extends RecyclerView.Adapter<TemanAdapter.TemanViewHolder> {
    private ArrayList<Teman> listData;

    public TemanAdapter(ArrayList<Teman> listData) {
        this.listData = listData;
    }

    @Override
    public TemanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInf = LayoutInflater.from(parent.getContext());
        View view = layoutInf.inflate(R.layout.row_data_tambah_teman,parent,false);
        return new TemanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TemanAdapter.TemanViewHolder holder, int position) {
        String id,nm,tlp;

        id = listData.get(position).getId();
        nm = listData.get(position).getNama();
        tlp = listData.get(position).getTelpon();

        holder.namaTxt.setText(nm);
        holder.telpontxt.setText(tlp);
        holder.namaTxt.setTextColor(Color.BLUE);
        holder.namaTxt.setTextSize(20);

        holder.cardku.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu pm = new PopupMenu(v.getContext(), v);
                pm.inflate(R.menu.popup1);
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId())
                        {
                            case R.id.edit:
                                Bundle bendel = new Bundle();
                                bendel.putString("kunci1", id);
                                bendel.putString("kunci2", nm);
                                bendel.putString("kunci3", tlp);
                                Intent intent = new Intent(v.getContext(), EditTeman.class);
                                intent.putExtras(bendel);
                                v.getContext().startActivity(intent);
                                break;
                            case R.id.hapus:
                                AlertDialog.Builder alertdb = new AlertDialog.Builder(v.getContext());
                                alertdb.setTitle("Yakin " +nm+" akan dihapus?");
                                alertdb.setMessage("Tekan Ya untuk menghapus");
                                alertdb.setCancelable(false);
                                alertdb.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {
                                        HapusData(id);
                                        Toast.makeText(v.getContext(),"Data " +id+ "telah dihapus", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(v.getContext(), MainActivity.class);
                                        v.getContext().startActivity(intent);

                                    }
                                });
                                alertdb.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog adlg = alertdb.create();
                                adlg.show();
                                break;
                        }
                        return false;
                    }
                });
                pm.show();
                return true;
            }
        });

    }

    private void HapusData(final String idx){
        String url_update = "https://20200140082.praktikumtiumy.com/deletetm.php";
        final String TAG = MainActivity.class.getSimpleName();
        final String TAG_SUCCES = "success";
        final int[] sukses = new int[1];

        StringRequest stringReq = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Respon : " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    sukses[0] = jObj.getInt(TAG_SUCCES);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(TAG,"Error : " +error.getMessage());
            }
        })
        {
         @Override
         protected Map<String,String> getParams(){
             Map<String,String> params = new HashMap<>();

             params.put("id",idx);

             return params;

         }
        };
        AppController.getInstance().addRequestQueue(stringReq);
    }


    @Override
    public int getItemCount() {
        return(listData != null)?listData.size() : 0;
    }

    public class TemanViewHolder extends RecyclerView.ViewHolder{
        private CardView cardku;
        private TextView namaTxt,telpontxt;
        public TemanViewHolder(View view) {
            super(view);
            cardku = (CardView) view.findViewById(R.id.kartuku);
            namaTxt = (TextView) view.findViewById(R.id.txtNama);
            telpontxt = (TextView) view.findViewById(R.id.txtTelpon);

        }
    }
}