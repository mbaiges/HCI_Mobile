package ar.edu.itba.hci.uzr.intellifox.api.models.commands.speaker;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class TurnOnSpeakerCommand extends DeviceCommand {
    static {
        actionName = "turnOn";
    }

    public TurnOnSpeakerCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
