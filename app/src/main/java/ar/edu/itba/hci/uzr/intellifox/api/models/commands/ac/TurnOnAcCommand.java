package ar.edu.itba.hci.uzr.intellifox.api.models.commands.ac;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class TurnOnAcCommand extends DeviceCommand {
    static {
        actionName = "turnOn";
    }

    public TurnOnAcCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
