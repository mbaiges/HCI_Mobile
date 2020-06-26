package ar.edu.itba.hci.uzr.intellifox.api.models.commands.speaker;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class SetGenreSpeakerCommand extends DeviceCommand {
    static {
        actionName = "setGenre";
    }

    public SetGenreSpeakerCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
