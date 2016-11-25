package com.lester.uparent;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.baidu.mobstat.StatService;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.Appinfo;

public class Aboutour  extends Activity implements OnClickListener{
   
	@Override
	protected void onResume() {
		super.onResume();
	 StatService.onResume(this);
	}
	@Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aboutour);
		findViewById(R.id.back).setOnClickListener(this);;
		WebView webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.getSettings().setSupportZoom(Boolean.TRUE);
        webView.getSettings().supportMultipleWindows();
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setScrollbarFadingEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(true);
        webView.setFocusable(false);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.startsWith("tel:")||url.startsWith("sms:")||url.startsWith("mailto:")){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url)); 
                    startActivity(intent);
                    return true;
                }
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
            }
        });
        String templet = getFileContent("about.html");
        Context context = getApplicationContext();
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = null;
        String versionName = "V1.0.0";
        try {
            pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            versionName= pi.versionName;
        } catch (NameNotFoundException e) {
        }
        String html = templet.replace("${{verson}}", versionName);
        webView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);
    }

    private String getFileContent(String file) {
        String content = "";
        try {
            InputStream is = getResources().getAssets().open(file);
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int i = is.read(buffer, 0, buffer.length);
            while (i > 0) {
                bs.write(buffer, 0, i);
                i = is.read(buffer, 0, buffer.length);
            }
            content = new String(bs.toByteArray(), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return content;
    }

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		}
	}
}