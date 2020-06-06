package ar.edu.itba.hci.uzr.intellifox.ui.help_and_feed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HelpAndFeedViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HelpAndFeedViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is help and feedback fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}