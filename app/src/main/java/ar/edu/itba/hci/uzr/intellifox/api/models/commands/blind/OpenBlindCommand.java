package ar.edu.itba.hci.uzr.intellifox.api.models.commands.blind;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class OpenBlindCommand extends DeviceCommand {
    static {
        actionName = "open";
    }

    public OpenBlindCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
