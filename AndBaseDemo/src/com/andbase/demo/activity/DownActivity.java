package com.andbase.demo.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.download.AbDownloadProgressListener;
import com.ab.download.AbDownloadThread;
import com.ab.download.AbFileDownloader;
import com.ab.download.DownFile;
import com.ab.download.DownFileDao;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.task.AbTaskPool;
import com.ab.util.AbFileUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.demo.adapter.MyExpandableListAdapter;
import com.andbase.global.Constant;
import com.andbase.global.MyApplication;

/**
 * AbFileUtil.setDownPathFileDir()设置文件下载路径
 * @author Xuechao Wang <br/>
 */
public class DownActivity extends AbActivity {

    private DownFileDao mDownFileDao = null;
    private final String TAG=DownActivity.class.getName();

    public ImageView itemsIcon;
    public TextView itemsTitle;
    public TextView itemsDesc;
    public Button operateBtn;
    public ProgressBar progress;
    public TextView received_progress_percent;
    public TextView received_progress_number;
    public RelativeLayout received_progressBar;

    DownFile mDownFile;
    public HashMap<String, AbFileDownloader> mFileDownloaders = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAbContentView(R.layout.activity_down);
        mDownFileDao = DownFileDao.getInstance(this);
        mFileDownloaders = new HashMap<String, AbFileDownloader>();
        initDownFileList();
        initView();

    }

    private void initView() {
        itemsIcon = (ImageView) findViewById(R.id.itemsIcon);
        itemsTitle = (TextView) findViewById(R.id.itemsTitle);
        itemsDesc = (TextView) findViewById(R.id.itemsDesc);
        operateBtn = (Button) findViewById(R.id.operateBtn);
        progress = (ProgressBar) findViewById(R.id.received_progress);
        received_progress_percent = (TextView) findViewById(R.id.received_progress_percent);
        received_progress_number = (TextView) findViewById(R.id.received_progress_number);
        received_progressBar = (RelativeLayout) findViewById(R.id.received_progressBar);
        
        itemsTitle.setText(mDownFile.getName());
        itemsDesc.setText(mDownFile.getDescription());
        if (mDownFile.getState() == Constant.undownLoad) {
            operateBtn.setBackgroundResource(R.drawable.down_load);
            received_progressBar.setVisibility(View.GONE);
            itemsDesc.setVisibility(View.VISIBLE);
            progress.setProgress(0);
            received_progress_percent.setText(0 + "%");
            received_progress_number.setText("0KB/" + AbStrUtil.getSizeDesc(mDownFile.getTotalLength()));
        } else if (mDownFile.getState() == Constant.downInProgress) {
            operateBtn.setBackgroundResource(R.drawable.down_pause);
            if (mDownFile.getDownLength() != 0 && mDownFile.getTotalLength() != 0) {
                int c = (int) (mDownFile.getDownLength() * 100 / mDownFile.getTotalLength());
                itemsDesc.setVisibility(View.GONE);
                received_progressBar.setVisibility(View.VISIBLE);
                progress.setProgress(c);
                received_progress_percent.setText(c + "%");
                received_progress_number.setText(AbStrUtil.getSizeDesc(mDownFile.getDownLength()) + "/"
                        + AbStrUtil.getSizeDesc(mDownFile.getTotalLength()));
            }
        } else if (mDownFile.getState() == Constant.downLoadPause) {
            operateBtn.setBackgroundResource(R.drawable.down_load);
            // 下载了多少
            if (mDownFile.getDownLength() != 0 && mDownFile.getTotalLength() != 0) {
                int c = (int) (mDownFile.getDownLength() * 100 / mDownFile.getTotalLength());
                itemsDesc.setVisibility(View.GONE);
                received_progressBar.setVisibility(View.VISIBLE);
                progress.setProgress(c);
                received_progress_percent.setText(c + "%");
                received_progress_number.setText(AbStrUtil.getSizeDesc(mDownFile.getDownLength()) + "/"
                        + AbStrUtil.getSizeDesc(mDownFile.getTotalLength()));
            } else {
                itemsDesc.setVisibility(View.VISIBLE);
                received_progressBar.setVisibility(View.GONE);
                progress.setProgress(0);
                received_progress_percent.setText(0 + "%");
                received_progress_number.setText("0KB/" + AbStrUtil.getSizeDesc(mDownFile.getTotalLength()));
            }
        } else if (mDownFile.getState() == Constant.downloadComplete) {
            operateBtn.setBackgroundResource(R.drawable.down_delete);
            received_progressBar.setVisibility(View.GONE);
            itemsDesc.setVisibility(View.VISIBLE);
        }
        final AbDownloadProgressListener mDownloadProgressListener = new AbDownloadProgressListener() {
            // 实时获知文件已经下载的数据长度
            @Override
            public void onDownloadSize(final long size) {
                Log.e(TAG, "onDownloadSize:" + size);
                if (mDownFile.getTotalLength() == 0) {
                    return;
                }
                final int c = (int) (size * 100 / mDownFile.getTotalLength());
                if (c != progress.getProgress()) {
                    progress.post(new Runnable() {
                        @Override
                        public void run() {
                            progress.setProgress(c);
                            received_progress_percent.setText(c + "%");
                            received_progress_number.setText(AbStrUtil.getSizeDesc(size) + "/"
                                    + AbStrUtil.getSizeDesc(mDownFile.getTotalLength()));
                        }
                    });
                }
                if (mDownFile.getTotalLength() == size) {
                        Log.d(TAG, "下载完成:" + size);
                    mDownFile.setState(Constant.downloadComplete);
                    // 下载完成
                    //mDownFile
                }
            }
        };
        
        // 处理按钮事件
        operateBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // 无sd卡
                    AbToastUtil.showToast(getApplicationContext(), "没找到存储卡");
                    return;
                }

                if (mDownFile.getState() == Constant.undownLoad || mDownFile.getState() == Constant.downLoadPause) {
                    // 下载

                    itemsDesc.setVisibility(View.GONE);
                    received_progressBar.setVisibility(View.VISIBLE);
                    operateBtn.setBackgroundResource(R.drawable.down_pause);
                    mDownFile.setState(Constant.downInProgress);
                    AbTask mAbTask = new AbTask();
                    final AbTaskItem item = new AbTaskItem();
                    item.setListener(new AbTaskListener() {

                        @Override
                        public void update() {
                        }

                        @Override
                        public void get() {
                            try {
                                // 检查文件总长度
                                int totalLength = AbFileUtil.getContentLengthFromUrl(mDownFile.getDownUrl());
                                mDownFile.setTotalLength(totalLength);
                                // 开始下载文件
                                AbFileDownloader loader = new AbFileDownloader(getApplicationContext(), mDownFile, 4);
                                mFileDownloaders.put(mDownFile.getDownUrl(), loader);
                                loader.download(mDownloadProgressListener);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        };
                    });
                    mAbTask.execute(item);

                } else if (mDownFile.getState() == Constant.downInProgress) {
                    // 暂停
                    operateBtn.setBackgroundResource(R.drawable.down_load);
                    mDownFile.setState(Constant.undownLoad);
                    AbFileDownloader mFileDownloader = mFileDownloaders.get(mDownFile.getDownUrl());
                    // 释放原来的线程
                    if (mFileDownloader != null) {
                        mFileDownloader.setFlag(false);
                        AbDownloadThread mDownloadThread = mFileDownloader.getThreads();
                        if (mDownloadThread != null) {
                            mDownloadThread.setFlag(false);
                            mFileDownloaders.remove(mDownFile.getDownUrl());
                            mDownloadThread = null;
                        }
                        mFileDownloader = null;
                    }
                } else if (mDownFile.getState() == Constant.downloadComplete) {
                    // 下载完成的界面更新
                    
                }

            }
        });
    }

    /**
     * 初始化所有文件
     */
    private void initDownFileList() {

         mDownFile = new DownFile();
        mDownFile.setName("愤怒的小鸟");
        mDownFile.setDescription("以星球大战电影前传为背景");
        mDownFile.setPakageName("");
        mDownFile.setState(Constant.undownLoad);
        mDownFile.setIcon(String.valueOf(R.drawable.default_pic));
        mDownFile.setDownUrl("http://down.apk.hiapk.com/down?aid=1832508&em=13");
        mDownFile.setSuffix(".apk");

        // 初始化文件已经下载的长度，计算已下载的进度
        // 本地数据
        DownFile mDownFileT = mDownFileDao.getDownFile(mDownFile.getDownUrl());
        if (mDownFileT != null) {
            mDownFile = mDownFileT;
            if (mDownFile.getDownLength() == mDownFile.getTotalLength() && mDownFile.getTotalLength() != 0) {
                mDownFile.setState(Constant.downloadComplete);
            } else {
                // 显示为暂停状态
                mDownFile.setState(Constant.downLoadPause);
            }
        } else {
            mDownFile.setState(Constant.undownLoad);
        }
    }

    @Override
    public void finish() {
        super.finish();

        // 释放所有的下载线程
        releaseThread();

    }
    /**
     * 描述：释放线程
     */
    public void releaseThread() {
        Iterator it = mFileDownloaders.entrySet().iterator();
        AbFileDownloader mFileDownloader = null;
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            mFileDownloader = (AbFileDownloader) e.getValue();
            // System.out.println("Key: " + e.getKey() + "; Value: " +
            // e.getValue());
            if (mFileDownloader != null) {
                mFileDownloader.setFlag(false);
                AbDownloadThread mDownloadThread = mFileDownloader.getThreads();
                if (mDownloadThread != null) {
                    mDownloadThread.setFlag(false);
                    mDownloadThread = null;
                }
                mFileDownloader = null;
            }
        }
    }
}
