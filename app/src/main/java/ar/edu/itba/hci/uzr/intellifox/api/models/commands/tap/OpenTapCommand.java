package ar.edu.itba.hci.uzr.intellifox.api.models.commands.tap;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class OpenTapCommand extends DeviceCommand {
    static {
        actionName = "open";
    }

    public OpenTapCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
