package com.ishow.ischool.business.Input;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ishow.ischool.R;
import com.ishow.ischool.common.base.BaseActivity4Crm;

import butterknife.BindView;

/**
 * Created by MrS on 2016/8/25.
 */
public class InputActivity extends BaseActivity4Crm implements TextWatcher {
    @BindView(R.id.input)
    EditText input;
    private MenuItem item;

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_input, "", R.menu.menu_addstudent, MODE_NONE);
    }

    @Override
    protected void setUpView() {
        String title = getIntent().getStringExtra("title");
        mToolbar = (Toolbar) findViewById(com.commonlib.R.id.toolbar);
        mToolbarTitle = (TextView) findViewById(com.commonlib.R.id.toolbar_title);
        mToolbarTitle.setText(title);
        mToolbar.setNavigationIcon(com.commonlib.R.drawable.ic_return);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });

        setUpMenu(R.menu.menu_addstudent);
        item = mToolbar.getMenu().findItem(R.id.submit);
        input.setHint(title);
        input.addTextChangedListener(this);
    }

    private void finishActivity() {
        Intent intent = new Intent();
        intent.putExtra("result",input.getText().toString().trim());
        setResult(RESULT_OK, intent);
        this.finish();
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        finishActivity();
        return super.onMenuItemClick(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishActivity();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.equals(s.toString(), ""))
            item.setEnabled(false);
        else item.setEnabled(true);
    }
}
