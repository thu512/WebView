package com.gsitm.webview;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import static java.lang.Thread.sleep;


public class SplashActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_AUDIO_AND_WRITE_EXTERNAL_STORAGE =1060;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //여기서 권한 받기
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_AUDIO_AND_WRITE_EXTERNAL_STORAGE);
            } else {
                // 유저가 거부하면서 다시 묻지 않기를 클릭.. 권한이 없다고 유저에게 직접 알림.
                Toast.makeText(this, "앱 실행을 위해서는 권한을 설정해야 합니다",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            try

            {
                sleep(800);
            } catch (
                    Exception e)

            {

            }

            Intent intent = new Intent(this, MainActivity.class);

            startActivity(intent);

            finish();
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_AUDIO_AND_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    try

                    {
                        sleep(800);
                    } catch (
                            Exception e)

                    {

                    }

                    Intent intent = new Intent(this, MainActivity.class);

                    startActivity(intent);

                    finish();
                } else {
                    finish();
                }
                break;
            default:
                break;
        }
    }

}