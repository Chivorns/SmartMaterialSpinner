package com.chivorn.smartmaterialspinner;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.chivorn.smartmaterialspinner.util.SoftKeyboardUtil;

import java.util.ArrayList;
import java.util.List;

public class SmartMaterialSpinner<T> extends AppCompatSpinner implements ValueAnimator.AnimatorUpdateListener, SearchableSpinnerDialog.SearchableItem {
    public static final int DEFAULT_ARROW_WIDTH_DP = 10;
    private static final String TAG = SmartMaterialSpinner.class.getSimpleName();

    //Paint objects
    private Paint paint;
    private TextPaint textPaint;
    private StaticLayout staticLayout;

    private SearchableSpinnerDialog searchableSpinnerDialog;
    private List<T> items;
    private List<Object> searchDialogItem;

    private boolean isSearchable = false;
    private boolean isEnableSearchHeader = true;
    private String searchHeaderText;
    private String searchHint;

    private Path selectorPath;
    private Point[] selectorPoints;

    //Inner padding = "Normal" android padding
    private int innerPaddingLeft;
    private int innerPaddingRight;
    private int innerPaddingTop;
    private int innerPaddingBottom;

    //Private padding to add space for FloatingLabel and ErrorLabel
    private int extraPaddingTop;
    private int extraPaddingBottom;

    //@see dimens.xml
    private int underlineTopSpacing;
    private int underlineBottomSpacing;
    private int errorLabelSpacing;
    private int floatingLabelTopSpacing;
    private int floatingLabelBottomSpacing;
    private int floatingLabelInsideSpacing;
    private int rightLeftSpinnerPadding;
    private int minContentHeight;

    //Properties about Error Label
    private int lastPosition;
    private ObjectAnimator errorLabelAnimator;
    private int errorLabelPosX;
    private int minNbErrorLines;
    private float currentNbErrorLines;


    //Properties about Floating Label (
    private float floatingLabelPercent;
    private ObjectAnimator floatingLabelAnimator;
    private boolean isSelected;
    private boolean floatingLabelVisible;
    private int baseAlpha;


    //AttributeSet
    private int baseColor;
    private int highlightColor;
    private float errorTextSize;
    private int errorColor;
    private int disabledColor;
    private int underlineColor;
    private float underlineSize;
    private CharSequence error;
    private CharSequence hint;
    private int hintColor;
    private int itemListHintColor;
    private int itemListHintBackground;
    private float itemSize;
    private int itemColor;
    private int itemListColor;
    private int selectedItemColor;
    private float hintTextSize;
    private CharSequence floatingLabelText;
    private float floatingLabelSize;
    private int floatingLabelColor;
    private boolean multilineError;
    private Typeface typeface;
    private boolean alignLabels;
    private int arrowColor;
    private float arrowSize;
    private boolean enableErrorLabel;
    private boolean enableFloatingLabel;
    private boolean alwaysShowFloatingLabel;
    private boolean isRtl;
    private boolean isShowEmptyDropdown;

    private HintAdapter hintAdapter;
    private TextView tvSpinnerItem;

    //Default hint views
    private Integer mDropdownView;
    private Integer mHintView;

    private OnEmptySpinnerClickListener onEmptySpinnerClickListener;

    /*
     * **********************************************************************************
     * CONSTRUCTORS
     * **********************************************************************************
     */

    public SmartMaterialSpinner(Context context) {
        super(context);
        init(context, null);
    }

    public SmartMaterialSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    public SmartMaterialSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    /*
     * **********************************************************************************
     * INITIALISATION METHODS
     * **********************************************************************************
     */

    private void init(Context context, AttributeSet attrs) {
        initSearchableDialogObject();
        initAttributes(context, attrs);
        initPaintObjects();
        initDimensions();
        initPadding();
        initFloatingLabelAnimator();
        initOnItemSelectedListener();
        configSearchableDialog();
        setMinimumHeight(getPaddingTop() + getPaddingBottom() + minContentHeight);
        setBackgroundResource(R.drawable.smart_material_spinner_background);
        setItems(new ArrayList<T>());
        configDropdownWidthAfterAttrReady();
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray defaultArray = context.obtainStyledAttributes(new int[]{R.attr.colorControlNormal, R.attr.colorAccent});
        int defaultBaseColor = ContextCompat.getColor(context, R.color.smsp_base_color); // defaultArray.getColor(1, 0);
        int defaultHighlightColor = ContextCompat.getColor(context, R.color.smsp_base_color);
        int defaultErrorColor = ContextCompat.getColor(context, R.color.smsp_error_color);
        defaultArray.recycle();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SmartMaterialSpinner);
        baseColor = typedArray.getColor(R.styleable.SmartMaterialSpinner_smsp_baseColor, defaultBaseColor);
        highlightColor = typedArray.getColor(R.styleable.SmartMaterialSpinner_smsp_highlightColor, defaultHighlightColor);
        errorTextSize = typedArray.getDimension(R.styleable.SmartMaterialSpinner_smsp_errorTextSize, getResources().getDimension(R.dimen.smsp_default_error_text_size));
        errorColor = typedArray.getColor(R.styleable.SmartMaterialSpinner_smsp_errorColor, defaultErrorColor);
        disabledColor = ContextCompat.getColor(context, R.color.smsp_disabled_color);
        underlineColor = typedArray.getColor(R.styleable.SmartMaterialSpinner_smsp_underlineColor, ContextCompat.getColor(context, R.color.smsp_underline_color));
        error = typedArray.getString(R.styleable.SmartMaterialSpinner_smsp_error);
        hint = typedArray.getString(R.styleable.SmartMaterialSpinner_smsp_hint);
        floatingLabelText = typedArray.getString(R.styleable.SmartMaterialSpinner_smsp_floatingLabelText);
        hintColor = typedArray.getColor(R.styleable.SmartMaterialSpinner_smsp_hintColor, ContextCompat.getColor(context, R.color.smsp_hint_color));
        itemListHintColor = typedArray.getColor(R.styleable.SmartMaterialSpinner_smsp_itemListHintColor, ContextCompat.getColor(context, R.color.smsp_item_list_hint_color));
        itemListHintBackground = typedArray.getColor(R.styleable.SmartMaterialSpinner_smsp_itemListHintBackgroundColor, ContextCompat.getColor(context, R.color.smsp_item_list_hint_background));
        itemSize = typedArray.getDimension(R.styleable.SmartMaterialSpinner_smsp_itemSize, getResources().getDimension(R.dimen.smsp_default_text_and_hint_size));
        itemColor = typedArray.getColor(R.styleable.SmartMaterialSpinner_smsp_itemColor, Color.BLACK);
        itemListColor = typedArray.getColor(R.styleable.SmartMaterialSpinner_smsp_itemListColor, Color.BLACK);
        selectedItemColor = typedArray.getColor(R.styleable.SmartMaterialSpinner_smsp_selectedItemColor, ContextCompat.getColor(context, R.color.smsp_selected_color));
        hintTextSize = typedArray.getDimension(R.styleable.SmartMaterialSpinner_smsp_hintTextSize, getResources().getDimension(R.dimen.smsp_default_hint_size));
        floatingLabelSize = typedArray.getDimension(R.styleable.SmartMaterialSpinner_smsp_floatingLabelSize, getResources().getDimension(R.dimen.smsp_default_floating_label_size));
        floatingLabelColor = typedArray.getColor(R.styleable.SmartMaterialSpinner_smsp_floatingLabelColor, ContextCompat.getColor(context, R.color.smsp_floating_label_color));
        multilineError = typedArray.getBoolean(R.styleable.SmartMaterialSpinner_smsp_multilineError, true);
        minNbErrorLines = typedArray.getInt(R.styleable.SmartMaterialSpinner_smsp_nbErrorLines, 1);
        alignLabels = typedArray.getBoolean(R.styleable.SmartMaterialSpinner_smsp_alignLabels, true);
        underlineSize = typedArray.getDimension(R.styleable.SmartMaterialSpinner_smsp_underlineSize, 0.6f);
        arrowColor = typedArray.getColor(R.styleable.SmartMaterialSpinner_smsp_arrowColor, baseColor);
        arrowSize = typedArray.getDimension(R.styleable.SmartMaterialSpinner_smsp_arrowSize, dpToPx(DEFAULT_ARROW_WIDTH_DP));
        enableErrorLabel = typedArray.getBoolean(R.styleable.SmartMaterialSpinner_smsp_enableErrorLabel, true);
        enableFloatingLabel = typedArray.getBoolean(R.styleable.SmartMaterialSpinner_smsp_enableFloatingLabel, true);
        alwaysShowFloatingLabel = typedArray.getBoolean(R.styleable.SmartMaterialSpinner_smsp_alwaysShowFloatingLabel, false);
        isRtl = typedArray.getBoolean(R.styleable.SmartMaterialSpinner_smsp_isRtl, false);
        mHintView = typedArray.getResourceId(R.styleable.SmartMaterialSpinner_smsp_hintView, R.layout.smart_material_spinner_hint_item_layout);
        mDropdownView = typedArray.getResourceId(R.styleable.SmartMaterialSpinner_smsp_dropdownView, R.layout.smart_material_spinner_dropdown_item);
        isShowEmptyDropdown = typedArray.getBoolean(R.styleable.SmartMaterialSpinner_smsp_showEmptyDropdown, false);
        isSearchable = typedArray.getBoolean(R.styleable.SmartMaterialSpinner_smsp_isSearchable, false);
        isEnableSearchHeader = typedArray.getBoolean(R.styleable.SmartMaterialSpinner_smsp_isEnableSearchHeader, true);
        searchHeaderText = typedArray.getString(R.styleable.SmartMaterialSpinner_smsp_searchHeaderText);
        int searchHeaderDrawableResId = typedArray.getResourceId(R.styleable.SmartMaterialSpinner_smsp_searchHeaderColor, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && searchHeaderDrawableResId != 0) {
            setSearchHeaderBackground(AppCompatResources.getDrawable(getContext(), searchHeaderDrawableResId));
        } else {
            setSearchHeaderBackground(typedArray.getColor(R.styleable.SmartMaterialSpinner_smsp_searchHeaderColor, ContextCompat.getColor(context, R.color.smsp_search_header_background)));
        }
        searchHint = typedArray.getString(R.styleable.SmartMaterialSpinner_smsp_searchHint);

        String typefacePath = typedArray.getString(R.styleable.SmartMaterialSpinner_smsp_typeface);
        if (typefacePath != null) {
            typeface = Typeface.createFromAsset(getContext().getAssets(), typefacePath);
        }

        typedArray.recycle();

        floatingLabelPercent = 0f;
        errorLabelPosX = 0;
        isSelected = false;
        floatingLabelVisible = false;
        lastPosition = -1;
        currentNbErrorLines = minNbErrorLines;
    }

    private void initSearchableDialogObject() {
        searchDialogItem = new ArrayList<>();
        searchableSpinnerDialog = SearchableSpinnerDialog.newInstance(searchDialogItem);
        searchableSpinnerDialog.setOnSearchItemSelectedListener(this);
    }

    private void configSearchableDialog() {
        setSearchable(isSearchable);
        setEnableSearchHeader(isEnableSearchHeader);
        setSearchHeaderText(searchHeaderText);
        setSearchHint(searchHint);
        setSearchListItemColor(itemListColor);
        setSelectedSearchItemColor(selectedItemColor);
    }

    /*
     * Config dropdown width and height.
     */
    private void configDropdownWidthAfterAttrReady() {
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    SmartMaterialSpinner.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                SmartMaterialSpinner.this.setDropDownWidth(SmartMaterialSpinner.this.getWidth());
                SmartMaterialSpinner.this.setDropDownVerticalOffset(SmartMaterialSpinner.this.getHeight());
                if (!isShowEmptyDropdown && getCount() == 1 && hint != null) {
                    SmartMaterialSpinner.this.setDropDownWidth(0);
                    SmartMaterialSpinner.this.setDropDownVerticalOffset(0);
                }
                setError(error);
            }
        });
    }

    @Override
    public void setSelection(final int position) {
        this.post(new Runnable() {
            @Override
            public void run() {
                SmartMaterialSpinner.super.setSelection(position);
            }
        });
    }

    private void initPaintObjects() {
        int labelTextSize = getResources().getDimensionPixelSize(R.dimen.label_text_size);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(labelTextSize);
        if (typeface != null) {
            textPaint.setTypeface(typeface);
        }
        textPaint.setColor(baseColor);
        baseAlpha = textPaint.getAlpha();

        selectorPath = new Path();
        selectorPath.setFillType(Path.FillType.EVEN_ODD);

        selectorPoints = new Point[3];
        for (int i = 0; i < 3; i++) {
            selectorPoints[i] = new Point();
        }
    }

    @Override
    public int getSelectedItemPosition() {
        return super.getSelectedItemPosition();
    }

    private void initPadding() {
        innerPaddingTop = getPaddingTop();
        innerPaddingLeft = getPaddingLeft();
        innerPaddingRight = getPaddingRight();
        innerPaddingBottom = getPaddingBottom();
        extraPaddingTop = enableFloatingLabel ? floatingLabelTopSpacing + floatingLabelInsideSpacing + floatingLabelBottomSpacing : floatingLabelBottomSpacing;
        updateBottomPadding();
    }

    private void updateBottomPadding() {
        Paint.FontMetrics textMetrics = textPaint.getFontMetrics();
        extraPaddingBottom = underlineTopSpacing + underlineBottomSpacing;
        if (error != null) {
            extraPaddingBottom = underlineTopSpacing + dpToPx(4);
        }
        if (enableErrorLabel) {
            extraPaddingBottom += (int) ((textMetrics.descent - textMetrics.ascent) * currentNbErrorLines);
        }
        updatePadding();
    }

    private void initDimensions() {
        underlineTopSpacing = getResources().getDimensionPixelSize(R.dimen.smsp_underline_top_spacing);
        underlineBottomSpacing = getResources().getDimensionPixelSize(R.dimen.smsp_underline_bottom_spacing);
        floatingLabelTopSpacing = getResources().getDimensionPixelSize(R.dimen.smsp_floating_label_top_spacing);
        floatingLabelBottomSpacing = getResources().getDimensionPixelSize(R.dimen.smsp_floating_label_bottom_spacing);
        rightLeftSpinnerPadding = alignLabels ? getResources().getDimensionPixelSize(R.dimen.smsp_right_left_spinner_padding) : 0;
        floatingLabelInsideSpacing = getResources().getDimensionPixelSize(R.dimen.smsp_floating_label_inside_spacing);
        errorLabelSpacing = (int) getResources().getDimension(R.dimen.smsp_error_label_spacing);
        minContentHeight = (int) getResources().getDimension(R.dimen.smsp_min_content_height);
    }

    private void initOnItemSelectedListener() {
        setOnItemSelectedListener(null);
    }

    /*
     * **********************************************************************************
     * ANIMATION METHODS
     * **********************************************************************************
     */

    private void initFloatingLabelAnimator() {
        if (floatingLabelAnimator == null) {
            floatingLabelAnimator = ObjectAnimator.ofFloat(this, "floatingLabelPercent", 0f, 1f);
            floatingLabelAnimator.addUpdateListener(this);
        }
    }

    public void showFloatingLabel() {
        if (floatingLabelAnimator != null) {
            floatingLabelVisible = true;
            if (floatingLabelAnimator.isRunning()) {
                floatingLabelAnimator.reverse();
            } else {
                floatingLabelAnimator.start();
            }
        }
    }

    public void hideFloatingLabel() {
        if (floatingLabelAnimator != null) {
            floatingLabelVisible = false;
            floatingLabelAnimator.reverse();
        }
    }

    private void startErrorScrollingAnimator() {
        int textWidth = Math.round(textPaint.measureText(error.toString()));
        if (errorLabelAnimator == null) {
            errorLabelAnimator = ObjectAnimator.ofInt(this, "errorLabelPosX", 0, textWidth + getWidth() / 2);
            errorLabelAnimator.setStartDelay(1000);
            errorLabelAnimator.setInterpolator(new LinearInterpolator());
            errorLabelAnimator.setDuration(500 * error.length() - error.length());
            errorLabelAnimator.addUpdateListener(this);
            errorLabelAnimator.setRepeatCount(ValueAnimator.INFINITE);
        } else {
            errorLabelAnimator.setIntValues(0, textWidth + getWidth() / 2);
        }
        errorLabelAnimator.start();
    }


    private void startErrorMultilineAnimator(float destLines) {
        if (errorLabelAnimator == null) {
            errorLabelAnimator = ObjectAnimator.ofFloat(this, "currentNbErrorLines", destLines);

        } else {
            errorLabelAnimator.setFloatValues(destLines);
        }
        errorLabelAnimator.start();
    }


    /*
     * **********************************************************************************
     * UTILITY METHODS
     * **********************************************************************************
     */

    private int dpToPx(float dp) {
        final DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
        return Math.round(px);
    }

    private float pxToDp(float px) {
        final DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return px * displayMetrics.density;
    }

    private void updatePadding() {
        int left = innerPaddingLeft;
        int top = innerPaddingTop + extraPaddingTop;
        int right = innerPaddingRight;
        int bottom = innerPaddingBottom + extraPaddingBottom;
        super.setPadding(left, top, right, bottom);
        setMinimumHeight(top + bottom + minContentHeight);
    }

    private boolean needScrollingAnimation() {
        if (error != null) {
            float screenWidth = getWidth() - rightLeftSpinnerPadding;
            float errorTextWidth = textPaint.measureText(error.toString(), 0, error.length());
            return errorTextWidth > screenWidth;
        }
        return false;
    }

    private void configStaticLayout(CharSequence charSequence, TextPaint textPaint, int mWidth) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            StaticLayout.Builder builder = StaticLayout.Builder.obtain(charSequence, 0, charSequence.length(), textPaint, mWidth)
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .setLineSpacing(0.0F, 1.0F)
                    .setIncludePad(false);
            staticLayout = builder.build();
        } else {
            staticLayout = new StaticLayout(error, textPaint, mWidth, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
        }
    }

    private int prepareBottomPadding() {
        final int[] targetNbLines = {minNbErrorLines};
        if (error != null) {
            final int[] mWidth = {getWidth() - getPaddingRight() - getPaddingLeft()};
            if (mWidth[0] <= 0) {
                getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                        mWidth[0] = getWidth() - getPaddingRight() - getPaddingLeft();
                        configStaticLayout(error, textPaint, mWidth[0]);
                        int nbErrorLines = staticLayout.getLineCount();
                        targetNbLines[0] = Math.max(minNbErrorLines, nbErrorLines);
                    }
                });
                return targetNbLines[0];
            }
            configStaticLayout(error, textPaint, mWidth[0]);
            int nbErrorLines = staticLayout.getLineCount();
            targetNbLines[0] = Math.max(minNbErrorLines, nbErrorLines);
        }
        return targetNbLines[0];
    }

    private boolean isSpinnerEmpty() {
        return (hintAdapter.getCount() == 0 && hint == null) || (hintAdapter.getCount() == 1 && hint != null);
    }

    private AppCompatActivity scanForActivity(Context context) {
        if (context instanceof AppCompatActivity) {
            return (AppCompatActivity) context;
        } else if (context instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    /*
     * **********************************************************************************
     * DRAWING METHODS
     * **********************************************************************************
     */


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int startX = 0;
        int endX = getWidth();
        int lineHeight;

        int startYLine = getHeight() - getPaddingBottom() + underlineTopSpacing;
        int startYFloatingLabel = (int) (getPaddingTop() - floatingLabelPercent * floatingLabelBottomSpacing);

        if (error != null && enableErrorLabel) {
            lineHeight = dpToPx(underlineSize);
            int startYErrorLabel = startYLine + errorLabelSpacing + lineHeight;
            paint.setColor(underlineColor);
            textPaint.setColor(errorColor);
            textPaint.setTextSize(errorTextSize);
            //Error Label Drawing
            if (multilineError) {
                canvas.save();
                canvas.translate(startX + rightLeftSpinnerPadding, startYErrorLabel - errorLabelSpacing);
                if (staticLayout == null) {
                    int mWidth = getWidth() - getPaddingRight() - getPaddingLeft();
                    configStaticLayout(error, textPaint, mWidth);
                }
                staticLayout.draw(canvas);
                canvas.restore();
            } else {
                //scrolling
                canvas.drawText(error.toString(), startX + rightLeftSpinnerPadding - errorLabelPosX, startYErrorLabel, textPaint);
                if (errorLabelPosX > 0) {
                    canvas.save();
                    canvas.translate(textPaint.measureText(error.toString()) + getWidth() / 2F, 0);
                    canvas.drawText(error.toString(), startX + rightLeftSpinnerPadding - errorLabelPosX, startYErrorLabel, textPaint);
                    canvas.restore();
                }
            }
        } else {
            lineHeight = dpToPx(underlineSize);
            if (isSelected || hasFocus()) {
                paint.setColor(underlineColor); // highlightColor
            } else {
                paint.setColor(isEnabled() ? underlineColor : disabledColor);
            }
        }

        // Underline Drawing
        canvas.drawRect(startX, startYLine, endX, startYLine + lineHeight, paint);

        //Floating Label Drawing
        if ((hint != null || floatingLabelText != null) && enableFloatingLabel) {
            if (isSelected || hasFocus()) {
                textPaint.setColor(floatingLabelColor); // highlightColor
            } else {
                textPaint.setColor(isEnabled() ? floatingLabelColor : disabledColor);
            }
            if (floatingLabelAnimator.isRunning() || !floatingLabelVisible) {
                textPaint.setAlpha((int) ((0.8 * floatingLabelPercent + 0.2) * baseAlpha * floatingLabelPercent));
            }
            textPaint.setTextSize(floatingLabelSize);
            String textToDraw = floatingLabelText != null ? floatingLabelText.toString() : hint.toString();
            if (isRtl) {
                canvas.drawText(textToDraw, getWidth() - rightLeftSpinnerPadding - textPaint.measureText(textToDraw), startYFloatingLabel, textPaint);
            } else {
                canvas.drawText(textToDraw, startX + rightLeftSpinnerPadding, startYFloatingLabel, textPaint);
            }
        }
        drawSelector(canvas, getWidth() - rightLeftSpinnerPadding, getPaddingTop() + dpToPx(6));
    }

    private void drawSelector(Canvas canvas, int posX, int posY) {
        if (isSelected || hasFocus()) {
            paint.setColor(highlightColor);
        } else {
            paint.setColor(isEnabled() ? arrowColor : disabledColor);
        }

        Point point1 = selectorPoints[0];
        Point point2 = selectorPoints[1];
        Point point3 = selectorPoints[2];

        point1.set(posX, posY);
        point2.set((int) (posX - (arrowSize)), posY);
        point3.set((int) (posX - (arrowSize / 2)), (int) (posY + (arrowSize / 2)));

        selectorPath.reset();
        selectorPath.moveTo(point1.x, point1.y);
        selectorPath.lineTo(point2.x, point2.y);
        selectorPath.lineTo(point3.x, point3.y);
        selectorPath.close();
        canvas.drawPath(selectorPath, paint);
    }

    /*
     * **********************************************************************************
     * LISTENER METHODS
     * **********************************************************************************
     */

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEnabled()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isSelected = true;
                    if (getContext() instanceof Activity) {
                        SoftKeyboardUtil.hideSoftKeyboard((Activity) getContext());
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (!isShowEmptyDropdown && getCount() == 1 && hint != null && onEmptySpinnerClickListener != null) {
                        onEmptySpinnerClickListener.onEmptySpinnerClicked();
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    isSelected = false;
                    break;
            }
            invalidate();
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    @Override
    public void setOnItemSelectedListener(final OnItemSelectedListener listener) {
        final OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (hint != null || floatingLabelText != null) {
                    if (!floatingLabelVisible && position != 0) {
                        showFloatingLabel();
                    } else if (floatingLabelVisible && (position == 0 && !alwaysShowFloatingLabel)) {
                        hideFloatingLabel();
                    }
                }

               /* if (position != lastPosition && error != null) {
                    setError(error);
                }*/
                boolean isStartup = lastPosition == -1;
                lastPosition = position;

                if (listener != null) {
                    position = hint != null ? position - 1 : position;
                    if (position >= 0) {
                        if (view instanceof TextView) {
                            TextView selectedItem = (TextView) parent.getChildAt(0);
                            selectedItem.setTextSize(TypedValue.COMPLEX_UNIT_PX, itemSize);
                            selectedItem.setTextColor(itemColor);
                        }
                        listener.onItemSelected(parent, view, position, id);
                        setSearchSelectedPosition(position);
                    } else if (position == -1 && !isStartup) {
                        listener.onNothingSelected(parent);
                        setSearchSelectedPosition(position);
                    }
                } else {
                    if (position > 0 && !isStartup) {
                        if (view instanceof TextView) {
                            TextView selectedItem = (TextView) parent.getChildAt(0);
                            selectedItem.setTextSize(TypedValue.COMPLEX_UNIT_PX, itemSize);
                            selectedItem.setTextColor(itemColor);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (listener != null) {
                    listener.onNothingSelected(parent);
                }
            }
        };
        super.setOnItemSelectedListener(onItemSelectedListener);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        invalidate();
    }

    @Override
    public void onSearchItemSelected(Object item, int position) {
        int selectedIndex = searchDialogItem.indexOf(item);
        if (position >= 0) {
            if (hint != null) {
                selectedIndex += 1;
            }
            setSelection(selectedIndex);
        }
    }


    /*
     * **********************************************************************************
     * GETTERS AND SETTERS
     * **********************************************************************************
     */

    public int getBaseColor() {
        return baseColor;
    }

    public void setBaseColor(int baseColor) {
        this.baseColor = baseColor;
        textPaint.setColor(baseColor);
        baseAlpha = textPaint.getAlpha();
        invalidate();
    }

    public int getHighlightColor() {
        return highlightColor;
    }

    public void setHighlightColor(int highlightColor) {
        this.highlightColor = highlightColor;
        invalidate();
    }

    public int getHintColor() {
        return hintColor;
    }

    public void setHintColor(int hintColor) {
        this.hintColor = hintColor;
        invalidate();
    }

    public void setItemListHintColor(int itemListHintColor) {
        this.itemListHintColor = itemListHintColor;
    }

    public void setItemListHintBackground(int itemListHintBackground) {
        this.itemListHintBackground = itemListHintBackground;
    }

    public float getHintTextSize() {
        return hintTextSize;
    }

    public void setHintTextSize(float hintTextSize) {
        this.hintTextSize = dpToPx(hintTextSize);
        invalidate();
    }

    public void setErrorTextSize(float errorTextSize) {
        this.errorTextSize = dpToPx(errorTextSize);
        invalidate();
    }

    public int getErrorColor() {
        return errorColor;
    }

    public void setErrorColor(int errorColor) {
        this.errorColor = errorColor;
        invalidate();
    }

    public int getDisabledColor() {
        return disabledColor;
    }

    public void setDisabledColor(int disabledColor) {
        this.disabledColor = disabledColor;
        invalidate();
    }

    public void setHint(CharSequence hint) {
        this.hint = hint;
        invalidate();
    }

    public void setHint(int resid) {
        CharSequence hint = getResources().getString(resid);
        setHint(hint);
    }

    public CharSequence getHint() {
        return hint;
    }

    public void setHintView(Integer resId) {
        this.mHintView = resId;
    }

    public void setDropdownView(Integer resId) {
        this.mDropdownView = resId;
    }

    public void setFloatingLabelText(CharSequence floatingLabelText) {
        this.floatingLabelText = floatingLabelText;
        invalidate();
    }

    public void setFloatingLabelText(int resid) {
        String floatingLabelText = getResources().getString(resid);
        setFloatingLabelText(floatingLabelText);
    }

    public CharSequence getFloatingLabelText() {
        return this.floatingLabelText;
    }

    public void setFloatingLabelSize(float floatingLabelSize) {
        this.floatingLabelSize = dpToPx(floatingLabelSize);
        invalidate();
    }

    public int getFloatingLabelColor() {
        return floatingLabelColor;
    }

    public void setFloatingLabelColor(int floatingLabelColor) {
        this.floatingLabelColor = floatingLabelColor;
        invalidate();
    }

    public boolean isMultilineError() {
        return multilineError;
    }

    public void setMultilineError(boolean multilineError) {
        this.multilineError = multilineError;
        invalidate();
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
        if (typeface != null) {
            textPaint.setTypeface(typeface);
        }
        invalidate();
    }

    public boolean isAlignLabels() {
        return alignLabels;
    }

    public void setAlignLabels(boolean alignLabels) {
        this.alignLabels = alignLabels;
        rightLeftSpinnerPadding = alignLabels ? getResources().getDimensionPixelSize(R.dimen.smsp_right_left_spinner_padding) : 0;
        invalidate();
    }

    public float getUnderlineSize() {
        return underlineSize;
    }

    public void setUnderlineSize(float underlineSize) {
        this.underlineSize = underlineSize;
        invalidate();
    }

    public int getArrowColor() {
        return arrowColor;
    }

    public void setArrowColor(int arrowColor) {
        this.arrowColor = arrowColor;
        invalidate();
    }

    public float getArrowSize() {
        return arrowSize;
    }

    public void setArrowSize(float arrowSize) {
        this.arrowSize = arrowSize;
        invalidate();
    }

    public boolean isEnableErrorLabel() {
        return enableErrorLabel;
    }

    public void setEnableErrorLabel(boolean enableErrorLabel) {
        this.enableErrorLabel = enableErrorLabel;
        updateBottomPadding();
        invalidate();
    }

    public boolean isEnableFloatingLabel() {
        return enableFloatingLabel;
    }

    public void setEnableFloatingLabel(boolean enableFloatingLabel) {
        this.enableFloatingLabel = enableFloatingLabel;
        extraPaddingTop = enableFloatingLabel ? floatingLabelTopSpacing + floatingLabelInsideSpacing + floatingLabelBottomSpacing : floatingLabelBottomSpacing;
        updateBottomPadding();
        invalidate();
    }

    public void setError(CharSequence error) {
        this.error = error;
        updateBottomPadding();
        if (errorLabelAnimator != null) {
            errorLabelAnimator.end();
        }
        if (multilineError) {
            startErrorMultilineAnimator(prepareBottomPadding());
        } else if (needScrollingAnimation()) {
            startErrorScrollingAnimator();
        }
        requestLayout();
    }

    public void setError(int resid) {
        CharSequence error = getResources().getString(resid);
        setError(error);
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (!enabled) {
            isSelected = false;
            invalidate();
        }
        super.setEnabled(enabled);
    }

    public CharSequence getError() {
        return this.error;
    }

    public void setRtl() {
        isRtl = true;
        invalidate();
    }

    public boolean isRtl() {
        return isRtl;
    }

    public void setUnderlineColor(int color) {
        underlineColor = color;
        invalidate();
    }

    public void setItemSize(float itemSize) {
        this.itemSize = dpToPx(itemSize);
        invalidate();
    }

    public void setItemColor(int color) {
        this.itemColor = color;
    }

    public void setItemListColor(int color) {
        this.itemListColor = color;
        setSearchListItemColor(itemListColor);
        if (selectedItemColor == Color.BLACK && color != Color.BLACK) {
            selectedItemColor = color;
            setSelectedSearchItemColor(selectedItemColor);
        }
    }

    public void setSelectedItemColor(int color) {
        this.selectedItemColor = color;
        setSelectedSearchItemColor(selectedItemColor);
    }

    public void setSearchable(boolean searchable) {
        this.isSearchable = searchable;
        if (searchable) {
            setClickable(false);
            setOnTouchListener(new OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (isSearchable && hintAdapter != null) {
                            searchDialogItem.clear();
                            int itemStart = 0;
                            if (hint != null) {
                                itemStart = 1;
                            }
                            for (int i = itemStart; i < hintAdapter.getCount(); i++) {
                                searchDialogItem.add(hintAdapter.getItem(i));
                            }
                            AppCompatActivity appCompatActivity = scanForActivity(getContext());
                            if (appCompatActivity != null) {
                                FragmentManager fragmentManager = appCompatActivity.getSupportFragmentManager();
                                searchableSpinnerDialog.show(fragmentManager, "TAG");
                            }
                        }
                    }
                    return true;
                }
            });
        }
    }

    public void setEnableSearchHeader(boolean enableSearchHeader) {
        if (searchableSpinnerDialog != null) {
            searchableSpinnerDialog.setEnableSearchHeader(enableSearchHeader);
        }
    }

    public void setSearchHeaderText(String headerText) {
        if (searchableSpinnerDialog != null) {
            searchableSpinnerDialog.setSearchHeaderText(headerText);
        }
    }

    public void setSearchHeaderBackground(int color) {
        if (searchableSpinnerDialog != null) {
            searchableSpinnerDialog.setSearchHeaderBackground(color);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setSearchHeaderBackground(Drawable drawable) {
        if (searchableSpinnerDialog != null) {
            searchableSpinnerDialog.setSearchHeaderBackground(drawable);
        }
    }

    public void setSearchHint(String hint) {
        if (searchableSpinnerDialog != null) {
            searchableSpinnerDialog.setSearchHint(hint);
        }
    }

    private void setSearchListItemColor(int searchListItemColor) {
        if (searchableSpinnerDialog != null) {
            searchableSpinnerDialog.setSearchListItemColor(searchListItemColor);
        }
    }

    private void setSelectedSearchItemColor(int selectedSearchItemColor) {
        if (searchableSpinnerDialog != null) {
            searchableSpinnerDialog.setSelectedSearchItemColor(selectedSearchItemColor);
        }
    }

    private void setSearchSelectedPosition(int position) {
        if (searchableSpinnerDialog != null) {
            searchableSpinnerDialog.setSelectedPosition(position);
        }
    }

    public interface OnEmptySpinnerClickListener {
        void onEmptySpinnerClicked();
    }

    public void setOnEmptySpinnerClicked(OnEmptySpinnerClickListener onEmptySpinnerClickListener) {
        this.onEmptySpinnerClickListener = onEmptySpinnerClickListener;
    }


    /**
     * @deprecated {use @link #setPaddingSafe(int, int, int, int)} to keep internal computation OK
     */
    @Deprecated
    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
    }

    public void setPaddingSafe(int left, int top, int right, int bottom) {
        innerPaddingRight = right;
        innerPaddingLeft = left;
        innerPaddingTop = top;
        innerPaddingBottom = bottom;

        updatePadding();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        if (adapter instanceof HintAdapter) {
            super.setAdapter(adapter);
        } else {
            hintAdapter = new HintAdapter(adapter, getContext());
            super.setAdapter(hintAdapter);
        }
    }

    public void setItems(@NonNull List<T> items) {
        this.items = items;
        ArrayAdapter<T> adapter = new ArrayAdapter<>(getContext(), R.layout.smart_material_spinner_hint_item_layout, items);
        adapter.setDropDownViewResource(R.layout.smart_material_spinner_dropdown_item);
        setAdapter(adapter);
    }

    public List<T> getItems() {
        return items;
    }

    @Override
    public SpinnerAdapter getAdapter() {
        return hintAdapter != null ? hintAdapter.getWrappedAdapter() : null;
    }

    private float getFloatingLabelPercent() {
        return floatingLabelPercent;
    }

    private void setFloatingLabelPercent(float floatingLabelPercent) {
        this.floatingLabelPercent = floatingLabelPercent;
    }

    private int getErrorLabelPosX() {
        return errorLabelPosX;
    }

    private void setErrorLabelPosX(int errorLabelPosX) {
        this.errorLabelPosX = errorLabelPosX;
    }

    private float getCurrentNbErrorLines() {
        return currentNbErrorLines;
    }

    private void setCurrentNbErrorLines(float currentNbErrorLines) {
        this.currentNbErrorLines = currentNbErrorLines;
        updateBottomPadding();
    }

    @Override
    public Object getItemAtPosition(int position) {
        if (hint != null) {
            position++;
        }
        return (hintAdapter == null || position < 0) ? null : hintAdapter.getItem(position);
    }

    @Override
    public long getItemIdAtPosition(int position) {
        if (hint != null) {
            position++;
        }
        return (hintAdapter == null || position < 0) ? INVALID_ROW_ID : hintAdapter.getItemId(position);
    }

    public void setShowEmptyDropdown(boolean status) {
        this.isShowEmptyDropdown = status;
        configDropdownWidthAfterAttrReady();
    }


    /*
     * **********************************************************************************
     * INNER CLASS
     * **********************************************************************************
     */

    private class HintAdapter<T> extends BaseAdapter {
        private static final int HINT_TYPE = -1;
        private SpinnerAdapter mSpinnerAdapter;
        private Context mContext;

        public HintAdapter(SpinnerAdapter spinnerAdapter, Context context) {
            mSpinnerAdapter = spinnerAdapter;
            mContext = context;
        }

        @Override
        public int getViewTypeCount() {
            //Workaround waiting for a Google correction (https://code.google.com/p/android/issues/detail?id=79011)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return 1;
            }
            return mSpinnerAdapter.getViewTypeCount();
        }

        @Override
        public int getItemViewType(int position) {
            position = hint != null ? position - 1 : position;
            return (position == -1) ? HINT_TYPE : mSpinnerAdapter.getItemViewType(position);
        }

        @Override
        public int getCount() {
            int count = mSpinnerAdapter.getCount();
            return hint != null ? count + 1 : count;
        }

        @Override
        public Object getItem(int position) {
            position = hint != null ? position - 1 : position;
            return (position == -1) ? hint : mSpinnerAdapter.getItem(position);
        }

        @Override
        public long getItemId(int position) {
            position = hint != null ? position - 1 : position;
            return (position == -1) ? 0 : mSpinnerAdapter.getItemId(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return buildView(position, convertView, parent, false);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            parent.setPadding(0, 0, 0, 0);
            return buildView(position, convertView, parent, true);
        }

        private View buildView(int position, View convertView, ViewGroup parent, boolean isDropDownView) {
            if (getItemViewType(position) == HINT_TYPE) {
                return getHintView(convertView, parent, isDropDownView);
            }
            //workaround to have multiple types in spinner
            if (convertView != null) {
                convertView = (convertView.getTag() != null && convertView.getTag() instanceof Integer && (Integer) convertView.getTag() != HINT_TYPE) ? convertView : null;
            }
            position = hint != null ? position - 1 : position;
            View dropdownItemView = (isDropDownView ? mSpinnerAdapter.getDropDownView(position, convertView, parent) : mSpinnerAdapter.getView(position, convertView, parent));
            if (dropdownItemView instanceof TextView) {
                TextView itemTextView = (TextView) dropdownItemView;
                itemTextView.setTextColor(itemListColor);
                if (position >= 0 && position == getSelectedItemPosition() - 1) {
                    itemTextView.setTextColor(selectedItemColor);
                }
            }
            return dropdownItemView;
        }

        private View getHintView(final View convertView, final ViewGroup parent, final boolean isDropDownView) {
            final LayoutInflater inflater = LayoutInflater.from(mContext);
            final int resId = isDropDownView ? mDropdownView : mHintView;
            final TextView textView = (TextView) inflater.inflate(resId, parent, false);
            textView.setText(hint);
            textView.setTextColor(SmartMaterialSpinner.this.isEnabled() ? hintColor : disabledColor);
            if (hintTextSize != -1)
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, hintTextSize);
            if (isDropDownView) {
                textView.setTextColor(itemListHintColor);
                textView.setBackgroundColor(itemListHintBackground);
            }
            textView.setTag(HINT_TYPE);
            return textView;
        }

        private SpinnerAdapter getWrappedAdapter() {
            return mSpinnerAdapter;
        }
    }
}