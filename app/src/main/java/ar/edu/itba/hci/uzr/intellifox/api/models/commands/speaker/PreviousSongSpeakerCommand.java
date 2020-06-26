package ar.edu.itba.hci.uzr.intellifox.api.models.commands.speaker;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class PreviousSongSpeakerCommand extends DeviceCommand {
    static {
        actionName = "previousSong";
    }

    public PreviousSongSpeakerCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
