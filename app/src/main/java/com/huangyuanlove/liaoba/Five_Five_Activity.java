package com.huangyuanlove.liaoba;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.huangyuanlove.liaoba.adapter.Five_Five_Adapter;

public class Five_Five_Activity extends AppCompatActivity {
    private int[][] datas = new int[5][5];
    private GridView gridView;
    private Five_Five_Adapter adapter;
    private int flag = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five__five);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle("提示")
                .setMessage("点击小方块会改变它和它上下左右的颜色，将所有小方块的颜色变成其他颜色，you win")
                .setPositiveButton("确定",null)
                .create();
        alertDialog.show();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                datas[i][j]= 1;
            }
        }
        gridView = (GridView) findViewById(R.id.five_five_gridView);
        adapter = new Five_Five_Adapter(this, datas);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                int i = position/5;
                int j = position%5;
                negative(i, j);
                if(i>0)
                    negative(i-1, j);
                if(i<4)
                    negative(i+1, j);
                if(j>0)
                    negative(i, j-1);
                if(j<4)
                    negative(i, j+1);
                adapter.notifyDataSetChanged();
                for(int[] data:datas){
                    for(int d:data){
                        if(d==flag){
                            return;
                        }
                    }
                }
                flag=-flag;
                Toast.makeText(getApplicationContext(), "you win", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void negative(int i, int j){
        datas[i][j] = -datas[i][j];
    }
}
