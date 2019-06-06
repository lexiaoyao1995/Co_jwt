package com.swjtu.mapper;

import com.swjtu.model.History;

import java.util.List;
public interface HistoryMapper {

    List<History> getAll();
    void insert(History history);

}
