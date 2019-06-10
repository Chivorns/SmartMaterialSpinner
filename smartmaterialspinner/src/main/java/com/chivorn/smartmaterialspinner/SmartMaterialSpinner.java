package com.chivorn.smartmaterialspinner;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
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
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.StateListDrawable;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SmartMaterialSpinner<T> extends AppCompatSpinner implements ValueAnimator.AnimatorUpdateListener, SearchableSpinnerDialog.OnSearchDialogEventListener, Serializable {
    public static final int DEFAULT_ARROW_WIDTH_DP = 10;
    private static final String TAG = SmartMaterialSpinner.class.getSimpleName();

    //Paint objects
    private Paint paint;
    private TextPaint textPaint;
    private StaticLayout staticLayout;
    private TextView tvDropdownItem;

    private SearchableSpinnerDialog searchableSpinnerDialog;
    private List<T> item;
    private List<Object> searchDialogItem;

    private boolean isSearchable = false;
    private boolean isEnableSearchHeader = true;
    private String searchHeaderText;
    private int searchHeaderTextColor;
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
    private int arrowMarginLeft;
    private int arrowMarginTop;
    private int arrowMarginRight;
    private int arrowMarginBottom;

    //Properties about Error Label
    private int lastPosition = -1;
    private ObjectAnimator errorLabelAnimator;
    private int errorLabelPosX;
    private int minNbErrorLine;
    private float currentNbErrorLines;


    //Properties about Floating Label (
    private float floatingLabelPercent;
    private ObjectAnimator floatingLabelAnimator;
    private boolean isSelected;
    private boolean isFloatingLabelVisible;
    private int baseAlpha;


    //AttributeSet
    private int baseColor;
    private int highlightColor;
    private float errorTextSize;
    private int errorTextColor;
    private int disabledColor;
    private float underlineSize;
    private int underlineColor;
    private CharSequence errorText;
    private CharSequence hint;
    private int hintColor;
    private boolean isShowItemListHint = true;
    private int itemListHintColor;
    private int itemListHintBackground;
    private float itemSize;
    private int itemColor;
    private int itemListColor;
    private int selectedItemListColor;
    private float hintSize;
    private CharSequence floatingLabelText;
    private float floatingLabelSize;
    private int floatingLabelColor;
    private boolean isMultilineError;
    private Typeface typeface;
    private boolean alignLabel;
    private int arrowColor;
    private float arrowSize;
    private boolean enableErrorLabel;
    private boolean enableFloatingLabel;
    private boolean alwaysShowFloatingLabel;
    private boolean isRtl;
    private boolean isEnableDefaultSelect = true;

    private HintAdapter hintAdapter;
    private TextView tvSpinnerItem;

    //Default hint views
    private Integer itemView;
    private Integer dropdownView;

    private OnEmptySpinnerClickListener onEmptySpinnerClickListener;
    private OnSpinnerEventListener spinnerEventsListener;
    private boolean isShowing = false;

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
        removeDefaultSelector(getBackground());
        initSearchableDialogObject();
        initAttributes(context, attrs);
        initPaintObjects();
        initDimensions(context, attrs);
        initPadding();
        initFloatingLabelAnimator();
        initOnItemSelectedListener();
        configSearchableDialog();
        setMinimumHeight((int) (getPaddingTop() + getPaddingBottom() + minContentHeight + (itemSize > hintSize ? itemSize : hintSize)));
        setItem(new ArrayList<T>());
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray defaultArray = context.obtainStyledAttributes(new int[]{R.attr.colorControlNormal, R.attr.colorAccent});
        int defaultBaseColor = ContextCompat.getColor(context, R.color.smsp_base_color); // defaultArray.getColor(1, 0);
        int defaultHighlightColor = ContextCompat.getColor(context, R.color.smsp_base_color);
        int defaultErrorColor = ContextCompat.getColor(context, R.color.smsp_error_color);
        defaultArray.recycle();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SmartMaterialSpinner);
        String typefacePath = typedArray.getString(R.styleable.SmartMaterialSpinner_smsp_typeface);
        if (typefacePath != null) {
            typeface = Typeface.createFromAsset(getContext().getAssets(), typefacePath);
        }
        baseColor = typedArray.getColor(R.styleable.SmartMaterialSpinner_smsp_baseColor, defaultBaseColor);
        highlightColor = typedArray.getColor(R.styleable.SmartMaterialSpinner_smsp_highlightColor, defaultHighlightColor);
        errorTextSize = typedArray.getDimension(R.styleable.SmartMaterialSpinner_smsp_errorTextSize, getResources().getDimension(R.dimen.smsp_default_error_text_size));
        errorTextColor = typedArray.getColor(R.styleable.SmartMaterialSpinner_smsp_errorTextColor, defaultErrorColor);
        disabledColor = ContextCompat.getColor(context, R.color.smsp_disabled_color);
        underlineColor = typedArray.getColor(R.styleable.SmartMaterialSpinner_smsp_underlineColor, ContextCompat.getColor(context, R.color.smsp_underline_color));
        errorText = typedArray.getString(R.styleable.SmartMaterialSpinner_smsp_errorText);
        hint = typedArray.getString(R.styleable.SmartMaterialSpinner_smsp_hint);
        floatingLabelText = typedArray.getString(R.styleable.SmartMaterialSpinner_smsp_floatingLabelText);
        hintColor = typedArray.getColor(R.styleable.SmartMaterialSpinner_smsp_hintColor, ContextCompat.getColor(context, R.color.smsp_hint_color));
        //isShowItemListHint = typedArray.getBoolean(R.styleable.SmartMaterialSpinner_smsp_showItemListHint, true);
        itemListHintColor = typedArray.getColor(R.styleable.SmartMaterialSpinner_smsp_itemListHintColor, ContextCompat.getColor(context, R.color.smsp_item_list_hint_color));
        itemListHintBackground = typedArray.getColor(R.styleable.SmartMaterialSpinner_smsp_itemListHintBackgroundColor, ContextCompat.getColor(context, R.color.smsp_item_list_hint_background));
        itemSize = typedArray.getDimension(R.styleable.SmartMaterialSpinner_smsp_itemSize, getResources().getDimension(R.dimen.smsp_default_text_and_hint_size));
        itemColor = typedArray.getColor(R.styleable.SmartMaterialSpinner_smsp_itemColor, Color.BLACK);
        itemListColor = typedArray.getColor(R.styleable.SmartMaterialSpinner_smsp_itemListColor, Color.BLACK);
        selectedItemListColor = typedArray.getColor(R.styleable.SmartMaterialSpinner_smsp_selectedItemListColor, ContextCompat.getColor(context, R.color.smsp_selected_color));
        hintSize = typedArray.getDimension(R.styleable.SmartMaterialSpinner_smsp_hintSize, getResources().getDimension(R.dimen.smsp_default_hint_size));
        floatingLabelSize = typedArray.getDimension(R.styleable.SmartMaterialSpinner_smsp_floatingLabelSize, getResources().getDimension(R.dimen.smsp_default_floating_label_size));
        floatingLabelColor = typedArray.getColor(R.styleable.SmartMaterialSpinner_smsp_floatingLabelColor, ContextCompat.getColor(context, R.color.smsp_floating_label_color));
        isMultilineError = typedArray.getBoolean(R.styleable.SmartMaterialSpinner_smsp_multilineError, true);
        minNbErrorLine = typedArray.getInt(R.styleable.SmartMaterialSpinner_smsp_nbErrorLine, 1);
        currentNbErrorLines = minNbErrorLine;
        alignLabel = typedArray.getBoolean(R.styleable.SmartMaterialSpinner_smsp_alignLabel, true);
        underlineSize = typedArray.getDimension(R.styleable.SmartMaterialSpinner_smsp_underlineSize, 0.6f);
        arrowColor = typedArray.getColor(R.styleable.SmartMaterialSpinner_smsp_arrowColor, baseColor);
        arrowSize = typedArray.getDimension(R.styleable.SmartMaterialSpinner_smsp_arrowSize, dpToPx(DEFAULT_ARROW_WIDTH_DP));
        enableErrorLabel = typedArray.getBoolean(R.styleable.SmartMaterialSpinner_smsp_enableErrorLabel, true);
        enableFloatingLabel = typedArray.getBoolean(R.styleable.SmartMaterialSpinner_smsp_enableFloatingLabel, true);
        alwaysShowFloatingLabel = typedArray.getBoolean(R.styleable.SmartMaterialSpinner_smsp_alwaysShowFloatingLabel, false);
        isRtl = typedArray.getBoolean(R.styleable.SmartMaterialSpinner_smsp_isRtl, false);
        itemView = typedArray.getResourceId(R.styleable.SmartMaterialSpinner_smsp_itemView, R.layout.smart_material_spinner_item_layout);
        dropdownView = typedArray.getResourceId(R.styleable.SmartMaterialSpinner_smsp_dropdownView, R.layout.smart_material_spinner_dropdown_item_layout);
        isSearchable = typedArray.getBoolean(R.styleable.SmartMaterialSpinner_smsp_isSearchable, false);
        isEnableSearchHeader = typedArray.getBoolean(R.styleable.SmartMaterialSpinner_smsp_enableSearchHeader, true);
        searchHeaderText = typedArray.getString(R.styleable.SmartMaterialSpinner_smsp_searchHeaderText);
        searchHeaderTextColor = typedArray.getColor(R.styleable.SmartMaterialSpinner_smsp_searchHeaderTextColor, ContextCompat.getColor(context, R.color.smsp_search_header_text_color));
        int searchHeaderDrawableResId = typedArray.getResourceId(R.styleable.SmartMaterialSpinner_smsp_searchHeaderBackgroundColor, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && searchHeaderDrawableResId != 0) {
            setSearchHeaderBackgroundColor(AppCompatResources.getDrawable(getContext(), searchHeaderDrawableResId));
        } else {
            setSearchHeaderBackgroundColor(typedArray.getColor(R.styleable.SmartMaterialSpinner_smsp_searchHeaderBackgroundColor, ContextCompat.getColor(context, R.color.smsp_search_header_background)));
        }
        searchHint = typedArray.getString(R.styleable.SmartMaterialSpinner_smsp_searchHint);
        //isEnableDefaultSelect = typedArray.getBoolean(R.styleable.SmartMaterialSpinner_smsp_enableDefaultSelect, true);

        typedArray.recycle();
        lastPosition = -1;
    }

    private void initSearchableDialogObject() {
        searchDialogItem = new ArrayList<>();
        searchableSpinnerDialog = SearchableSpinnerDialog.newInstance(this, searchDialogItem);
        //  searchableSpinnerDialog.setOnSearchDialogEventListener(this);
    }

    private void configSearchableDialog() {
        setSearchable(isSearchable);
        setEnableSearchHeader(isEnableSearchHeader);
        setSearchHeaderText(searchHeaderText);
        setSearchHeaderTextColor(searchHeaderTextColor);
        setSearchHint(searchHint);
        setSearchListItemColor(itemListColor);
        setSelectedSearchItemColor(selectedItemListColor);
    }

    private void removeDefaultSelector(Drawable drawable) {
        if (drawable instanceof LayerDrawable || (drawable instanceof StateListDrawable && drawable.getCurrent() instanceof NinePatchDrawable)) {
            setBackgroundResource(R.drawable.smsp_transparent_color);
        }
    }

    /*
     * Config dropdown width and height.
     */
    private void configDropdownWidthAfterDataReady() {
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    SmartMaterialSpinner.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                if (getWidth() != 0 && getHeight() != 0) {
                    SmartMaterialSpinner.this.setDropDownWidth(getWidth());
                    SmartMaterialSpinner.this.setDropDownVerticalOffset(getHeight());
                }
                if (isSpinnerEmpty()) {
                    SmartMaterialSpinner.this.setDropDownWidth(0);
                    SmartMaterialSpinner.this.setDropDownVerticalOffset(0);
                }
                setErrorText(errorText);
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
        int selectedIndex = super.getSelectedItemPosition();
        if (hint != null)
            selectedIndex -= 1;
        return selectedIndex;
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
        if (errorText != null) {
            extraPaddingBottom = underlineTopSpacing + dpToPx(4);
        }
        if (enableErrorLabel) {
            extraPaddingBottom += (int) ((textMetrics.descent - textMetrics.ascent) * currentNbErrorLines);
        }
        updatePadding();
    }

    private void initDimensions(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SmartMaterialSpinner);

        underlineTopSpacing = getResources().getDimensionPixelSize(R.dimen.smsp_underline_top_spacing);
        underlineBottomSpacing = getResources().getDimensionPixelSize(R.dimen.smsp_underline_bottom_spacing);
        floatingLabelTopSpacing = getResources().getDimensionPixelSize(R.dimen.smsp_floating_label_top_spacing);
        floatingLabelBottomSpacing = getResources().getDimensionPixelSize(R.dimen.smsp_floating_label_bottom_spacing);
        rightLeftSpinnerPadding = alignLabel ? getResources().getDimensionPixelSize(R.dimen.smsp_right_left_spinner_padding) : 0;
        floatingLabelInsideSpacing = getResources().getDimensionPixelSize(R.dimen.smsp_floating_label_inside_spacing);
        errorLabelSpacing = getResources().getDimensionPixelSize(R.dimen.smsp_error_label_spacing);
        minContentHeight = getResources().getDimensionPixelSize(R.dimen.smsp_min_content_height);

        arrowMarginLeft = typedArray.getDimensionPixelSize(R.styleable.SmartMaterialSpinner_smsp_arrowMarginLeft, 0);
        arrowMarginTop = typedArray.getDimensionPixelSize(R.styleable.SmartMaterialSpinner_smsp_arrowMarginTop, 0);
        arrowMarginRight = typedArray.getDimensionPixelSize(R.styleable.SmartMaterialSpinner_smsp_arrowMarginRight, getResources().getDimensionPixelSize(R.dimen.smsp_default_arrow_margin_right));
        arrowMarginBottom = typedArray.getDimensionPixelSize(R.styleable.SmartMaterialSpinner_smsp_arrowMarginBottom, 0);

        typedArray.recycle();
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
            isFloatingLabelVisible = true;
            if (floatingLabelAnimator.isRunning()) {
                floatingLabelAnimator.reverse();
            } else {
                floatingLabelAnimator.start();
            }
        }
    }

    public void hideFloatingLabel() {
        if (floatingLabelAnimator != null) {
            isFloatingLabelVisible = false;
            floatingLabelAnimator.reverse();
        }
    }

    private void startErrorScrollingAnimator() {
        int textWidth = Math.round(textPaint.measureText(errorText.toString()));
        if (errorLabelAnimator == null) {
            errorLabelAnimator = ObjectAnimator.ofInt(this, "errorLabelPosX", 0, textWidth + getWidth() / 2);
            errorLabelAnimator.setStartDelay(1000);
            errorLabelAnimator.setInterpolator(new LinearInterpolator());
            errorLabelAnimator.setDuration(500 * errorText.length() - errorText.length());
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
        setMinimumHeight((int) (top + bottom + minContentHeight + (itemSize > hintSize ? itemSize : hintSize)));
    }

    private boolean needScrollingAnimation() {
        if (errorText != null) {
            float screenWidth = getWidth() - rightLeftSpinnerPadding;
            float errorTextWidth = textPaint.measureText(errorText.toString(), 0, errorText.length());
            return errorTextWidth > screenWidth;
        }
        return false;
    }

    private void configStaticLayout(CharSequence charSequence, TextPaint textPaint, int mWidth) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            StaticLayout.Builder builder = StaticLayout.Builder.obtain(charSequence, 0, charSequence.length(), textPaint, mWidth)
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .setLineSpacing(0.0F, 1.0F)
                    .setIncludePad(true);
            staticLayout = builder.build();
        } else {
            staticLayout = new StaticLayout(errorText, textPaint, mWidth, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
        }
    }

    private int prepareBottomPadding() {
        final int[] targetNbLines = {minNbErrorLine};
        if (errorText != null) {
            final int[] mWidth = {getWidth() - getPaddingRight() - getPaddingLeft()};
            if (mWidth[0] <= 0) {
                getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                        mWidth[0] = getWidth() - getPaddingRight() - getPaddingLeft();
                        configStaticLayout(errorText, textPaint, mWidth[0]);
                        int nbErrorLines = staticLayout.getLineCount();
                        targetNbLines[0] = Math.max(minNbErrorLine, nbErrorLines);
                    }
                });
                return targetNbLines[0];
            }
            configStaticLayout(errorText, textPaint, mWidth[0]);
            int nbErrorLines = staticLayout.getLineCount();
            targetNbLines[0] = Math.max(minNbErrorLine, nbErrorLines);
        }
        return targetNbLines[0];
    }

    private boolean isSpinnerEmpty() {
        return (hintAdapter.getCount() == 0 && hint == null) || (hintAdapter.getCount() == 1 && getCount() == 0 && hint != null) || (item.size() == 0 && getCount() == 1 && hint != null);
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

        if (errorText != null && enableErrorLabel) {
            lineHeight = dpToPx(underlineSize);
            int startYErrorLabel = startYLine + errorLabelSpacing + lineHeight;
            paint.setColor(underlineColor);
            textPaint.setColor(errorTextColor);
            textPaint.setTextSize(errorTextSize);
            //Error Label Drawing
            if (isMultilineError) {
                canvas.save();
                canvas.translate(startX + rightLeftSpinnerPadding, startYErrorLabel - errorLabelSpacing);
                if (staticLayout == null) {
                    int mWidth = getWidth() - getPaddingRight() - getPaddingLeft();
                    configStaticLayout(errorText, textPaint, mWidth);
                }
                staticLayout.draw(canvas);
                canvas.restore();
            } else {
                //scrolling
                canvas.drawText(errorText.toString(), startX + rightLeftSpinnerPadding - errorLabelPosX, startYErrorLabel, textPaint);
                if (errorLabelPosX > 0) {
                    canvas.save();
                    canvas.translate(textPaint.measureText(errorText.toString()) + getWidth() / 2F, 0);
                    canvas.drawText(errorText.toString(), startX + rightLeftSpinnerPadding - errorLabelPosX, startYErrorLabel, textPaint);
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
            if (floatingLabelAnimator.isRunning() || !isFloatingLabelVisible) {
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
        //  drawSelector(canvas, (getWidth() - rightLeftSpinnerPadding - arrowMarginRight + arrowMarginLeft), getPaddingTop() + dpToPx(6) - arrowMarginBottom + arrowMarginTop);
        drawSelector(canvas, (getWidth() - rightLeftSpinnerPadding - arrowMarginRight + arrowMarginLeft), (int) (getPaddingTop() - arrowMarginBottom + arrowMarginTop + minContentHeight / 2F + itemSize / 2 - floatingLabelTopSpacing));
    }

    private void drawSelector(Canvas canvas, int posX, int posY) {
        if (isSelected || hasFocus()) {
            paint.setColor(arrowColor); //highlightColor
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
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            SoftKeyboardUtil.hideSoftKeyboard(getContext());
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        SoftKeyboardUtil.hideSoftKeyboard(getContext());
        if (isSpinnerClickable()) {
            isShowing = false;
            onEmptySpinnerClickListener.onEmptySpinnerClicked();
            return true;
        } else if (isSearchable && hintAdapter != null) {
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
                if (!isShowing()) {
                    isShowing = true;
                    searchableSpinnerDialog.show(fragmentManager, "TAG");
                }
                if (spinnerEventsListener != null) {
                    spinnerEventsListener.onSpinnerOpened(SmartMaterialSpinner.this);
                }
                return true;
            }
        } else if (isSpinnerEmpty()) {
            isShowing = false;
            return true;
        }

        isShowing = true;
        if (spinnerEventsListener != null) {
            spinnerEventsListener.onSpinnerOpened(this);
        }
        return super.performClick();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (isShowing() && hasWindowFocus) {
            dismiss();
        }
        super.onWindowFocusChanged(hasWindowFocus);
    }

    public void setOnSpinnerEventListener(OnSpinnerEventListener onSpinnerEventListener) {
        this.spinnerEventsListener = onSpinnerEventListener;
    }

    public void dismiss() {
        isShowing = false;
        if (spinnerEventsListener != null) {
            spinnerEventsListener.onSpinnerClosed(this);
        }
    }

    public boolean isShowing() {
        return isShowing;
    }

    @Override
    public void setOnItemSelectedListener(final OnItemSelectedListener listener) {
        final OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lastPosition = position;
                updateSelectedItemStyle(parent, view);
                if (isSearchable) {
                    SoftKeyboardUtil.hideSoftKeyboard(getContext());
                }
                if (hint != null || floatingLabelText != null) {
                    if (!isFloatingLabelVisible && position != -1) {
                        showFloatingLabel();
                    } else if (isFloatingLabelVisible && (position == -1 && !alwaysShowFloatingLabel)) {
                        hideFloatingLabel();
                    }
                }
                if (listener != null) {
                    listener.onItemSelected(parent, view, position, id);
                    setSearchSelectedPosition(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (lastPosition != -1 && listener != null) {
                    listener.onNothingSelected(parent);
                    if (isFloatingLabelVisible && !alwaysShowFloatingLabel) {
                        hideFloatingLabel();
                    }
                }
            }
        };
        super.setOnItemSelectedListener(onItemSelectedListener);
    }

    private void updateSelectedItemStyle(AdapterView<?> parent, View view) {
        if (view instanceof TextView) {
            TextView selectedItem = (TextView) parent.getChildAt(0);
            selectedItem.setTextSize(TypedValue.COMPLEX_UNIT_PX, itemSize);
            selectedItem.setTextColor(itemColor);
            //  selectedItem.setPadding(selectedItem.getPaddingLeft(), selectedItem.getPaddingTop(), arrowMarginRight + dpToPx(14), selectedItem.getPaddingBottom());
            selectedItem.setPadding(selectedItem.getPaddingLeft(), selectedItem.getPaddingTop(), (int) (arrowMarginRight + itemSize * 0.4), selectedItem.getPaddingBottom());
        }
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

    @Override
    public void onSearchableSpinnerDismiss() {
        dismiss();
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

    /*public boolean isShowItemListHint() {
        return isShowItemListHint;
    }

    public void setShowItemListHint(boolean showItemListHint) {
        isShowItemListHint = showItemListHint;
        invalidate();
    }*/

    public int getItemListHintColor() {
        return itemListHintColor;
    }

    public void setItemListHintColor(int itemListHintColor) {
        this.itemListHintColor = itemListHintColor;
        invalidate();
    }

    public int getItemListHintBackground() {
        return itemListHintBackground;
    }

    public void setItemListHintBackground(int itemListHintBackground) {
        this.itemListHintBackground = itemListHintBackground;
        invalidate();
    }

    public float getHintSize() {
        return hintSize;
    }

    public void setHintSize(float hintSize) {
        this.hintSize = dpToPx(hintSize);
        updatePadding();
        invalidate();
    }

    public float getErrorTextSize() {
        return errorTextSize;
    }

    public void setErrorTextSize(float errorTextSize) {
        this.errorTextSize = dpToPx(errorTextSize);
        invalidate();
    }

    public int getErrorTextColor() {
        return errorTextColor;
    }

    public void setErrorTextColor(int errorTextColor) {
        this.errorTextColor = errorTextColor;
        invalidate();
    }

    public int getDisabledColor() {
        return disabledColor;
    }

    public void setDisabledColor(int disabledColor) {
        this.disabledColor = disabledColor;
        invalidate();
    }

    public CharSequence getHint() {
        return hint;
    }

    public void setHint(CharSequence hint) {
        this.hint = hint;
        if (isSpinnerEmpty()) {
            setAdapter(getAdapter());
        }
        invalidate();
    }

    public void setHint(int resId) {
        CharSequence hint = getResources().getString(resId);
        setHint(hint);
    }

    public void setItemView(Integer resId) {
        this.itemView = resId;
        invalidate();
    }

    public void setDropdownView(Integer resId) {
        this.dropdownView = resId;
        invalidate();
    }

    public CharSequence getFloatingLabelText() {
        return this.floatingLabelText;
    }

    public void setFloatingLabelText(CharSequence floatingLabelText) {
        this.floatingLabelText = floatingLabelText;
        invalidate();
    }

    public void setFloatingLabelText(int resId) {
        String floatingLabelText = getResources().getString(resId);
        setFloatingLabelText(floatingLabelText);
    }

    public float getFloatingLabelSize() {
        return floatingLabelSize;
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
        return isMultilineError;
    }

    public void setMultilineError(boolean multilineError) {
        this.isMultilineError = multilineError;
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

    public boolean isAlignLabel() {
        return alignLabel;
    }

    public void setAlignLabel(boolean alignLabel) {
        this.alignLabel = alignLabel;
        rightLeftSpinnerPadding = alignLabel ? getResources().getDimensionPixelSize(R.dimen.smsp_right_left_spinner_padding) : 0;
        invalidate();
    }

    public float getArrowSize() {
        return arrowSize;
    }

    public void setArrowSize(float arrowSize) {
        this.arrowSize = arrowSize;
        invalidate();
    }

    public int getArrowColor() {
        return arrowColor;
    }

    public void setArrowColor(int arrowColor) {
        this.arrowColor = arrowColor;
        invalidate();
    }

    public float getUnderlineSize() {
        return underlineSize;
    }

    public void setUnderlineSize(float underlineSize) {
        this.underlineSize = underlineSize;
        invalidate();
    }

    public int getUnderlineColor() {
        return underlineColor;
    }

    public void setUnderlineColor(int underlineColor) {
        this.underlineColor = underlineColor;
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


    public boolean isAlwaysShowFloatingLabel() {
        return alwaysShowFloatingLabel;
    }

    public void setAlwaysShowFloatingLabel(boolean alwaysShowFloatingLabel) {
        this.alwaysShowFloatingLabel = alwaysShowFloatingLabel;
        invalidate();
    }

    public CharSequence getErrorText() {
        return this.errorText;
    }

    public void setErrorText(CharSequence errorText) {
        this.errorText = errorText;
        updateBottomPadding();
        if (errorLabelAnimator != null) {
            errorLabelAnimator.end();
        }
        if (isMultilineError) {
            startErrorMultilineAnimator(prepareBottomPadding());
        } else if (needScrollingAnimation()) {
            startErrorScrollingAnimator();
        }
        requestLayout();
    }

    public void setErrorText(int resId) {
        CharSequence error = getResources().getString(resId);
        setErrorText(error);
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (!enabled) {
            isSelected = false;
            invalidate();
        }
        super.setEnabled(enabled);
    }

    public boolean isRtl() {
        return isRtl;
    }

    public void setRtl() {
        isRtl = true;
        invalidate();
    }

    public float getItemSize() {
        return itemSize;
    }

    public void setItemSize(float itemSize) {
        this.itemSize = dpToPx(itemSize);
        updatePadding();
        invalidate();
    }

    public int getItemColor() {
        return itemColor;
    }

    public void setItemColor(int color) {
        this.itemColor = color;
        invalidate();
    }

    public int getItemListColor() {
        return itemListColor;
    }

    public void setItemListColor(int color) {
        this.itemListColor = color;
        setSearchListItemColor(itemListColor);
        if (selectedItemListColor == Color.BLACK && color != Color.BLACK) {
            selectedItemListColor = color;
            setSelectedSearchItemColor(selectedItemListColor);
        }
        invalidate();
    }

    public int getSelectedItemListColor() {
        return selectedItemListColor;
    }

    public void setSelectedItemListColor(int selectedItemListColor) {
        this.selectedItemListColor = selectedItemListColor;
        setSelectedSearchItemColor(this.selectedItemListColor);
        invalidate();
    }

    public boolean isSearchable() {
        return isSearchable;
    }

    public void setSearchable(boolean searchable) {
        this.isSearchable = searchable;
        invalidate();
    }

    public boolean isEnableSearchHeader() {
        return isEnableSearchHeader;
    }

    public void setEnableSearchHeader(boolean isEnableSearchHeader) {
        this.isEnableSearchHeader = isEnableSearchHeader;
        if (searchableSpinnerDialog != null) {
            searchableSpinnerDialog.setEnableSearchHeader(isEnableSearchHeader);
        }
        invalidate();
    }

    public String getSearchHeaderText() {
        return searchHeaderText;
    }

    public void setSearchHeaderText(String searchHeaderText) {
        this.searchHeaderText = searchHeaderText;
        if (searchableSpinnerDialog != null) {
            searchableSpinnerDialog.setSearchHeaderText(searchHeaderText);
        }
        invalidate();
    }

    public int getSearchHeaderTextColor() {
        return searchHeaderTextColor;
    }

    public void setSearchHeaderTextColor(int color) {
        this.searchHeaderTextColor = color;
        if (searchableSpinnerDialog != null) {
            searchableSpinnerDialog.setSearchHeaderTextColor(color);
        }
        invalidate();
    }

    public void setSearchHeaderBackgroundColor(int color) {
        if (searchableSpinnerDialog != null) {
            searchableSpinnerDialog.setSearchHeaderBackgroundColor(color);
        }
        invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setSearchHeaderBackgroundColor(Drawable drawable) {
        if (searchableSpinnerDialog != null) {
            searchableSpinnerDialog.setSearchHeaderBackgroundColor(drawable);
        }
        invalidate();
    }

    public String getSearchHint() {
        return searchHint;
    }

    public void setSearchHint(String searchHint) {
        this.searchHint = searchHint;
        if (searchableSpinnerDialog != null) {
            searchableSpinnerDialog.setSearchHint(searchHint);
        }
        invalidate();
    }

    public void setSearchListItemColor(int searchListItemColor) {
        if (searchableSpinnerDialog != null) {
            searchableSpinnerDialog.setSearchListItemColor(searchListItemColor);
        }
        invalidate();
    }

    public void setSelectedSearchItemColor(int selectedSearchItemColor) {
        if (searchableSpinnerDialog != null) {
            searchableSpinnerDialog.setSelectedSearchItemColor(selectedSearchItemColor);
        }
        invalidate();
    }

    private void setSearchSelectedPosition(int position) {
        if (searchableSpinnerDialog != null) {
            searchableSpinnerDialog.setSelectedPosition(position);
        }
        invalidate();
    }

    public void setSearchDialogGravity(int gravity) {
        if (searchableSpinnerDialog != null) {
            searchableSpinnerDialog.setGravity(gravity);
        }
        invalidate();
    }

  /*  public boolean isEnableDefaultSelect() {
        return isEnableDefaultSelect;
    }

    public void setEnableDefaultSelect(boolean enableDefaultSelect) {
        this.isEnableDefaultSelect = enableDefaultSelect;
        invalidate();
    }*/

    public int getArrowMarginLeft() {
        return arrowMarginLeft;
    }

    public void setArrowMarginLeft(int padding) {
        this.arrowMarginLeft = dpToPx(padding);
        invalidate();
    }

    public int getArrowMarginTop() {
        return arrowMarginTop;
    }

    public void setArrowMarginTop(int padding) {
        this.arrowMarginTop = dpToPx(padding);
        invalidate();
    }

    public int getArrowMarginRight() {
        return arrowMarginRight;
    }

    public void setArrowMarginRight(int padding) {
        this.arrowMarginRight = dpToPx(padding);
        invalidate();
    }

    public int getArrowMarginBottom() {
        return arrowMarginBottom;
    }

    public void setArrowMarginBottom(int padding) {
        this.arrowMarginBottom = dpToPx(padding);
        invalidate();
    }

    public void setArrowPadding(int left, int top, int right, int bottom) {
        this.arrowMarginLeft = left;
        this.arrowMarginTop = top;
        this.arrowMarginRight = right;
        this.arrowMarginBottom = bottom;
        invalidate();
    }

    public void setOnEmptySpinnerClickListener(OnEmptySpinnerClickListener onEmptySpinnerClickListener) {
        this.onEmptySpinnerClickListener = onEmptySpinnerClickListener;
    }

    private boolean isSpinnerClickable() {
        return isSpinnerEmpty() && onEmptySpinnerClickListener != null;
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
            this.hintAdapter = (HintAdapter) adapter;
            super.setAdapter(adapter);
        } else {
            hintAdapter = new HintAdapter(adapter, getContext());
            super.setAdapter(hintAdapter);
        }
    }

    public void setItem(@NonNull List<T> item) {
        this.item = item;
        ArrayAdapter<T> adapter = new ArrayAdapter<>(getContext(), R.layout.smart_material_spinner_item_layout, item);
        adapter.setDropDownViewResource(R.layout.smart_material_spinner_dropdown_item_layout);
        setAdapter(adapter);
        configDropdownWidthAfterDataReady();
        invalidate();
    }

    public List<T> getItem() {
        return item;
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
            return buildView(position, convertView, parent, true);
        }

        private View buildView(int position, View convertView, ViewGroup parent, boolean isDropDownView) {
            if (getItemViewType(position) == HINT_TYPE) {
                return getItemView(convertView, parent, isDropDownView);
            }
            //workaround to have multiple types in spinner
            if (convertView != null) {
                convertView = (convertView.getTag() != null && convertView.getTag() instanceof Integer && (Integer) convertView.getTag() != HINT_TYPE) ? convertView : null;
            }
            position = hint != null ? position - 1 : position;
            View dropdownItemView = (isDropDownView ? mSpinnerAdapter.getDropDownView(position, convertView, parent) : mSpinnerAdapter.getView(position, convertView, parent));
            if (dropdownItemView instanceof TextView) {
                tvDropdownItem = (TextView) dropdownItemView;
                tvDropdownItem.setTextColor(itemListColor);
                if (position >= 0 && position == getSelectedItemPosition()) {
                    tvDropdownItem.setTextColor(selectedItemListColor);
                }
            }
            return dropdownItemView;
        }

        private View getItemView(final View convertView, final ViewGroup parent, final boolean isDropDownView) {
            final LayoutInflater inflater = LayoutInflater.from(mContext);
            final int resId = isDropDownView ? dropdownView : itemView;
            final TextView textView = (TextView) inflater.inflate(resId, parent, false);
            // parent.setPadding(0, 0, 0, 0);
            if (isShowing()) {
                textView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }
            textView.setText(hint);
            textView.setTextColor(SmartMaterialSpinner.this.isEnabled() ? hintColor : disabledColor);
            if (hintSize != -1)
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, hintSize);
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

    /**
     * Listening for no item spinner perform clicked event.
     */
    public interface OnEmptySpinnerClickListener {
        void onEmptySpinnerClicked();
    }

    /**
     * Listening for open/closed events.
     */
    public interface OnSpinnerEventListener {
        void onSpinnerOpened(SmartMaterialSpinner spinner);

        void onSpinnerClosed(SmartMaterialSpinner spinner);
    }
}