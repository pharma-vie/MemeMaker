package com.dinosilvestro.mememaker;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

public class InputTextWatcher implements TextWatcher {

    private TextView mTextView;

    public InputTextWatcher(TextView textView) {
        mTextView = textView;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        mTextView.setVisibility(editable.length() > 0 ? View.VISIBLE : View.INVISIBLE);
        mTextView.setText(editable.toString());
    }
}