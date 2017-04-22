package com.example.metaphizik.design.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.metaphizik.design.dto.RemindDTO;
import com.example.metaphizik.design.fragments.AbstractTabFragment;
import com.example.metaphizik.design.fragments.BirthdaysFragment;
import com.example.metaphizik.design.fragments.HistoryFragment;
import com.example.metaphizik.design.fragments.IdeasFragment;
import com.example.metaphizik.design.fragments.TodoFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabsFragmentAdapter extends FragmentPagerAdapter{

    private Map<Integer, AbstractTabFragment> tabs;
    private Context context;

    private HistoryFragment historyFragment;

    private List<RemindDTO> data;

    public TabsFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        this.data = new ArrayList<>();
        initTabsMap(context);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).getTitle();
    }

    @Override
    public Fragment getItem(int position) {
        return tabs.get(position);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    private void initTabsMap(Context context) {
        historyFragment = HistoryFragment.getInstance(context, data);

        tabs = new HashMap<>();
        tabs.put(0, IdeasFragment.getInstance(context));
        tabs.put(1, historyFragment);
        tabs.put(2, TodoFragment.getInstance(context));
    }

    public void setDate(List<RemindDTO> data) {
        this.data = data;
        historyFragment.refreshData(data);
    }
}
