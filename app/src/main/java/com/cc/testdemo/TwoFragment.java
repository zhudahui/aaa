package com.cc.testdemo;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mobileclient.activity.LoginActivity;
import com.mobileclient.activity.MyProgressDialog;
import com.mobileclient.activity.R;
import com.mobileclient.activity.SecondOrderDetailActivity;
import com.mobileclient.activity.SecondUserDetailActivity;
import com.mobileclient.activity.UserInfoDetailActivity;

import com.mobileclient.adapter.UserInfoSimpleAdapter;
import com.mobileclient.app.Declare;
import com.mobileclient.app.RefreshListView;
import com.mobileclient.domain.User;
import com.mobileclient.domain.UserInfo;
import com.mobileclient.service.UserService;
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import butterknife.ButterKnife;

public class TwoFragment extends Fragment {

    UserInfoSimpleAdapter adapter;
    List<Map<String, Object>> list;
    private User queryConditionUser=null;
   // @BindView(R.id.id_recyclerview)
    private RefreshListView lv;
    private List<String> stringList;
    private MyProgressDialog dialog; //进度条	@Override
    UserService userService=new UserService();
    private int userId;
    private Declare declare;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        lv=view.findViewById(R.id.list_view);
        dialog = MyProgressDialog.getInstance(getContext());
        declare = (Declare)getActivity().getApplication();
        ButterKnife.bind(this, view);
        setViews();
        return view;
    }

    private void initData() {
        stringList = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            stringList.add(String.valueOf(i));
        }

       // mAdapter = new MyRecyclerViewAdapter(getActivity(), stringList);
        //设置布局管理器
       // mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        //设置adapter
       // mRecyclerview.setAdapter(mAdapter);
        //添加分割线
       // mRecyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        //mAdapter.setOnItemClickLitener(this);
    }


    private void setViews() {

        dialog.show();
        final Handler handler = new Handler();
        new Thread(){
            @Override
            public void run() {
                //在子线程中进行下载数据操作
                list = getDatas();
                //发送消失到handler，通知主线程下载完成
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.cancel();
                        adapter = new UserInfoSimpleAdapter(getContext(), list,
                                R.layout.userinfo_list_item,
                                new String[] { "userPhoto","userType","nickName" },
                                new int[] {R.id.iv_userPhoto,R.id.userType,R.id.nickName},lv);
                        lv.setAdapter(adapter);
                    }
                });
            }
        }.start();
        //=================
        lv.setonRefreshListener(new RefreshListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                setViews();
                adapter.notifyDataSetChanged();
                lv.onRefreshComplete();

            }
        });
        // 添加点击
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final int i=position-1;
                if(declare.getIdentify().equals("admin")) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                    builder.setMessage("确认删除？");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //删掉长按的item
                            //  list.remove(position);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    userService.DeleteUserInfo((Integer) list.get(i).get("userId"));
                                }
                            }).start();
                            //  动态更新listview
                            adapter.notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    android.support.v7.app.AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }

                return true;
            }

        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), UserInfoDetailActivity.class);
                arg2=arg2-1;
                Bundle bundle = new Bundle();
                bundle.putString("nickName", list.get(arg2).get("nickName").toString());
                bundle.putString("userName", list.get(arg2).get("userName").toString());
                bundle.putInt("userId", Integer.parseInt(list.get(arg2).get("userId").toString()));
                bundle.putInt("studentId", Integer.parseInt(list.get(arg2).get("studentId").toString()));
                bundle.putString("userPassword", list.get(arg2).get("userPassword").toString());
                bundle.putString("userType", list.get(arg2).get("userType").toString());
                bundle.putString("userPhone", list.get(arg2).get("userPhone").toString());
                bundle.putString("userGender", list.get(arg2).get("userGender").toString());
                bundle.putString("userEmail", list.get(arg2).get("userEmail").toString());
                bundle.putString("userMoney", list.get(arg2).get("userMoney").toString());
                bundle.putInt("userReputation", Integer.parseInt(list.get(arg2).get("userReputation").toString()));
                bundle.putString("regTime", list.get(arg2).get("regTime").toString());
                bundle.putString("userAuthFile", list.get(arg2).get("userAuthFile").toString());
                bundle.putString("userAuthState", list.get(arg2).get("userAuthState").toString());
                bundle.putString("payPwd", list.get(arg2).get("payPwd").toString());
                bundle.putByteArray("photo", (byte[]) list.get(arg2).get("photo"));
                bundle.putString("Photo", list.get(arg2).get("Photo").toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


    private View.OnCreateContextMenuListener UserListItemListener = new View.OnCreateContextMenuListener() {
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            if(declare.getIdentify().equals("admin")) {
                menu.add(0, 0, 0, "编辑用户信息");
                menu.add(0, 1, 0, "删除用户信息");
            }
        }
    };



    // 长按菜单响应函数
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {  //编辑用户信息
            ContextMenu.ContextMenuInfo info = item.getMenuInfo();
            AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) info;
            // 获取选中行位置
            int arg2 = contextMenuInfo.position;
            // 获取用户名
            arg2=arg2-1;
            Intent intent = new Intent();
            intent.setClass(getActivity(), SecondUserDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("nickName", list.get(arg2).get("nickName").toString());
            bundle.putString("userName", list.get(arg2).get("userName").toString());
            bundle.putInt("userId", Integer.parseInt(list.get(arg2).get("userId").toString()));
            bundle.putInt("studentId", Integer.parseInt(list.get(arg2).get("studentId").toString()));
            bundle.putString("userPassword", list.get(arg2).get("userPassword").toString());
            bundle.putString("userType", list.get(arg2).get("userType").toString());
            bundle.putString("userPhone", list.get(arg2).get("userPhone").toString());
            bundle.putString("userGender", list.get(arg2).get("userGender").toString());
            bundle.putString("userEmail", list.get(arg2).get("userEmail").toString());
            bundle.putString("userMoney", list.get(arg2).get("userMoney").toString());
            bundle.putInt("userReputation", Integer.parseInt(list.get(arg2).get("userReputation").toString()));
            bundle.putString("regTime", list.get(arg2).get("regTime").toString());
            bundle.putString("userAuthFile", list.get(arg2).get("userAuthFile").toString());
            bundle.putByteArray("photo", (byte[]) list.get(arg2).get("photo"));
            bundle.putString("Photo", list.get(arg2).get("Photo").toString());
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (item.getItemId() == 1) {// 删除用户信息
            ContextMenu.ContextMenuInfo info = item.getMenuInfo();
            AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) info;
            // 获取选中行位置
            int position = contextMenuInfo.position;
            Log.i("ppp","tt"+getActivity());
            // 获取用户Id
            position=position-1;
            userId = Integer.parseInt(list.get(position).get("userId").toString());
            dialog();
        }
        return super.onContextItemSelected(item);
    }





    // 删除
    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("确认删除吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        userService.DeleteUserInfo(userId);
                    }
                }).start();
                //String result = UserService.DeleteUser(user_name);
                //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                setViews();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    private List<Map<String, Object>> getDatas() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            /* 查询用户信息 */
            List<User> userList = userService.QueryUser(queryConditionUser);
            for (int i = 0; i < userList.size(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                if(userList.get(i).getUserType().equals("快递员")) {
                    map.put("userId", userList.get(i).getUserId());
                    map.put("userName", userList.get(i).getUserName());
                    map.put("userPassword", userList.get(i).getUserPassword());
                    map.put("Photo",userList.get(i).getUserPhoto());
                    byte[] userPhoto_data = null;
                    // 获取图片数据
                    userPhoto_data = ImageService.getImage(HttpUtil.DOWNURL + userList.get(i).getUserPhoto());
                    map.put("photo", userPhoto_data);
                    Bitmap userPhoto = BitmapFactory.decodeByteArray(userPhoto_data, 0, userPhoto_data.length);
                    map.put("userPhoto", userPhoto);
                    map.put("userType", userList.get(i).getUserType());
                    map.put("userPhone", userList.get(i).getUserPhone());
                    map.put("userGender", userList.get(i).getUserGender());
                    map.put("userEmail", userList.get(i).getUserEmail());
                    map.put("userReputation", userList.get(i).getUserReputation());
                    map.put("userMoney", userList.get(i).getUserMoney());
                    map.put("userAuthFile", userList.get(i).getUserAuthFile());
                    map.put("regTime", userList.get(i).getRegTime());
                    map.put("studentId", userList.get(i).getStudentId());
                    map.put("nickName", userList.get(i).getNickName());
                    map.put("userAuthState",userList.get(i).getUserAuthState());
                    map.put("payPwd",userList.get(i).getPayPwd());
				/*byte[] userPhoto_data = ImageService.getImage(HttpUtil.BASE_URL+ UserList.get(i).getUserPhoto());// 获取图片数据
				BitmapFactory.Options userPhoto_opts = new BitmapFactory.Options();
				userPhoto_opts.inJustDecodeBounds = true;
				BitmapFactory.decodeByteArray(userPhoto_data, 0, userPhoto_data.length, userPhoto_opts);
				userPhoto_opts.inSampleSize = photoListActivity.computeSampleSize(userPhoto_opts, -1, 100*100);
				userPhoto_opts.inJustDecodeBounds = false;
				try {
					Bitmap userPhoto = BitmapFactory.decodeByteArray(userPhoto_data, 0, userPhoto_data.length, userPhoto_opts);
					map.put("userPhoto", userPhoto);
				} catch (OutOfMemoryError err) { }*/
//				map.put("userPhoto", HttpUtil.BASE_URL+ UserList.get(i).getUserPhoto());
//				map.put("telephone", UserList.get(i).getTelephone());
//				map.put("shenHeState", UserList.get(i).getShenHeState());
                    list.add(map);
                }
            }
        } catch (Exception e) {
            //	Toast.makeText(getApplicationContext(), "", 1).show();
        }
        return list;
    }

    /**
     * 定义一个handler处理请求返回来的信息
     */
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            System.out.println("这是刷新返回来的信息");
            adapter.notifyDataSetChanged();
            lv.onRefreshComplete();
        }

    };



}
