package ar.edu.itba.hci.uzr.intellifox.api.models.commands.blind;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class CloseBlindCommand extends DeviceCommand {
    static {
        actionName = "close";
    }

    public CloseBlindCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
