package com.test.gotovjet.burlaka.socialviewer.presenter.facebook;

import com.test.gotovjet.burlaka.socialviewer.model.UserInfo;
import com.test.gotovjet.burlaka.socialviewer.view.EditView;

/**
 * Created by Operator on 29.08.2016.
 */
public class FBUpdateInfo implements FBUpdateInfoPresenter{
    EditView editView;
    UserInfo userModel;

    public FBUpdateInfo(EditView editView) {
        this.editView = editView;
        //Get singleton with updating data
        userModel = UserInfo.getInstance();
    }


    @Override
    public void updateRequest (){
       // Can't to change user information with request.
        // You need to manually use the Official Facebook app

    }


    @Override
    public void onDestroy() {
        editView = null;
    }

}
