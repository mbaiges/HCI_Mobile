package ar.edu.itba.hci.uzr.intellifox.api.models.commands.ac;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class SetTemperatureAcCommand extends DeviceCommand {
    static {
        actionName = "setTemperature";
    }

    public SetTemperatureAcCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
