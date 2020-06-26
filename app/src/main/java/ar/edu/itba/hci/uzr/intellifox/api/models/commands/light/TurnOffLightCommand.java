package ar.edu.itba.hci.uzr.intellifox.api.models.commands.light;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class TurnOffLightCommand extends DeviceCommand {
    static {
        actionName = "turnOff";
    }

    public TurnOffLightCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
