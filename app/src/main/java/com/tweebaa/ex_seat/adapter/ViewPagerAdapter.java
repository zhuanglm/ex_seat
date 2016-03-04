package com.tweebaa.ex_seat.adapter;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;
/**
 * Created by Zhuang on 2016-02-22.
 */
public class ViewPagerAdapter extends PagerAdapter {

    List<View> viewLists;

    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    public Parcelable saveState() {
        return null;
    }


    public ViewPagerAdapter(List<View> lists)
    {
        viewLists = lists;
    }

    //获得size
    @Override
    public int getCount() {
        return viewLists.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    //销毁Item
    @Override
    public void destroyItem(View view, int position, Object object)
    {
        ((ViewPager) view).removeView(viewLists.get(position));
    }

    //实例化Item
    @Override
    public Object instantiateItem(View view, int position)
    {
        ((ViewPager) view).addView(viewLists.get(position), 0);
        return viewLists.get(position);
    }
}
