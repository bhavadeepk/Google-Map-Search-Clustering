package com.bhavadeep.googleclustering.Presenter;

import com.bhavadeep.googleclustering.Models.Result;

import java.util.List;

/**
 * Created by ${Bhavadeep} on 12/12/2017.
 */

public class MainPresenter implements OnLoadFinshListener, IViewPresenter {

    IModelInteractor interactor;
    IViewUpdater updater;

    public MainPresenter(IViewUpdater viewUpdater) {
        updater = viewUpdater;
        interactor = new Interactor(this);
    }

    @Override
    public void OnLoadFinsh(List<Result> results) {
        updater.updateView(results);
    }

    @Override
    public void OnLoadFailed() {

    }

    @Override
    public void getResults(String query){
        interactor.loadResults(query);

    }
}
