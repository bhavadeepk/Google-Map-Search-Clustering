package com.bhavadeep.googleclustering.Presenter;

import com.bhavadeep.googleclustering.Models.Result;

import java.util.List;

/**
 * Created by ${Bhavadeep} on 12/12/2017.
 */

public interface OnLoadFinishListener {
    public void OnLoadFinish(List<Result> results);
    public void OnLoadFailed(String message);
}
