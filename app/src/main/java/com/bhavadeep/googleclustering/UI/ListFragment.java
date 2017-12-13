package com.bhavadeep.googleclustering.UI;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bhavadeep.googleclustering.Models.Result;
import com.bhavadeep.googleclustering.Presenter.IViewUpdater;
import com.bhavadeep.googleclustering.Presenter.MainPresenter;
import com.bhavadeep.googleclustering.R;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment implements IViewUpdater {
    Context context;
    List<Result> resultList;
    MainPresenter presenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resultList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_places, container, false);
        RecyclerView rcv = rootView.findViewById(R.id.recycler_grid_view);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter();
        rcv.setAdapter(recyclerAdapter);
        return rootView;
    }

    public static ListFragment newInstance() {
        ListFragment listFragment = new ListFragment();
        return listFragment;
    }

    @Override
    public void updateView(List<Result> results) {
        resultList.addAll(results);
    }
}
