package ar.edu.itba.hci.uzr.intellifox.api.models.commands.vacuum;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class StartVacuumCommand extends DeviceCommand {
    static {
        actionName = "start";
    }

    public StartVacuumCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
