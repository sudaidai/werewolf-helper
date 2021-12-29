package com.example.werewolfs.wereWolfDev.viewModel;

import com.example.werewolfs.wereWolfDev.model.DataModel;

public class NumOfPeopleViewModel {

    public void setPeoCnt(int peoCnt){
        DataModel.getInstance().setPeoCnt(peoCnt);
    }

}
