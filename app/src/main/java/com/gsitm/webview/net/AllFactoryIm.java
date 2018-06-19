package com.gsitm.webview.net;

import com.gsitm.webview.model.Res;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 통신API를 선언한다. 구현X
 */

public interface AllFactoryIm {




    @GET("voiceRecog.do")
    Call<Res> voiceRecog(@Query("voice") String voice);



}
