package com.leelay.demo.passwordinputview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 订房宝安全码控件
 * Created by lilei on 2016/6/14.
 */
public class PasswordInputView extends FrameLayout implements TextWatcher {

    private EditText mHintEditText;

    private int maxLength;

    private static final int DEFULT_MAX_LENGTH = 4;
    private LinearLayout linearLayout;

    private String mCurrentText;
    private String mBeforeText;

    public PasswordInputView(Context context) {
        super(context);
        init();
    }


    public PasswordInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PasswordInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //添加隐藏的EditText
        mHintEditText = new HindEditText(getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mHintEditText, params);
        mHintEditText.addTextChangedListener(this);
        //添加LinearLayout
        linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        this.addView(linearLayout, params);
        //初始化长度
        setMaxLength(DEFULT_MAX_LENGTH);
    }
    public void clearText(){
        if (mHintEditText!=null){
            mHintEditText.setText("");
        }
    }
    private void initChildView() {
        linearLayout.removeAllViews();
        for (int i = 0; i < maxLength; i++) {
            ItemView textView = new ItemView(getContext());
            textView.setIncludeFontPadding(false);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(0, 0, 0, 0);
            textView.setTextSize(36);
            textView.setTextColor(Color.parseColor("#fbbe04"));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.weight = 1;
            linearLayout.addView(textView, layoutParams);
        }
    }

    public void setMaxLength(int maxLength) {
        if (maxLength < 1) {
            return;
        }
        this.maxLength = maxLength;
        mHintEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        initChildView();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.e("beforeTextChanged", "CharSequence: " + s);
        mBeforeText = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mCurrentText = s.toString();
        Log.e("onTextChanged", "CharSequence: " + s);
    }

    @Override
    public void afterTextChanged(Editable s) {
        int cLength = mCurrentText.length();
        int bLength = mBeforeText.length();
        //增加数字了
        if (cLength > bLength) {
            if (cLength > maxLength) {
                return;
            }
            for (int i = bLength; i < cLength; i++) {
                ItemView childAt = (ItemView) linearLayout.getChildAt(i);
                String substring = mCurrentText.substring(i, i + 1);
                childAt.setWord(substring);
            }
        }
        //删除数字了
        else if (cLength < bLength) {
            if (bLength > maxLength) {
                return;
            }
            for (int i = cLength; i < bLength; i++) {
                ItemView childAt = (ItemView) linearLayout.getChildAt(i);
                childAt.clear();
            }
        }
        if (cLength > maxLength - 1) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (onInputCallback != null) {
                        onInputCallback.onInputComplete(mCurrentText);
                    }
                    mHintEditText.setText("");
                }
            }, 300);
        }
    }


    public static class HindEditText extends EditText {

        public HindEditText(Context context) {
            super(context);
            init();
        }

        public HindEditText(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public HindEditText(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        @Override
        protected void onDraw(Canvas canvas) {
        }

        private void init() {
            setBackgroundDrawable(null);
            setInputType(InputType.TYPE_CLASS_NUMBER);
            setCursorVisible(false);
        }
    }

    public static class ItemView extends ImageTextView {

        private boolean stopRunnable = false;

        public ItemView(Context context) {
            super(context);
            init();
        }

        public ItemView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();

        }

        private void init() {

        }

        public void setWord(String word) {
            if (TextUtils.isEmpty(word)) {
                clear();
                return;
            }
            hindStar();
            setBackgroundRes(R.drawable.bg_password_input_selected);
            setText(word);
            stopRunnable = false;
            postDelayed(runnable, 200);
        }

        public void clear() {
            stopRunnable = true;
            hindStar();
            setBackgroundRes(R.drawable.bg_password_input_narmal);
            setText("");
        }

        public Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (!stopRunnable) {
                    showStar();
                    setBackgroundRes(R.drawable.bg_password_input_selected);
                    setText("");
                }
            }
        };

        @Override
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            stopRunnable = true;
        }
    }

    public static class ImageTextView extends TextView {
        private int mWidth;
        private int mHeight;
        private Bitmap mBackgroundBitmap;
        private Bitmap mStarBitmap;
        private boolean hindStar = true;

        private static final int DEFULT_BACKGROUND_RES = R.drawable.bg_password_input_narmal;
        private static final int DEFULT_STAR_RES = R.drawable.img_password_input_star;

        public ImageTextView(Context context) {
            super(context);
            init();
        }

        public ImageTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public ImageTextView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        private void init() {

        }

        @Override
        protected void onDraw(Canvas canvas) {
            drawBackground(canvas);
            super.onDraw(canvas);
            if (!hindStar) {
                drawStar(canvas);
            }
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mWidth = w;
            mHeight = h;
            setForegrounndRes(DEFULT_STAR_RES);
            setBackgroundRes(DEFULT_BACKGROUND_RES);
        }

        public void setForegrounndRes(int res) {
            if (res > 0) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeResource(getResources(), res, options);
                options.inSampleSize = calculateInSampleSize(options, mWidth, mHeight);
                options.inJustDecodeBounds = false;
                mStarBitmap = BitmapFactory.decodeResource(getResources(), res, options);
            }
        }

        public void setBackgroundRes(int res) {
            if (res > 0) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeResource(getResources(), res, options);
                options.inSampleSize = calculateInSampleSize(options, mWidth, mHeight);
                options.inJustDecodeBounds = false;
                mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), res, options);
            }
        }

        private void drawStar(Canvas canvas) {
            if (mStarBitmap != null) {
                int left = getPaddingLeft() + ((mWidth - getPaddingLeft() - getPaddingRight()) - mStarBitmap.getWidth()) / 2;
                int top = getPaddingTop() + ((mHeight - getPaddingTop() - getPaddingBottom()) - mStarBitmap.getHeight()) / 2;
                canvas.drawBitmap(mStarBitmap, left, top, null);
            }
        }

        private void drawBackground(Canvas canvas) {
            if (mBackgroundBitmap != null) {
                int left = getPaddingLeft() + ((mWidth - getPaddingLeft() - getPaddingRight()) - mBackgroundBitmap.getWidth()) / 2;
                int top = getPaddingTop() + ((mHeight - getPaddingTop() - getPaddingBottom()) - mBackgroundBitmap.getHeight()) / 2;
                canvas.drawBitmap(mBackgroundBitmap, left, top, null);
            }
        }


        public static int calculateInSampleSize(
                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // 原始图片的宽高
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;
            if (height > reqHeight || width > reqWidth) {
                final int halfHeight = height / 2;
                final int halfWidth = width / 2;
                // 在保证解析出的bitmap宽高分别大于目标尺寸宽高的前提下，取可能的inSampleSize的最大值
                while ((halfHeight / inSampleSize) > reqHeight
                        && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }
            return inSampleSize;
        }

        public void hindStar() {
            hindStar = true;
            postInvalidate();
        }

        public void showStar() {
            hindStar = false;
            postInvalidate();
        }
    }

    public interface OnInputCallback {

        void onInputComplete(String inputText);
    }

    private OnInputCallback onInputCallback;

    public void setOnInputCallback(OnInputCallback onInputCallback) {
        this.onInputCallback = onInputCallback;
    }
}
