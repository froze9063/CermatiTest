package id.testing.cermatitest.VIew;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.core.content.res.ResourcesCompat;

import id.testing.cermatitest.R;

public class MyTextView extends androidx.appcompat.widget.AppCompatTextView {

    public MyTextView(Context context) {
        super(context);
        Typeface typeface = myTypeFace(context,0);

        if (typeface != null){
            this.setTypeface(typeface);
        }
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.applyCustomFont(context,attrs);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.applyCustomFont(context,attrs);
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
    }

    private void applyCustomFont(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes( attrs, R.styleable.MyTextView,
                0, 0);
        int mTextPos = 0;
        try {
            mTextPos = a.getInteger(R.styleable.MyTextView_myFontStyle, 0);
        } finally {
            a.recycle();
        }

        Typeface typeface = myTypeFace(context,mTextPos);

        if (typeface != null){
            this.setTypeface(typeface);
        }
    }

    private Typeface myTypeFace(Context context, int type){
        Typeface typeface = null;

        if (android.os.Build.VERSION.SDK_INT >= 26 ){
            switch (type) {
                case 0 : typeface = context.getResources().getFont(R.font.nunito_sans_reguler);
                break;

                case 1 : typeface = context.getResources().getFont(R.font.nunito_sans_semibold);
                    break;

                case 2 : typeface = context.getResources().getFont(R.font.nunito_sans_bold);
                    break;

                case 3 : typeface = context.getResources().getFont(R.font.rubik_reguler);
                    break;

                case 4 : typeface = context.getResources().getFont(R.font.rubik_medium);
                    break;

                case 5 : typeface = context.getResources().getFont(R.font.rubik_bold);
                    break;
            }

        }
        else{
            switch (type){
                case 0: typeface = ResourcesCompat.getFont(context, R.font.nunito_sans_reguler);
                break;

                case 1: typeface = ResourcesCompat.getFont(context, R.font.nunito_sans_semibold);
                    break;

                case 2: typeface = ResourcesCompat.getFont(context, R.font.nunito_sans_bold);
                    break;

                case 3: typeface = ResourcesCompat.getFont(context, R.font.rubik_reguler);
                    break;

                case 4: typeface = ResourcesCompat.getFont(context, R.font.rubik_medium);
                    break;

                case 5: typeface = ResourcesCompat.getFont(context, R.font.rubik_bold);
                    break;
            }
        }

        return typeface;
    }
}


