package com.huangyuanlove.liaoba.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.huangyuanlove.liaoba.R;

/**
 * Created
 * Author: huangyuan_xuan
 * Date: 2015/10/17
 */
public class ResponseURLViewActivity extends BaseActivity {


    private WebView webView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.response_url_view);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        String url = getIntent().getStringExtra("url");

        WebView webView = (WebView) findViewById(R.id.response_url_webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                progressDialog.dismiss();
            }
        });

        webView.loadUrl(url);
        progressDialog = ProgressDialog.show(this, null, "正在加载······");
    }

    public static void actionStart(Context context, String url) {
        Intent intent = new Intent(context, ResponseURLViewActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }
}
