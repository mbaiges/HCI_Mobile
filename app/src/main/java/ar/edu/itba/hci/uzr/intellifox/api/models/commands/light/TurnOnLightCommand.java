package ar.edu.itba.hci.uzr.intellifox.api.models.commands.light;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class TurnOnLightCommand extends DeviceCommand {
    static {
        actionName = "turnOn";
    }

    public TurnOnLightCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
