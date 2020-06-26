package ar.edu.itba.hci.uzr.intellifox.api.models.commands.door;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class UnlockDoorCommand extends DeviceCommand {
    static {
        actionName = "unlock";
    }

    public UnlockDoorCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
