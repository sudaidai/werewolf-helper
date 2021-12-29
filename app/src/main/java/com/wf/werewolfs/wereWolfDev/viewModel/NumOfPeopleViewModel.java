package com.wf.werewolfs.wereWolfDev.viewModel;

import com.wf.werewolfs.wereWolfDev.model.DataModel;

public class NumOfPeopleViewModel {

    public void setPeoCnt(int peoCnt){
        DataModel.getInstance().setPeoCnt(peoCnt);
    }

}
