package com.hong.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hong.test.mini3d.SplashActivity;

public class MainActivity extends AppCompatActivity {

    private String[] items = new String[]{"min3D", "网络测试"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("hong +++++++++++++++++");

        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (items[position]) {
                    case "min3D":
                        startActivity(new Intent(MainActivity.this, SplashActivity.class));
                        break;
                    case "网络测试":
                        startActivity(new Intent(MainActivity.this, TestNetActivity.class));
                        break;
                }
            }
        });
    }
}
