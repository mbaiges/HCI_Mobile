package ar.edu.itba.hci.uzr.intellifox.api.models.commands.ac;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class SetVerticalSwingAcCommand extends DeviceCommand {
    static {
        actionName = "setVerticalSwing";
    }

    public SetVerticalSwingAcCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
