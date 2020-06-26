package ar.edu.itba.hci.uzr.intellifox.api.models.commands.speaker;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class GetPlaylistSpeakerCommand extends DeviceCommand {
    static {
        actionName = "getPlaylist";
    }

    public GetPlaylistSpeakerCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
