package ar.edu.itba.hci.uzr.intellifox.api.models.commands.tap;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class CloseTapCommand extends DeviceCommand {
    static {
        actionName = "close";
    }

    public CloseTapCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
