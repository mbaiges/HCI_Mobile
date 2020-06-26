package ar.edu.itba.hci.uzr.intellifox.api.models.commands.door;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class OpenDoorCommand extends DeviceCommand {
    static {
        actionName = "open";
    }

    public OpenDoorCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
