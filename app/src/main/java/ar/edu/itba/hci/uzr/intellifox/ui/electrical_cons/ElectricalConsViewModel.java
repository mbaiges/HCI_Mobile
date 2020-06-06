package ar.edu.itba.hci.uzr.intellifox.ui.electrical_cons;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ElectricalConsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ElectricalConsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is electrical consumption fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}