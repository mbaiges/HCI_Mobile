package ar.edu.itba.hci.uzr.intellifox.api.models.commands.speaker;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class NextSongSpeakerCommand extends DeviceCommand {
    static {
        actionName = "nextSong";
    }

    public NextSongSpeakerCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
