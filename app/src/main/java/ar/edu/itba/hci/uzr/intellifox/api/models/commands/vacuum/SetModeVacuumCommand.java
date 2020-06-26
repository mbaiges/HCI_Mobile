package ar.edu.itba.hci.uzr.intellifox.api.models.commands.vacuum;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class SetModeVacuumCommand extends DeviceCommand {
    static {
        actionName = "setMode";
    }

    public SetModeVacuumCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
