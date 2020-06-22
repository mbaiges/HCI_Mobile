package ar.edu.itba.hci.uzr.intellifox.ui.devices;

import android.util.Pair;
import android.view.View;

import androidx.lifecycle.Observer;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.ui.devices.ac.ACDeviceObserver;
import ar.edu.itba.hci.uzr.intellifox.ui.devices.blind.BlindDeviceObserver;
import ar.edu.itba.hci.uzr.intellifox.ui.devices.door.DoorDeviceObserver;
import ar.edu.itba.hci.uzr.intellifox.ui.devices.tap.TapDeviceObserver;

public class DeviceObserverViewFactory {

    private HashMap<String, Pair<Integer, Class>> map;

    public DeviceObserverViewFactory() {
        map = new HashMap<String, Pair<Integer, Class>>() {{
            put("ac", new Pair<>(R.layout.fragment_device_ac, ACDeviceObserver.class));
            put("blinds", new Pair<>(R.layout.fragment_device_blinds, BlindDeviceObserver.class));
            put("faucet", new Pair<>(R.layout.fragment_device_tap, TapDeviceObserver.class));
            put("door", new Pair<>(R.layout.fragment_device_door, DoorDeviceObserver.class));
            put("vacuum", new Pair<>(R.layout.fragment_device_vacuum, ACDeviceObserver.class));
            put("speaker", new Pair<>(R.layout.fragment_device_speaker, ACDeviceObserver.class));
            put("oven", new Pair<>(R.layout.fragment_device_oven, ACDeviceObserver.class));
            put("light", new Pair<>(R.layout.fragment_device_light, ACDeviceObserver.class));
        }};
    }

    public Integer getView(String typeName) {
      Pair<Integer, Class> p =  map.get(typeName);
      if (p != null) {
          return p.first;
      }
      return null;
    }

    public Observer<Device> getObserver(String typeName, View contextView) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Pair<Integer, Class> p =  map.get(typeName);
        if (p != null) {
            return (Observer<Device>) p.second.getDeclaredConstructor(View.class).newInstance(contextView);
        }
        return null;
    }
}
