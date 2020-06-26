package ar.edu.itba.hci.uzr.intellifox.api.models.commands.light;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class SetBrightnessLightCommand extends DeviceCommand {
    static {
        actionName = "setBrightness";
    }

    public SetBrightnessLightCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
