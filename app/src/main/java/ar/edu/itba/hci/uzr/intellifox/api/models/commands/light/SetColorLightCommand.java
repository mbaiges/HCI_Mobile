package ar.edu.itba.hci.uzr.intellifox.api.models.commands.light;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class SetColorLightCommand extends DeviceCommand {
    static {
        actionName = "setColor";
    }

    public SetColorLightCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
