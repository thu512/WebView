package com.gsitm.webview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.gsitm.webview.model.Res;
import com.gsitm.webview.net.SNet;
import com.kakao.sdk.newtoneapi.SpeechRecognizerActivity;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity {

    private WebView mWebView;
    private WebSettings mWebSetting;
    private Button back;
    private Button front;
    private Button home;
    private Button refresh;
    private Button uibutton; //음성 인식
    private static final String SERVER_URL ="http://192.168.9.233:8082/main.do";

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

        // library를 초기화 합니다.
        // API를 사용할 시점이 되었을 때 initializeLibrary(Context)를 호출한다.
        // 사용을 마치면 finalizeLibrary()를 호출해야 한다.
        SpeechRecognizerManager.getInstance().initializeLibrary(this);


        back = (Button)findViewById(R.id.back);
        front = (Button)findViewById(R.id.front);
        home = (Button)findViewById(R.id.home);
        refresh = (Button)findViewById(R.id.refresh);
        uibutton = (Button)findViewById(R.id.uibutton);


        mWebView = (WebView)findViewById(R.id.webview);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;

            }

            /**
             * 웹페이지 로딩이 시작할 때 처리
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {


                showPd();
            }


            /**
             * 웹페이지 로딩중 에러가 발생했을때 처리
             */
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getApplicationContext(),"로딩에 실패하였습니다.",Toast.LENGTH_LONG).show();

            }

            /**
             * 웹페이지 로딩이 끝났을 때 처리
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                stopPd();
            }

        });

        mWebSetting = mWebView.getSettings();
        mWebSetting.setJavaScriptEnabled(true);
        mWebSetting.setLoadWithOverviewMode(true);
        mWebSetting.setBuiltInZoomControls(false);
        mWebSetting.setSupportZoom(true);
        mWebSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        mWebSetting.setDefaultZoom(WebSettings.ZoomDensity.FAR);


        mWebView.loadUrl(SERVER_URL);

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
            mWebView.loadUrl(SERVER_URL);
        });

        //새로고침 버튼 클릭
        refresh.setOnClickListener((view) -> {
            mWebView.reload();
        });

        //음성인식
        uibutton.setOnClickListener((view) -> {
            String serviceType = SpeechRecognizerClient.SERVICE_TYPE_WEB;
            String apikey = "b5533584fb6e3ea4f2cff528d9e0eb0e";
            Intent i = new Intent(getApplicationContext(), VoiceRecoActivity.class);

            i.putExtra(SpeechRecognizerActivity.EXTRA_KEY_SERVICE_TYPE, serviceType);
            i.putExtra(SpeechRecognizerActivity.EXTRA_KEY_API_KEY, apikey);
            startActivityForResult(i, 0);
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            ArrayList<String> results = data.getStringArrayListExtra(VoiceRecoActivity.EXTRA_KEY_RESULT_ARRAY);


            //음성인식 완료 여기서 처리!!!
            if(data.getBooleanExtra(VoiceRecoActivity.EXTRA_KEY_MARKED,false)){

                String voice = results.get(0);
                if(voice.indexOf("회의실") > -1){
                    //서버로 전송
                    postVoice("회의실");
                }else if(voice.indexOf("교육실") > -1) {
                    postVoice("교육실");
                }else{
                    new AlertDialog.Builder(this).
                            setMessage("회의실 또는 교육실을 정확히 말씀해주세요.").
                            setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).
                            show();
                }

            }else{
                new AlertDialog.Builder(this).
                        setMessage("정확히 말씀해주세요.").
                        setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).
                        show();
            }

        }
        else if (requestCode == RESULT_CANCELED) {
            // 음성인식의 오류 등이 아니라 activity의 취소가 발생했을 때.
            if (data == null) {
                return;
            }

            int errorCode = data.getIntExtra(VoiceRecoActivity.EXTRA_KEY_ERROR_CODE, -1);
            String errorMsg = data.getStringExtra(VoiceRecoActivity.EXTRA_KEY_ERROR_MESSAGE);

            if (errorCode != -1 && !TextUtils.isEmpty(errorMsg)) {
                new AlertDialog.Builder(this).
                        setMessage(errorMsg).
                        setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).
                        show();
            }
        }
    }

    public void postVoice(String voice) {
        showPd();

        Call<Res> res = SNet.getInstance().getAllFactoryIm().voiceRecog(voice);
        res.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d("VOICE","성공 " + response.body().toString());
                        Log.d("VOICE","성공 " + response.body().getData());

                        ///여기서 링크를 받아서 webview에 뿌려주기
                        mWebView.loadUrl(response.body().getData());
                    } else {
                        Log.d("VOICE","통신실패1 " + response.body().toString());
                    }
                } else {
                    try {
                        Log.d("VOICE","통신실패2 " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                stopPd();
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {
                Log.d("VOICE","통신실패3 " + t.getLocalizedMessage());
                stopPd();
            }
        });
    }

}
