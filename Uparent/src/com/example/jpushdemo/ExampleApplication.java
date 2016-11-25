package com.example.jpushdemo;

import java.util.Set;

import com.baidu.mobstat.SendStrategyEnum;
import com.baidu.mobstat.StatService;
import com.lester.crash.CrashHandler;

import android.app.Application;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * For developer startup JPush SDK
 * 
 * 一般建议在自定义 Application 类里初始化。也可以在主 Activity 里。
 */
public class ExampleApplication extends Application {
    private static final String TAG = "JPush";
    public static final boolean SHIELD_EXCEPTION = false;
    
    
    @Override
    public void onCreate() {    	     
    	 Log.d(TAG, "[ExampleApplication] onCreate");
         super.onCreate();
         CrashHandler crashHandler = CrashHandler.getInstance();  
         crashHandler.init(getApplicationContext());  
         
         JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
         JPushInterface.init(this);     		// 初始化 JPush
         
    }
    
 
	// 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般  App 的设置的调用入口，在任何方便的地方调用都可以。 
	public void setAlias( String alias ) {
	    if (TextUtils.isEmpty(alias)) {
	        return;
	    }
	    if (!ExampleUtil.isValidTagAndAlias(alias)) {
	        return;
	    }
	    // 调用 Handler 来异步设置别名
	    mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
	}
	private static final int MSG_SET_ALIAS = 1001;
	private final Handler mHandler = new Handler() {
	@Override
	    public void handleMessage(android.os.Message msg) {
	        super.handleMessage(msg);
	        switch (msg.what) {
	            case MSG_SET_ALIAS:
	                Log.d(TAG, "Set alias in handler.");
	                // 调用 JPush 接口来设置别名。
	                JPushInterface.setAliasAndTags(getApplicationContext(),
	                                                (String) msg.obj, 
	                                                 null,
	                                                 mAliasCallback);
	            break;
	        default:
	            Log.i(TAG, "Unhandled msg - " + msg.what);
	        }
	    }                                       
	};
	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
	    @Override
	    public void gotResult(int code, String alias, Set<String> tags) {
	        String logs ;
	        switch (code) {
	        case 0:
	            logs = "Set tag and alias success";
	            Log.i(TAG, logs);
	            // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
	            break;
	        case 6002:
	            logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
	            Log.i(TAG, logs);
	            // 延迟 60 秒来调用 Handler 设置别名
	            mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
	            break;
	        default:
	            logs = "Failed with errorCode = " + code;
	            Log.e(TAG, logs);
	        }
//	        ExampleUtil.showToast(logs, getApplicationContext());
	    }
	};
}
