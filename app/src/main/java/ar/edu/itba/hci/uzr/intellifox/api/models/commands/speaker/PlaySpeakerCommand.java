package ar.edu.itba.hci.uzr.intellifox.api.models.commands.speaker;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class PlaySpeakerCommand extends DeviceCommand {
    static {
        actionName = "play";
    }

    public PlaySpeakerCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
