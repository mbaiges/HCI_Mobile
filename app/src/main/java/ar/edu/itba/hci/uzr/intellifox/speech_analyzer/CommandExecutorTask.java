package ar.edu.itba.hci.uzr.intellifox.speech_analyzer;

import android.os.AsyncTask;
import android.util.Log;

import java.text.Normalizer;

public class CommandExecutorTask extends AsyncTask<Void, Void, Void> {

    static final String COMMAND_TASK_TAG = "Command_Task";

    private String command;

    public CommandExecutorTask(String command) {
        String s = Normalizer.normalize(command, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        this.command = s.toLowerCase();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (command.equals("la juana es tres gatos")) {
            Log.d(COMMAND_TASK_TAG, "This is what i wanted so far");
        }
        return null;
    }
}
