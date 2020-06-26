package ar.edu.itba.hci.uzr.intellifox.api.models.commands.ac;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class TurnOffAcCommand extends DeviceCommand {
    static {
        actionName = "turnOff";
    }

    public TurnOffAcCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
