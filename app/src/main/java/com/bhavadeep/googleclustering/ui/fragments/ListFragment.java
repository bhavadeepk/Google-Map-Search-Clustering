package com.bhavadeep.googleclustering.ui.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bhavadeep.googleclustering.models.Result;
import com.bhavadeep.googleclustering.R;
import com.bhavadeep.googleclustering.ui.adapters.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment{
    Context context;
    List<Result> resultList;
    RecyclerAdapter recyclerAdapter;

    public ListFragment() {
        resultList = new ArrayList<>();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_places, container, false);
        RecyclerView rcv = rootView.findViewById(R.id.recycler_grid_view);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
         recyclerAdapter = new RecyclerAdapter(context, resultList);
        rcv.setAdapter(recyclerAdapter);
        rcv.setLayoutManager(layoutManager);
        return rootView;
    }

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    public void updateView(List<Result> results) {
        resultList.addAll(results);

    }
}
