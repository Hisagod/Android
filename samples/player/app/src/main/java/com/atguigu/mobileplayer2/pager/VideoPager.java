package com.atguigu.mobileplayer2.pager;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.atguigu.mobileplayer2.R;
import com.atguigu.mobileplayer2.activity.SystemVideoPlayer;
import com.atguigu.mobileplayer2.adapter.VideoPagerAdapter;
import com.atguigu.mobileplayer2.base.BasePager;
import com.atguigu.mobileplayer2.domain.MediaItem;
import com.atguigu.mobileplayer2.utils.LogUtil;
import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.Objects;

/**
 * 作者：杨光福 on 2016/7/16 11:48
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：本地视频页面
 */
public class VideoPager extends Fragment {

    private ListView listview;
    private TextView tv_nomedia;
    private ProgressBar pb_loading;


    private VideoPagerAdapter videoPagerAdapter;

    /**
     * 装数据集合
     */
    private ArrayList<MediaItem> mediaItems;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mediaItems != null && mediaItems.size() > 0) {
                //有数据
                //设置适配器
                videoPagerAdapter = new VideoPagerAdapter(getContext(), mediaItems, true);
                listview.setAdapter(videoPagerAdapter);
                //把文本隐藏
                tv_nomedia.setVisibility(View.GONE);
            } else {
                //没有数据
                //文本显示
                tv_nomedia.setVisibility(View.VISIBLE);
            }


            //ProgressBar隐藏
            pb_loading.setVisibility(View.GONE);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.video_pager, null);
        listview = (ListView) view.findViewById(R.id.listview);
        tv_nomedia = (TextView) view.findViewById(R.id.tv_nomedia);
        pb_loading = (ProgressBar) view.findViewById(R.id.pb_loading);
        //设置ListView的Item的点击事件
        listview.setOnItemClickListener(new MyOnItemClickListener());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtil.e("本地视频的数据被初始化了。。。");
        //加载本地视频数据
        getDataFromLocal();
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            MediaItem mediaItem = mediaItems.get(position);
//            Toast.makeText(context, "mediaItem=="+mediaItem.toString(), Toast.LENGTH_SHORT).show();

            //1.调起系统所有的播放-隐式意图
//            Intent intent = new Intent();
//            intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*");
//            context.startActivity(intent);

            //2.调用自己写的播放器-显示意图--一个播放地址
//            Intent intent = new Intent(context,SystemVideoPlayer.class);
//            intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*");
//            context.startActivity(intent);
            //3.传递列表数据-对象-序列化
            Intent intent = new Intent(getContext(), SystemVideoPlayer.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("videolist", mediaItems);
            intent.putExtras(bundle);
            intent.putExtra("position", position);
            startActivity(intent);

        }
    }

    /**
     * 从本地的sdcard得到数据
     * //1.遍历sdcard,后缀名
     * //2.从内容提供者里面获取视频
     * //3.如果是6.0的系统，动态获取读取sdcard的权限
     */
    private void getDataFromLocal() {

        new Thread() {
            @Override
            public void run() {
                super.run();
//                SystemClock.sleep(2000);
                mediaItems = new ArrayList<>();
                ContentResolver resolver = requireContext().getContentResolver();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Video.Media.DISPLAY_NAME,//视频文件在sdcard的名称
                        MediaStore.Video.Media.DURATION,//视频总时长
                        MediaStore.Video.Media.SIZE,//视频的文件大小
                        MediaStore.Video.Media.DATA,//视频的绝对地址
                        MediaStore.Video.Media.ARTIST,//歌曲的演唱者

                };
                Cursor cursor = resolver.query(uri, null, null, null, null);
                while (cursor.moveToNext()) {

                    MediaItem mediaItem = new MediaItem();

                    mediaItems.add(mediaItem);//写在上面

                    String name = cursor.getString(0);//视频的名称
                    mediaItem.setName(name);

                    long duration = cursor.getLong(1);//视频的时长
                    mediaItem.setDuration(duration);

                    long size = cursor.getLong(2);//视频的文件大小
                    mediaItem.setSize(size);

                    String data = cursor.getString(3);//视频的播放地址
                    mediaItem.setData(data);

                    String artist = cursor.getString(4);//艺术家
                    mediaItem.setArtist(artist);


                }

                cursor.close();

                //Handler发消息
                handler.sendEmptyMessage(10);


            }
        }.start();

    }
}
