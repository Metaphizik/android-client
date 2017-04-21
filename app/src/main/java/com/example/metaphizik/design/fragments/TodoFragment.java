package com.example.metaphizik.design.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class TodoFragment extends AbstractTabFragment{
    private static final int LAYOUT = R.layout.fragment_history;

    public static TodoFragment getInstance(Context context){
        Bundle args = new Bundle();
        TodoFragment fragment = new TodoFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.tab_item_todo));

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT,container, false);

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.recycleView);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(new RemindListAdapter(createMockRemindListData()));

        return view;
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
}