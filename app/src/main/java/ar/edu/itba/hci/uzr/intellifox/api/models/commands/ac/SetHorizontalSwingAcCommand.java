package ar.edu.itba.hci.uzr.intellifox.api.models.commands.ac;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class SetHorizontalSwingAcCommand extends DeviceCommand {
    static {
        actionName = "setHorizontalSwing";
    }

    public SetHorizontalSwingAcCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
