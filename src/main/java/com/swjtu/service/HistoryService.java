package com.swjtu.service;

import com.swjtu.mapper.HistoryMapper;
import com.swjtu.model.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryService {

    @Autowired
    private HistoryMapper historyMapper;

    public List<History> getAll(){
        return historyMapper.getAll();
    }

    public void insert(History history){
        historyMapper.insert(history);
    }


}
