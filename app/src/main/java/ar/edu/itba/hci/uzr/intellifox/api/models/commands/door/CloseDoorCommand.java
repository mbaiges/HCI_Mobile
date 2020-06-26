package ar.edu.itba.hci.uzr.intellifox.api.models.commands.door;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class CloseDoorCommand extends DeviceCommand {
    static {
        actionName = "close";
    }

    public CloseDoorCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
