package com.mobileclient.activity.myorder;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cc.testdemo.FouthFragment;
import com.cc.testdemo.ThreeFragment;
import com.cc.testdemo.TwoFragment;
import com.mobileclient.activity.ExpressOrderAddActivity;
import com.mobileclient.activity.ExpressOrderDetailActivity;
import com.mobileclient.activity.MainUserActivity;
import com.mobileclient.activity.MyInfoActivtiy;
import com.mobileclient.activity.PayPwdActivity;
import com.mobileclient.activity.R;
import com.mobileclient.activity.UserInfoEditActivity;
import com.mobileclient.app.Declare;
import com.mobileclient.fragment.MyOrderFourFragment;
import com.mobileclient.fragment.MyOrderOneFragment;
import com.mobileclient.fragment.MyOrderThreeFragment;
import com.mobileclient.fragment.MyOrderTwoFragment;
import com.mobileclient.util.ActivityUtils;
import com.mobileclient.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class ExpressTakeMyListActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView title, item_quanbu, item_jiedan, item_shouhuo,item_pingjia;
    private ViewPager vp;
    private MyOrderOneFragment oneFragment;
    private MyOrderTwoFragment twoFragment;
    private MyOrderThreeFragment threeFragment;
    private MyOrderFourFragment fouthFragment;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private FragmentAdapter mFragmentAdapter;
    private ImageView add;
   // String[] titles = new String[]{"待接单", "送单中", "待收获","待评价"};
    Intent intent=new Intent();
    Declare declare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除工具栏
        //getSupportActionBar().hide();
        setContentView(R.layout.my_order);


        declare= (Declare) getApplication();

        initViews();
        title=findViewById(R.id.title);
        title.setText("我的发布");
        ImageView back = (ImageView) this.findViewById(R.id.back_btn);

        back.setVisibility(View.GONE);
        mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), mFragmentList);
        vp.setOffscreenPageLimit(4);//ViewPager的缓存为4帧
        vp.setAdapter(mFragmentAdapter);
        vp.setCurrentItem(0);//初始设置ViewPager选中第一帧
        item_quanbu.setTextColor(Color.parseColor("#66CDAA"));
        //=====add==
        add=findViewById(R.id.search);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(declare.getPayPwd().equals("--")){
                    Toast.makeText(ExpressTakeMyListActivity.this,"请先设置支付密码！",Toast.LENGTH_SHORT).show();
                    intent = new Intent(ExpressTakeMyListActivity.this, PayPwdActivity.class);
                    startActivity(intent);
                }
                else {
                    intent.setClass(ExpressTakeMyListActivity.this, ExpressOrderAddActivity.class);
                    startActivityForResult(intent, ActivityUtils.ADD_CODE);
                }
            }
        });
        //=======
        //ViewPager的监听事件
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                /*此方法在页面被选中时调用*/
                //title.setText(titles[position]);
                changeTextColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /*此方法是在状态改变的时候调用，其中arg0这个参数有三种状态（0，1，2）。
                arg0 ==1的时辰默示正在滑动，
                arg0==2的时辰默示滑动完毕了，
                arg0==0的时辰默示什么都没做。*/
            }
        });
    }

    /**
     * 初始化布局View
     */
    private void initViews() {
        title = (TextView) findViewById(R.id.title);
        item_quanbu = (TextView) findViewById(R.id.item_quanbu);
        item_jiedan = (TextView) findViewById(R.id.item_jiedan);
        item_shouhuo = (TextView) findViewById(R.id.item_shouhuo);
        item_pingjia = (TextView) findViewById(R.id.item_pingjia);

        item_quanbu.setOnClickListener(this);
        item_jiedan.setOnClickListener(this);
        item_shouhuo.setOnClickListener(this);
        item_pingjia.setOnClickListener(this);

        vp = (ViewPager) findViewById(R.id.mainViewPager);
        oneFragment = new MyOrderOneFragment();
        twoFragment = new MyOrderTwoFragment();
        threeFragment = new MyOrderThreeFragment();
        fouthFragment = new MyOrderFourFragment();
        //给FragmentList添加数据
        mFragmentList.add(oneFragment);
        mFragmentList.add(twoFragment);
        mFragmentList.add(threeFragment);
        mFragmentList.add(fouthFragment);
    }

    /**
     * 点击底部Text 动态修改ViewPager的内容
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_quanbu:
                vp.setCurrentItem(0, true);
                break;
            case R.id.item_jiedan:
                vp.setCurrentItem(1, true);
                break;
            case R.id.item_shouhuo:
                vp.setCurrentItem(2, true);
                break;
            case R.id.item_pingjia:
                vp.setCurrentItem(3, true);
                break;
        }
    }


    public class FragmentAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList = new ArrayList<Fragment>();

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }

    /*
     *由ViewPager的滑动修改底部导航Text的颜色
     */
    private void changeTextColor(int position) {
        if (position == 0) {
            item_quanbu.setTextColor(Color.parseColor("#66CDAA"));
            item_jiedan.setTextColor(Color.parseColor("#000000"));
            item_shouhuo.setTextColor(Color.parseColor("#000000"));
            item_pingjia.setTextColor(Color.parseColor("#000000"));
        } else if (position == 1) {
            item_quanbu.setTextColor(Color.parseColor("#000000"));
            item_jiedan.setTextColor(Color.parseColor("#66CDAA"));
            item_shouhuo.setTextColor(Color.parseColor("#000000"));
            item_pingjia.setTextColor(Color.parseColor("#000000"));
        } else if (position == 2) {
            item_quanbu.setTextColor(Color.parseColor("#000000"));
            item_jiedan.setTextColor(Color.parseColor("#000000"));
            item_shouhuo.setTextColor(Color.parseColor("#66CDAA"));
            item_pingjia.setTextColor(Color.parseColor("#000000"));
        } else if (position == 3) {
            item_pingjia.setTextColor(Color.parseColor("#66CDAA"));
            item_jiedan.setTextColor(Color.parseColor("#000000"));
            item_quanbu.setTextColor(Color.parseColor("#000000"));
            item_shouhuo.setTextColor(Color.parseColor("#000000"));
        }
    }
}
