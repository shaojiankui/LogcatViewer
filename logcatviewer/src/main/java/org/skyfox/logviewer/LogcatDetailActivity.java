package org.skyfox.logviewer;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.weijiaxing.logviewer.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class LogcatDetailActivity extends AppCompatActivity {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            "MM-dd hh:mm:ss.SSS", Locale.getDefault());
    private static final String CONTENT_TEMPLATE
            = "Time: %s\nPid: %d\nTid: %d\nPriority: %s\nTag: %s\n\nContent: \n%s";
    private ImageView copyButton;
    TextView contentTextView;
    public static void launch(Context context, LogItem log) {


        Intent log1 = new Intent(context, LogcatDetailActivity.class).putExtra("log", log);
        log1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(log1);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.parseColor("#1a1a1a"));
        }
        setContentView(R.layout.activity_logcat_detail);
        contentTextView = findViewById(R.id.content);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_close);
        setSupportActionBar(toolbar);
        copyButton = findViewById(R.id.copyButton);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        LogItem log = getIntent().getParcelableExtra("log");
        contentTextView.setText(String.format(Locale.getDefault(), CONTENT_TEMPLATE,
                DATE_FORMAT.format(log.time), log.processId, log.threadId, log.priority, log.tag,
                log.content));

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取剪贴版
                ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                //创建ClipData对象
                //第一个参数只是一个标记，随便传入。
                //第二个参数是要复制到剪贴版的内容
                ClipData clip = ClipData.newPlainText("logdetail", contentTextView.getText().toString());
                //传入clipdata对象.
                clipboard.setPrimaryClip(clip);
                Toast.makeText(LogcatDetailActivity.this, "Log已经复制到剪切板", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
