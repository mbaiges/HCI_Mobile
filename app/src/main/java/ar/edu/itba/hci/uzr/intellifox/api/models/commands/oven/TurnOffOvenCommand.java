package ar.edu.itba.hci.uzr.intellifox.api.models.commands.oven;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class TurnOffOvenCommand extends DeviceCommand {
    static {
        actionName = "turnOff";
    }

    public TurnOffOvenCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
