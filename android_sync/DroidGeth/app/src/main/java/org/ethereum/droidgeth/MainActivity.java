package org.ethereum.droidgeth;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ethereum.go_ethereum.cmd.Geth;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button      start    = (Button) findViewById(R.id.button);
        final TextView    label    = (TextView) findViewById(R.id.label);
        final ProgressBar progress = (ProgressBar) findViewById(R.id.progress);

        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        Geth.run("--ipcdisable --rpc --fast --datadir=" + getFilesDir().getAbsolutePath());
                    }
                }).start();
                new Thread(new Runnable() {
                    public void run() {
                        while (true) {
                            try {
                                URL url = new URL("http://127.0.0.1:8545/");
                                byte[] req = "{\"jsonrpc\":\"2.0\",\"method\":\"eth_syncing\",\"params\":[],\"id\":1}".getBytes(StandardCharsets.UTF_8);;

                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.setRequestMethod("POST");
                                connection.setRequestProperty("Content-Length", Integer.toString(req.length));
                                try (DataOutputStream out = new DataOutputStream(connection.getOutputStream())) {
                                    out.write(req);
                                }
                                try {
                                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                                    StringBuilder buffer = new StringBuilder();

                                    String line;
                                    while ((line = in.readLine()) != null){
                                        buffer.append(line);
                                    }
                                    final JSONObject reply = new JSONObject(buffer.toString());
                                    if (reply.getString("result") == "false") {
                                        handler.post(new Runnable() {
                                            public void run() {
                                                label.setText("Geth not syncing...");
                                            }
                                        });
                                    } else {
                                        handler.post(new Runnable() {
                                            public void run() {
                                                try {
                                                    final Integer startingBlock = Integer.decode(reply.getJSONObject("result").getString("startingBlock"));
                                                    final Integer currentBlock = Integer.decode(reply.getJSONObject("result").getString("currentBlock"));
                                                    final Integer highestBlock = Integer.decode(reply.getJSONObject("result").getString("highestBlock"));

                                                    label.setText("Geth processing block #" + currentBlock.toString());
                                                    progress.setProgress(100 * (currentBlock-startingBlock)/(highestBlock-startingBlock));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                }
                                finally{
                                    connection.disconnect();
                                }
                                Thread.sleep(1000);
                            } catch (Exception e) {}
                        }
                    }
                }).start();
            }
        });
    }
}
