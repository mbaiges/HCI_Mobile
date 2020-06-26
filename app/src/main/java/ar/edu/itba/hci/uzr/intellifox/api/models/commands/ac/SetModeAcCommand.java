package ar.edu.itba.hci.uzr.intellifox.api.models.commands.ac;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class SetModeAcCommand extends DeviceCommand {
    static {
        actionName = "setMode";
    }

    public SetModeAcCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
