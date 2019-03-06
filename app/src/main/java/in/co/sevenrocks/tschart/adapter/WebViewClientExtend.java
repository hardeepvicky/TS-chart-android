package in.co.sevenrocks.tschart.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Created on 02-Oct-17.
 */

public class WebViewClientExtend extends WebViewClient {

    ProgressBar progressBar;
    private Context context;

    public WebViewClientExtend(Context context, ProgressBar progressBar)
    {
        this.context = context;
        this.progressBar = progressBar;
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // TODO Auto-generated method stub
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        // TODO Auto-generated method stub
        super.onPageFinished(view, url);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        Toast.makeText(context, "Error : " + description, Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.INVISIBLE);
    }
}