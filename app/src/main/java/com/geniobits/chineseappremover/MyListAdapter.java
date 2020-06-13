package com.geniobits.chineseappremover;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{
    private List<MyListData> listdata;

    // RecyclerView recyclerView;
    MyListAdapter(List<MyListData> listdata) {
        this.listdata = listdata;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.AppName.setText(listdata.get(position).getA_name());
        holder.RemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uninstallPackage(view.getContext(), listdata.get(position).getP_name());
                listdata.remove(position);
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        Button RemoveButton;
        TextView AppName;
        RelativeLayout relativeLayout;
        ViewHolder(View itemView) {
            super(itemView);
            this.RemoveButton =itemView.findViewById(R.id.btnRemove);
            this.AppName = itemView.findViewById(R.id.app_name);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }

    private void uninstallPackage(Context context, String packageName) {
        Log.e("packkagename",packageName);
        Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
        intent.setData(Uri.parse("package:"+packageName));
        context.startActivity(intent);
    }
}
