package com.bhavadeep.googleclustering.presenter;

import com.bhavadeep.googleclustering.models.Result;
import com.bhavadeep.googleclustering.reftrofit.Interactor;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by ${Bhavadeep} on 12/12/2017.
 */

public class MainPresenter implements IViewPresenter {

    private IModelInteractor interactor;
    private IViewUpdater updater;
    private EventBus bus;


    public MainPresenter(IViewUpdater viewUpdater) {
        updater = viewUpdater;
        bus = EventBus.getDefault();
        bus.register(this);
    }


    @Override
    public void getResults(String query){
        interactor = new Interactor();
        interactor.loadResults(query);

    }

    public void onEvent(List<Result> results) {
        updater.updateView(results);
    }

    public void onEvent(String failed) {
        updater.updateFailed(failed);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        bus.unregister(this);
    }
}
