package ar.edu.itba.hci.uzr.intellifox.api.models.commands.oven;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class TurnOnOvenCommand extends DeviceCommand {
    static {
        actionName = "turnOn";
    }

    public TurnOnOvenCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
