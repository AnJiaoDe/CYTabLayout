package com.cy.tablayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by cy on 2018/8/2.
 */

public class CYTabLayout<T> extends HorizontalScrollView {
    private Context context;


    private LinearLayout linearLayout;

    private GradientDrawable view_indicator;


    private int width_indicator = 30;
    private int height_indicator = 10;
    public static final int FIXED = 0;
    public static final int SCROLLABLE = 1;

    private int tabMode = 1;

    private boolean haveIndicator = true;

    private int position_selected_last = 0;
    private int currentItem = 0;


    private TabAdapter<T> tabAdapter;
    //???????????????????????????????????????????????????????????????????????????

    public CYTabLayout(Context context) {
        this(context, null);
    }

    public CYTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setOverScrollMode(View.OVER_SCROLL_NEVER);
        setScrollBarSize(0);
        setFillViewport(true);//设置滚动视图是否可以伸缩其内容以填充视口


        linearLayout = new LinearLayout(context);

        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(linearLayout);

        view_indicator = new GradientDrawable();

        view_indicator.setColor(0xffffff00);

        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.CYTabLayout);
        width_indicator = arr.getDimensionPixelSize(R.styleable.CYTabLayout_width_indicator, 0);
        height_indicator = arr.getDimensionPixelSize(R.styleable.CYTabLayout_height_indicator, 0);

        float cornerRadius = arr.getFloat(R.styleable.CYTabLayout_cornerRadius_indicator, 0);
        view_indicator.setCornerRadius(cornerRadius);

        if (width_indicator == 0) {
            width_indicator = 30;
        }
        if (height_indicator == 0) {
            height_indicator = 10;
        }

    }
    //???????????????????????????????????????????????????????????????????????????


    public void setTabMode(int tabMode) {
        this.tabMode = tabMode;

        if (linearLayout.getChildCount() == 0) {
            return;
        }
        int count_child = linearLayout.getChildCount();
        for (int i = 0; i < count_child; i++) {
            /** 每一个Tab的布局参数 */
            LinearLayout.LayoutParams layoutParams = tabMode == 0 ?
                    new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f) :
                    new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            linearLayout.getChildAt(i).setLayoutParams(layoutParams);
        }
        invalidate();


    }
    //???????????????????????????????????????????????????????????????????????????


    //???????????????????????????????????????????????????????????????????????????


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (linearLayout.getChildCount()==0)return;
        if (!haveIndicator) return;

        View view_current =  linearLayout.getChildAt(currentItem);


        int left_indicator = view_current.getLeft() + view_current.getPaddingLeft();

        int right_indicator = view_current.getRight() - view_current.getPaddingRight();

        int left = view_current.getLeft() + (view_current.getWidth() - width_indicator) / 2;
        int top = getHeight() - height_indicator;
        int right = left + width_indicator;
        int bottom = getHeight();


        view_indicator.setBounds(left, top, right, bottom);
        view_indicator.draw(canvas);

    }

    //???????????????????????????????????????????????????????????????????????????
    public GradientDrawable getView_indicator() {
        return view_indicator;
    }

    public boolean isHaveIndicator() {
        return haveIndicator;
    }

    public void setHaveIndicator(boolean haveIndicator) {
        this.haveIndicator = haveIndicator;

        invalidate();
    }

    public int getHeight_indicator() {
        return height_indicator;
    }

    /**
     * 设置指示器高度
     *
     * @param height_indicator
     */
    public void setHeight_indicator(int height_indicator) {
        this.height_indicator = height_indicator;

        invalidate();
    }

    public int getWidth_indicator() {
        return width_indicator;
    }

    /**
     * 设置指示器宽度
     *
     * @param width_indicator
     */

    public void setWidth_indicator(int width_indicator) {
        this.width_indicator = width_indicator;
        invalidate();

    }

    public int getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(int currentItem) {
        this.currentItem = currentItem;

        if(tabAdapter==null)return;

        tabAdapter.onTabUnSelected(new ViewHolder(
                        linearLayout.getChildAt(position_selected_last)), position_selected_last,
                tabAdapter.getList_bean().get(position_selected_last));

        tabAdapter.onTabSelected(new ViewHolder(linearLayout.getChildAt(currentItem)), currentItem, tabAdapter.getList_bean().get(currentItem));

        position_selected_last = currentItem;

        invalidate();
    }

    public void setAdapter(final CYTabLayout.TabAdapter<T> tabAdapter) {
        this.tabAdapter = tabAdapter;
        /** 每一个Tab的布局参数 */
        LinearLayout.LayoutParams layoutParams = tabMode == 0 ?
                new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f) :
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);

        linearLayout.removeAllViews();

        currentItem =0;
        position_selected_last=0;

        int size = tabAdapter.getCount();
        if (size==0)return;
        for (int i = 0; i < size; i++) {
            View view = tabAdapter.getView(i);
            linearLayout.addView(view, layoutParams);

            if (i==currentItem){

                tabAdapter.onTabSelected(new ViewHolder(view), currentItem, tabAdapter.getList_bean().get(currentItem));
            }



            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position_click = linearLayout.indexOfChild(v);

                    if (currentItem == position_click) {

                        tabAdapter.onTabReselected(new ViewHolder(v), currentItem, tabAdapter.getList_bean().get(currentItem));


                        return;
                    }
                    tabAdapter.onTabUnSelected(new ViewHolder(
                            linearLayout.getChildAt(position_selected_last)), position_selected_last,
                            tabAdapter.getList_bean().get(position_selected_last));


                    currentItem = position_click;


                    int scrollX_now = v.getLeft();
                    scrollX_now -= getWidth() / 2 - getPaddingLeft();


                    smoothScrollTo(scrollX_now, 0);

                    invalidate();

                    position_selected_last = position_click;

                    tabAdapter.onTabSelected(new ViewHolder(v), position_click, tabAdapter.getList_bean().get(position_click));


                }

            });
        }

    }

    /**
     * @param <T>
     */
    public static abstract class TabAdapter<T> {
        private List<T> list_bean;
        private Context context;

        public TabAdapter(Context context, List<T> list_bean) {
            this.list_bean = list_bean;
            this.context = context;
        }

        protected View getView(final int position) {

            View view = LayoutInflater.from(context).inflate(getItemLayoutID(position, list_bean.get(position)), null);


            bindDataToView(new ViewHolder(view), position, list_bean.get(position));
            return view;

        }

        public List<T> getList_bean() {
            return list_bean;
        }

        //填充数据
        public abstract void bindDataToView(ViewHolder holder, int position, T bean);

        /*
             取得ItemView的布局文件
             @return
            */
        public abstract int getItemLayoutID(int position, T bean);

        public abstract void onTabSelected(ViewHolder holder, int position, T bean);

        public abstract void onTabUnSelected(ViewHolder holder, int position, T bean);

        public abstract void onTabReselected(ViewHolder holder, int position, T bean);


        public int getCount() {
            return list_bean.size();
        }
    }

    /**
     *
     */
    public static class ViewHolder {
        private View itemView;
        private SparseArray<View> array_view;

        public ViewHolder(View itemView) {
            this.itemView = itemView;

            array_view = new SparseArray<View>();

        }


        public <T extends View> T getView(int viewId) {

            View view = array_view.get(viewId);
            if (view == null) {
                view = itemView.findViewById(viewId);
                array_view.put(viewId, view);
            }
            return (T) view;
        }


        public ViewHolder setVisible(int res_id) {
            getView(res_id).setVisibility(View.VISIBLE);
            return this;
        }

        public ViewHolder setInVisible(int res_id) {
            getView(res_id).setVisibility(View.INVISIBLE);
            return this;
        }

        public void setViewGone(int res_id) {
            getView(res_id).setVisibility(View.GONE);
        }

        public void setViewVisible(int res_id) {
            getView(res_id).setVisibility(View.VISIBLE);
        }


        public void setText(int tv_id, String text) {
            TextView tv = getView(tv_id);


            tv.setText(nullToString(text));
        }

        public String nullToString(Object object) {
            return object == null ? "" : object.toString();
        }


        public void setText(int tv_id, int text) {
            TextView tv = getView(tv_id);
            tv.setText(String.valueOf(nullToString(text)));
        }

        public void setTextColor(int tv_id, int color) {
            TextView tv = getView(tv_id);
            tv.setTextColor(color);
        }

        public String getTVText(int tv_id) {
            TextView tv = getView(tv_id);
            return tv.getText().toString().trim();
        }

        public String getETText(int tv_id) {
            EditText tv = getView(tv_id);
            return tv.getText().toString().trim();
        }

        public void setBackgroundResource(int v_id, int resid) {
            View view = getView(v_id);
            view.setBackgroundResource(resid);
        }
        public void setBackgroundColor(int v_id, int color) {
            View view = getView(v_id);
            view.setBackgroundColor(color);
        }

        public void setImageBitmap(int v_id, Bitmap bitmap) {
            ImageView view = getView(v_id);
            view.setImageBitmap(bitmap);
        }

        public void setImageResource(int v_id, int resID) {
            ImageView view = getView(v_id);
            view.setImageResource(resID);
        }


    }


}
