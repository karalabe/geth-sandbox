package org.ethereum.droidwallet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import org.ethereum.geth.Geth;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            public void run() {
                Geth.run("--ipcdisable --rpc --rpccorsdomain=* --fast --datadir=" + getFilesDir().getAbsolutePath());
            }
        }).start();

        final WebView wallet = (WebView) findViewById(R.id.webView);
        wallet.getSettings().setJavaScriptEnabled(true);
        wallet.getSettings().setAllowUniversalAccessFromFileURLs(true);

        Executors.newSingleThreadScheduledExecutor().schedule(new Runnable() {
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        wallet.loadUrl("file:///android_asset/wallet/index.html");
                    }
                });
            }
        }, 5, TimeUnit.SECONDS);
    }
}
