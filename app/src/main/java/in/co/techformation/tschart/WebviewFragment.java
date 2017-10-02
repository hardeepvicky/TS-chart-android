package in.co.techformation.tschart;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

import in.co.techformation.tschart.adapter.WebViewClientExtend;

public class WebviewFragment extends Fragment {

    WebView webview;
    ProgressBar progressBar;

    public WebviewFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_webview, container, false);

        String url = getArguments().getString("url");

        webview = (WebView) view.findViewById(R.id.webview);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClientExtend(getContext(), progressBar));
        webview.loadUrl(url);

        return view;
    }
}
