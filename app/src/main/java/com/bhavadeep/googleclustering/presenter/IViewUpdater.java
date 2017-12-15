package com.bhavadeep.googleclustering.presenter;

import com.bhavadeep.googleclustering.models.Result;

import java.util.List;

/**
 * Created by ${Bhavadeep} on 12/12/2017.
 */

public interface IViewUpdater {

    public void updateView(List<Result> results);

    void updateFailed(String message);
}
