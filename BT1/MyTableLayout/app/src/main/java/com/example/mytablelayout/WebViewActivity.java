package com.example.mytablelayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  sử dụng layout webview
        setContentView(R.layout.webview);

        //  lấy cái intent mà bắt đầu cái activity này để khi bấm nút trở về
        //  thì ứng dụng trở về trạng thái trước khi bấm đi tới trang MOMA
        //  intent nhận biết cái activity nào làm chuyện đó thông qua "key"
        Intent intent = getIntent();

        // gán cái key đó vào một chuỗi
        String value = intent.getStringExtra("key");

        //  tạo một cái webview mới (là gì thì đã có giải thích ở dòng 148 MainActivity)
        WebView webView = (WebView) findViewById(R.id.webview);

        //  sử dụng nó để load url cần đi đến
        webView.loadUrl("https://www.moma.org/");
    }
}
