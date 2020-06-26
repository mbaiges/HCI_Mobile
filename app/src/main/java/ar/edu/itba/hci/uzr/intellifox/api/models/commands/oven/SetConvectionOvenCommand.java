package ar.edu.itba.hci.uzr.intellifox.api.models.commands.oven;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class SetConvectionOvenCommand extends DeviceCommand {
    static {
        actionName = "setConvection";
    }

    public SetConvectionOvenCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
