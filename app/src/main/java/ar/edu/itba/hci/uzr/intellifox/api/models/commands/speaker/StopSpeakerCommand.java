package ar.edu.itba.hci.uzr.intellifox.api.models.commands.speaker;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class StopSpeakerCommand extends DeviceCommand {
    static {
        actionName = "stop";
    }

    public StopSpeakerCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
