package jamesou.autotalking;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import jamesou.autotalking.service.AutoTalkingService;
import jamesou.autotalking.utils.Config;


/**
 * Created by ouyangjian on 2016-04-08.
 */
//TODO 增加抢红包功能，代码混淆，打开程序后的提示功能
public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
   // private Dialog mTipsDialog;
    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;
    private MainFragment mainFrament;
    //定义浮动窗口布局
    LinearLayout mFloatLayout;
    WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    WindowManager mWindowManager;
    ImageView mFloatView;
    Preference wechatPref;

    private BroadcastReceiver autoTalkingConnectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(isFinishing()) {
                return;
            }
            String action = intent.getAction();
            Log.d(TAG, "receive-->" + action);
            if(Config.ACTION_AUTOTALKING_SERVICE_DISCONNECT.equals(action)) {//如果没有开启服务，需要提示先开启服务
                showAssistant();
            }
            if(Config.ACTION_NOTIFY_LISTENER_SERVICE_DISCONNECT.equals(action)) {//如果没有开启服务，需要提示先开启服务
                showNotice();
            }
        /*    if(Config.ACTION_AUTOTALKING_SERVICE_CONNECT.equals(action)) {
                if (mTipsDialog != null) {
                    mTipsDialog.dismiss();
                }
            } else if(Config.ACTION_AUTOTALKING_SERVICE_DISCONNECT.equals(action)) {//todo如果没有开启服务，需要提示先开启服务
               // showOpenAccessibilityServiceDialog();
            } else if(Config.ACTION_NOTIFY_LISTENER_SERVICE_CONNECT.equals(action)) {
                if(mMainFragment != null) {
                    mMainFragment.updateNotifyPreference();
                }
            } else if(Config.ACTION_NOTIFY_LISTENER_SERVICE_DISCONNECT.equals(action)) {
                if(mMainFragment != null) {
                    mMainFragment.updateNotifyPreference();
                }
            }*/
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        setContentView(R.layout.main);
        setupMain();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.ACTION_AUTOTALKING_SERVICE_CONNECT);
        filter.addAction(Config.ACTION_AUTOTALKING_SERVICE_DISCONNECT);
        filter.addAction(Config.ACTION_NOTIFY_LISTENER_SERVICE_DISCONNECT);
        filter.addAction(Config.ACTION_NOTIFY_LISTENER_SERVICE_CONNECT);
        registerReceiver(autoTalkingConnectReceiver, filter);
        setupPermission();
        createFloatView();
    }

    private void setupPermission()
    {
        if(Build.VERSION.SDK_INT >= 23)//TODO 低版本JDK测试
        {
            if(!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent,OVERLAY_PERMISSION_REQ_CODE);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if(Build.VERSION.SDK_INT >= 23) {
                if (!Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "6.0以上版本需要设置悬浮窗口,请同意设置.", Toast.LENGTH_SHORT).show();
                    setupPermission();
                }
            }
        }
    }
    private void setupMain()
    {
        getFragmentManager().beginTransaction().add(R.id.container, getSettingsFragment()).commitAllowingStateLoss();
    }
    public Fragment getSettingsFragment()
    {
        if(mainFrament==null)
            mainFrament = new MainFragment();
        wechatPref = mainFrament.getWechatPref();
        return mainFrament;

    }

    private void setupActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
    }

    public void createFloatView()
    {
        //TODO 这里有bug，缩小到桌面时候会出现程序强制关闭
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        wmParams = new WindowManager.LayoutParams();
        //获取WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        //设置window type
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        /*
 	         * 下面的flags属性的效果形同“锁定”。
 	         * 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
 	         wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL
 	                               | LayoutParams.FLAG_NOT_FOCUSABLE
 	                               | LayoutParams.FLAG_NOT_TOUCHABLE;
 	        */
        wmParams.flags =
//          LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//          LayoutParams.FLAG_NOT_TOUCHABLE

        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;

        // 以屏幕左上角为原点，设置x、y初始值
        wmParams.x = 0;
        wmParams.y = 0;

        /*// 设置悬浮窗口长宽数据
        wmParams.width = 200;
        wmParams.height = 80;*/

        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_flag, null);
        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);

       /* Log.i(TAG, "mFloatLayout-->left" + mFloatLayout.getLeft());
        Log.i(TAG, "mFloatLayout-->right" + mFloatLayout.getRight());
        Log.i(TAG, "mFloatLayout-->top" + mFloatLayout.getTop());
        Log.i(TAG, "mFloatLayout-->bottom" + mFloatLayout.getBottom());*/

        //浮动窗口按钮
        mFloatView = (ImageView)mFloatLayout.findViewById(R.id.open_flag);

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        // Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredWidth() / 2);
        // Log.i(TAG, "Height/2--->" + mFloatView.getMeasuredHeight() / 2);

        mFloatView.setImageResource(R.drawable.enable);//默认disable
        mFloatView.setTag(Integer.valueOf(R.drawable.enable));
        //设置监听浮动窗口的触摸移动
        mFloatView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                wmParams.x = (int) event.getRawX() - mFloatView.getMeasuredWidth() / 2;
                //Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredWidth()/2);
                // Log.i(TAG, "RawX" + event.getRawX());
                // Log.i(TAG, "X" + event.getX());
                //25为状态栏的高度
                wmParams.y = (int) event.getRawY() - mFloatView.getMeasuredHeight() / 2 - 25;
                // Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredHeight()/2);
                // Log.i(TAG, "RawY" + event.getRawY());
                // Log.i(TAG, "Y" + event.getY());
                //刷新
                mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                return false;
            }
        });

        mFloatView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                // Toast.makeText(FloatWindowActivity.this, "onClick", Toast.LENGTH_SHORT).show();
                int drawable = (Integer) mFloatView.getTag();
                switch(drawable) {
                    case R.drawable.enable:
                        mFloatView.setImageResource(R.drawable.disable);
                        mFloatView.setTag(Integer.valueOf(R.drawable.disable));
                        if(wechatPref!=null)
                            wechatPref.getOnPreferenceChangeListener().onPreferenceChange(wechatPref,false);
                        break;

                    case R.drawable.disable:
                        mFloatView.setImageResource(R.drawable.enable);
                        mFloatView.setTag(Integer.valueOf(R.drawable.enable));
                        if(wechatPref!=null)
                            wechatPref.getOnPreferenceChangeListener().onPreferenceChange(wechatPref,true);
                        break;

                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // 获取当前的菜单
        MenuInflater inflater = getMenuInflater();
        // 填充菜单
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    /**
     * 对菜单点击事件处理
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.open_assistant:
                showAssistant();
                break;
            case R.id.open_notice:
                showNotice();
                break;
            case R.id.about_me:
                showAboutMe();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 打开无障碍Intent
     */
    public void showAssistant() {
        Intent settings = new Intent
                (Settings.ACTION_ACCESSIBILITY_SETTINGS);
        settings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        startActivity(settings);
    }
    /**
     * 打开通知服务权Intent
     */
    public void showNotice() {
        Intent settings = new Intent
                (Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
        settings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        startActivity(settings);
    }
    /**
     * 打开关于微信机器人Intent
     */
    private void showAboutMe() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.about_me);
        TextView tv_title = (TextView) window.findViewById(R.id.tv_dialog_title);
        tv_title.setText(R.string.aboutme_title);
        TextView tv_message = (TextView) window.findViewById(R.id.tv_dialog_message);
        tv_message.setText(R.string.aboutme_text);
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(autoTalkingConnectReceiver);
        //mTipsDialog = null;
        autoTalkingConnectReceiver = null;
        if(mFloatLayout != null)//点击手机上的back键盘退出UI，不在后台运行（service后台运行），点击home键盘后台运行
            mWindowManager.removeView(mFloatLayout);
        wechatPref = null;
        wmParams = null;
        mFloatView = null;
        mFloatLayout = null;
        mWindowManager = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!AutoTalkingService.isRunning())
            showAssistant();
       /* if(QiangHongBaoService.isRunning()) {
            if(mTipsDialog != null) {
                mTipsDialog.dismiss();
            }
        } else {
            showOpenAccessibilityServiceDialog();
        }

        boolean isAgreement = Config.getConfig(this).isAgreement();
        if(!isAgreement) {
            showAgreementDialog();
        }*/
    }

}
