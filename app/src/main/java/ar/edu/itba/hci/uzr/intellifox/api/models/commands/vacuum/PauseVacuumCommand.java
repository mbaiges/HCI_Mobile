package ar.edu.itba.hci.uzr.intellifox.api.models.commands.vacuum;

import ar.edu.itba.hci.uzr.intellifox.api.models.commands.DeviceCommand;

public class PauseVacuumCommand extends DeviceCommand {
    static {
        actionName = "pause";
    }

    public PauseVacuumCommand(String deviceId, String[] params) {
        super(deviceId, params);
    }
}
