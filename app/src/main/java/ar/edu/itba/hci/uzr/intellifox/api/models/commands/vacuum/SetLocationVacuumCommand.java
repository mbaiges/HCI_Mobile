package ar.edu.itba.hci.uzr.intellifox.api.models.commands.vacuum;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class SetLocationVacuumCommand extends DeviceCommand {
    static {
        actionName = "setLocation";
    }

    public SetLocationVacuumCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
