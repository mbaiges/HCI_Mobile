package ar.edu.itba.hci.uzr.intellifox.api.models.commands.door;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class LockDoorCommand extends DeviceCommand {
    static {
        actionName = "lock";
    }

    public LockDoorCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
