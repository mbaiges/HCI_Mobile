package ar.edu.itba.hci.uzr.intellifox.api.models.commands.oven;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class SetGrillOvenCommand extends DeviceCommand {
    static {
        actionName = "setGrill";
    }

    public SetGrillOvenCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
