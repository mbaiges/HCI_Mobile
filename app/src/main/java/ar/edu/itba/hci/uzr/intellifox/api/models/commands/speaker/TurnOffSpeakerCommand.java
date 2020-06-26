package ar.edu.itba.hci.uzr.intellifox.api.models.commands.speaker;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class TurnOffSpeakerCommand extends DeviceCommand {
    static {
        actionName = "turnOff";
    }

    public TurnOffSpeakerCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
