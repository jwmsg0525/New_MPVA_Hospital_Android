package kr.jwmsg.new_mpva.activities;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import kr.jwmsg.new_mpva.R;

public class SettingActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_setting );
    }
}
