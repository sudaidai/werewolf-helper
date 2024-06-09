package com.wf.werewolfs.android.viewModel;

import com.wf.werewolfs.android.model.DataModel;

public class NumOfPeopleViewModel {

    public void setPeoCnt(int peoCnt) {
        DataModel.getInstance().setPeoCnt(peoCnt);
    }

}
