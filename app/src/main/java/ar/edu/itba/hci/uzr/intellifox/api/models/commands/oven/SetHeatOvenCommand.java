package ar.edu.itba.hci.uzr.intellifox.api.models.commands.oven;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class SetHeatOvenCommand extends DeviceCommand {
    static {
        actionName = "setHeat";
    }

    public SetHeatOvenCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
