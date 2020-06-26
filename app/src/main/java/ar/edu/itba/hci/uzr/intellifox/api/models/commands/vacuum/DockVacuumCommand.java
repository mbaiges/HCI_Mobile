package ar.edu.itba.hci.uzr.intellifox.api.models.commands.vacuum;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class DockVacuumCommand extends DeviceCommand {
    static {
        actionName = "dock";
    }

    public DockVacuumCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
