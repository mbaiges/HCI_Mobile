package ar.edu.itba.hci.uzr.intellifox.api.models.commands.tap;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class DispenseTapCommand extends DeviceCommand {
    static {
        actionName = "dispense";
    }

    public DispenseTapCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
