package ar.edu.itba.hci.uzr.intellifox.api.models.commands.blind;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class SetLevelBlindCommand extends DeviceCommand {
    static {
        actionName = "setLevel";
    }

    public SetLevelBlindCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
