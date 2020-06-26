package ar.edu.itba.hci.uzr.intellifox.speech_analyzer;

import android.os.AsyncTask;
import android.util.Log;

import java.text.Normalizer;

import ar.edu.itba.hci.uzr.intellifox.path_highlighter.PathHighlighter;

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
        if (command.equals("highlight path on") || command.equals("ilumina camino encendido")) {
            highlightMyWay(true);
        }
        else if (command.equals("highlight path off") || command.equals("ilumina camino apagado")) {
            highlightMyWay(false);
        }


        return null;
    }

    private void highlightMyWay(boolean intentSwitch) {
        Log.d("HIGHLIGHTER", "Entered");
        PathHighlighter.getInstance().highlightPath(intentSwitch);
    }
}
