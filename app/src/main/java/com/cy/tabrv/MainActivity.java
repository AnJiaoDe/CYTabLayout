package com.cy.tabrv;


import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cy.tablayout.CYTabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private CYTabLayout cyTabLayout;
    private CYTabLayout cyTabLayout2;

    private CYTabLayout.TabAdapter<String> tabAdapter;
    private CYTabLayout.TabAdapter<String> tabAdapter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        cyTabLayout = (CYTabLayout) findViewById(R.id.cytab);


        GradientDrawable view_indicator = cyTabLayout.getView_indicator();


        List<String> list=new ArrayList<>();
        list.add("差滚");
        list.add("二个人体会");
        list.add("问题该如何");
        list.add("人员和他");
        list.add("容易");
        list.add("人员还头一回");
        list.add("人一样");
        list.add("人");
        list.add("日月同辉");
        list.add("人缘好");
        list.add("容易");
        list.add("二等分");
        list.add("软硬件");

        tabAdapter=new CYTabLayout.TabAdapter<String>(this,list) {
            @Override
            public void bindDataToView(CYTabLayout.ViewHolder holder, int position, String bean) {

                holder.setText(R.id.tv,bean);
            }

            @Override
            public int getItemLayoutID(int position, String bean) {
                return R.layout.item_rv;
            }

            @Override
            public void onTabSelected(CYTabLayout.ViewHolder holder, int position, String bean) {

                holder.setTextColor(R.id.tv,0xffffff00);
            }

            @Override
            public void onTabUnSelected(CYTabLayout.ViewHolder holder, int position, String bean) {
                holder.setTextColor(R.id.tv,0xffffffff);

            }

            @Override
            public void onTabReselected(CYTabLayout.ViewHolder holder, int position, String bean) {

                ToastUtils.showToast(MainActivity.this,"再次选中"+position);

            }
        };
        cyTabLayout.setCurrentItem(8);

        cyTabLayout.setAdapter(tabAdapter);



        //???????????????????????????????????????????????????????????
        cyTabLayout2 = (CYTabLayout) findViewById(R.id.cytab2);

        cyTabLayout2.setHaveIndicator(false);
        List<String> list2=new ArrayList<>();
        list2.add("女装");
        list2.add("裤子");
        list2.add("男装");
        list2.add("化妆品");
        list2.add("鞋包");
        list2.add("食品");
        list2.add("玩具");
        list2.add("内衣");

        tabAdapter2=new CYTabLayout.TabAdapter<String>(this,list2) {
            @Override
            public void bindDataToView(CYTabLayout.ViewHolder holder, int position, String bean) {

                holder.setText(R.id.tv,bean);
            }

            @Override
            public int getItemLayoutID(int position, String bean) {
                return R.layout.item_2;
            }

            @Override
            public void onTabSelected(CYTabLayout.ViewHolder holder, int position, String bean) {
                holder.setBackgroundColor(R.id.tv,0xffffff00);


            }

            @Override
            public void onTabUnSelected(CYTabLayout.ViewHolder holder, int position, String bean) {
                holder.setBackgroundColor(R.id.tv,0xfff6f6f7);

            }

            @Override
            public void onTabReselected(CYTabLayout.ViewHolder holder, int position, String bean) {
                remove(position);

                ToastUtils.showToast(MainActivity.this,"再次选中"+position);

            }
        };

        cyTabLayout2.setAdapter(tabAdapter2);

//        cyTabLayout.setWidth_indicator(100);
//        cyTabLayout.setHeight_indicator(30);

    }
}
