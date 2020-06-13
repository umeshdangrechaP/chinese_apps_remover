package com.geniobits.chineseappremover;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PackageManager packageManger;
    List<MyListData> package_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        package_list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(readJSONFromAsset());
            JSONArray data = jsonArray.getJSONObject(2).getJSONArray("data");
            packageManger = getApplicationContext().getPackageManager();
            for(int i=0;i<data.length();i++){
                if (isPackageInstalled(data.getJSONObject(i).getString("p_name"),packageManger)){
                    package_list.add(new MyListData(data.getJSONObject(i).getString("p_name"),data.getJSONObject(i).getString("a_name")));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(package_list.isEmpty()){
            Toast.makeText(this, "You are free from chinese apps", Toast.LENGTH_SHORT).show();
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        MyListAdapter adapter = new MyListAdapter(package_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public String readJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("chinese_app_list.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            ApplicationInfo ai = packageManager.getApplicationInfo(packageName, 0);
            if ((ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                return false;
            }
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
