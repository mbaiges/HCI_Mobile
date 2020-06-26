package ar.edu.itba.hci.uzr.intellifox.api.models.commands.ac;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class SetFanSpeedAcCommand extends DeviceCommand {
    static {
        actionName = "setFanSpeed";
    }

    public SetFanSpeedAcCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
