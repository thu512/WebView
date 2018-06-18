package com.gsitm.webview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    private WebSettings mWebSetting;
    private Button back;
    private Button front;
    private Button home;
    private Button refresh;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode == KeyEvent.KEYCODE_BACK)&&mWebView.canGoBack()){
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        back = (Button)findViewById(R.id.back);
        front = (Button)findViewById(R.id.front);
        home = (Button)findViewById(R.id.home);
        refresh = (Button)findViewById(R.id.refresh);


        mWebView = (WebView)findViewById(R.id.webview);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());

        mWebSetting = mWebView.getSettings();
        mWebSetting.setJavaScriptEnabled(true);
        mWebSetting.setLoadWithOverviewMode(true);
        mWebSetting.setBuiltInZoomControls(false);
        mWebSetting.setSupportZoom(true);
        mWebSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        mWebSetting.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        mWebView.loadUrl("http://10.0.2.2:8087/main.do");

        //뒤로가기 버튼 클릭
        back.setOnClickListener((view) -> {
            mWebView.goBack();
        });

        //앞으로가기 버튼 클릭
        front.setOnClickListener((view) -> {
            mWebView.goForward();
        });

        //홈으로 버튼 클릭
        home.setOnClickListener((view) -> {
            mWebView.loadUrl("http://192.168.9.233:8087/main.do");
        });

        //새로고침 버튼 클릭
        refresh.setOnClickListener((view) -> {
            mWebView.reload();
        });


    }
}
