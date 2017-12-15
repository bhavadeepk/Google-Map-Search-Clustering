package com.bhavadeep.googleclustering.presenter;

import com.bhavadeep.googleclustering.models.Result;
import com.bhavadeep.googleclustering.reftrofit.Interactor;

import java.util.List;

/**
 * Created by ${Bhavadeep} on 12/12/2017.
 */

public class MainPresenter implements IViewPresenter {

    private IModelInteractor interactor;
    private IViewUpdater updater;
    private OnLoadFinishListener onModelLoadListner;


    public MainPresenter(IViewUpdater viewUpdater) {
        updater = viewUpdater;
    }


    @Override
    public void getResults(String query){
        onModelLoadListner = new OnLoadFinishListener() {
            @Override
            public void OnLoadFinish(List<Result> results) {
                updater.updateView(results);
            }

            @Override
            public void OnLoadFailed(String message) {
                updater.updateFailed(message);
            }
        };
        interactor = new Interactor(onModelLoadListner);
        interactor.loadResults(query);

    }
}
