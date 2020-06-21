package ar.edu.itba.hci.uzr.intellifox.ui.devices;

public class DeviceObserverViewFactoryContainer {
    static private DeviceObserverViewFactory instance;

    private DeviceObserverViewFactoryContainer() {}

    static public DeviceObserverViewFactory getInstance() {
        if (instance == null) {
            instance = new DeviceObserverViewFactory();
        }
        return instance;
    }
}
