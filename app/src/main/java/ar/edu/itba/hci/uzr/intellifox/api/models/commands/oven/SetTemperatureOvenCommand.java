package ar.edu.itba.hci.uzr.intellifox.api.models.commands.oven;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class SetTemperatureOvenCommand extends DeviceCommand {
    static {
        actionName = "setTemperature";
    }

    public SetTemperatureOvenCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
