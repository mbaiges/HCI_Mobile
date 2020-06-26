package ar.edu.itba.hci.uzr.intellifox.api.models.commands.speaker;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class SetVolumeSpeakerCommand extends DeviceCommand {
    static {
        actionName = "setVolume";
    }

    public SetVolumeSpeakerCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
