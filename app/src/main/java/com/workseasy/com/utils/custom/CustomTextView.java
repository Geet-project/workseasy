package com.workseasy.com.utils.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.hr.hrmanagement.R;


public class CustomTextView extends AppCompatTextView {
    String customFont;

    public CustomTextView(Context context) {
        this(context, null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        int cf = a.getInteger(R.styleable.CustomTextView_font, 0);
        int fontName = 0;
        switch (cf) {
            case 1:
                fontName = R.string.Poppins_Black;
                break;
            case 2:
                fontName = R.string.Poppins_Bold;
                break;
            case 3:
                fontName = R.string.Poppins_ExtraBold;
                break;
            case 4:
                fontName = R.string.Poppins_Medium;
                break;
            case 5:
                fontName = R.string.Poppins_Regular;
                break;
            case 6:
                fontName = R.string.Poppins_Semi_Bold;
                break;
            default:
                fontName = R.string.Poppins_Regular;
                break;
        }
        customFont = getResources().getString(fontName);

        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/" + customFont + ".ttf");
        setTypeface(tf);

        a.recycle();
    }
}
