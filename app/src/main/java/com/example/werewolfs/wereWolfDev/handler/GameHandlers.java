package com.example.werewolfs.wereWolfDev.handler;


import com.example.werewolfs.wereWolfDev.model.GameDataModel;
import com.example.werewolfs.wereWolfDev.viewModel.GameViewModel;

import java.util.ArrayList;
import java.util.List;

public class GameHandlers {

    private GameViewModel viewModel;
    private GameDataModel gameDataModel;

    private List<Integer> tempGroup = new ArrayList<>();

    public GameHandlers(GameViewModel viewModel){
        this.viewModel = viewModel;
        this.gameDataModel = viewModel.getDataModel();
    }

}