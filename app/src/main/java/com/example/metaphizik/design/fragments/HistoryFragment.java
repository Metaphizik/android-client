package com.example.metaphizik.design.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.metaphizik.design.R;
import com.example.metaphizik.design.adapter.RemindListAdapter;
import com.example.metaphizik.design.dto.RemindDTO;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends AbstractTabFragment{
        private static final int LAYOUT = R.layout.fragment_history;

        private List<RemindDTO> data;
        private RemindListAdapter adapter;

        public static HistoryFragment getInstance(Context context, List<RemindDTO> data){
            Bundle args = new Bundle();
            HistoryFragment fragment = new HistoryFragment();
            fragment.setArguments(args);
            fragment.setData(data);
            fragment.setContext(context);
            fragment.setTitle(context.getString(R.string.tab_item_history));

            return fragment;
        }
    private SwipeRefreshLayout mSwipeRefreshLayout;
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            view = inflater.inflate(LAYOUT,container, false);

            RecyclerView rv = (RecyclerView) view.findViewById(R.id.recycleView);
            rv.setLayoutManager(new LinearLayoutManager(context));
            adapter = new RemindListAdapter(data);
            rv.setAdapter(adapter);

            View view2 = inflater.inflate(R.layout.swipe_layout, container, false);
            mSwipeRefreshLayout = (SwipeRefreshLayout) view2.findViewById(R.id.swiperefresh);

            // Set the color scheme of the SwipeRefreshLayout by providing 4 color resource ids
            mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW);

            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

                @Override public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override public void run() {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }, 5000);
                }});
                    return view2;
        }


    private List<RemindDTO> createMockRemindListData() {                //заглушка даты. ибо нет сервера откуда она прийдет(дата)
        List<RemindDTO> data = new ArrayList<>();
        data.add(new RemindDTO("Item 1"));
        data.add(new RemindDTO("Item 2"));
        data.add(new RemindDTO("Item 3"));
        data.add(new RemindDTO("Item 4"));
        data.add(new RemindDTO("Item 5"));
        data.add(new RemindDTO("Item 6"));
        return data;
    }

    public void setContext(Context context) {
            this.context = context;
        }

    public void setData(List<RemindDTO> data) {
        this.data = data;
    }
    public void refreshData(List<RemindDTO> data) {
        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }
}
