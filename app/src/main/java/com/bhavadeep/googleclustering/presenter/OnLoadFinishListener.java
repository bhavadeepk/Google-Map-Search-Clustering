package com.bhavadeep.googleclustering.presenter;

import com.bhavadeep.googleclustering.models.Result;

import java.util.List;


public interface OnLoadFinishListener {
    void OnLoadFinish(List<Result> results);

    void OnLoadFailed(String message);
}
