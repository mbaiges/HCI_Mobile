package ar.edu.itba.hci.uzr.intellifox.api.models.device;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import ar.edu.itba.hci.uzr.intellifox.api.models.device_type.DeviceType;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.AcDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.AlarmDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.BlindDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.DoorDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.FridgeDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.LightDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.OvenDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.SpeakerDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.TapDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.VacuumDevice;

public class DeviceDeserializer implements JsonDeserializer<Device> {

    @Override
    public Device deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new Gson();
        final JsonObject jsonObject = json.getAsJsonObject();

        final JsonElement jsonElementType = jsonObject.get("type");
        DeviceType deviceType = gson.fromJson(jsonElementType, DeviceType.class);

        switch(deviceType.getId()) {
            case "c89b94e8581855bc":
                return gson.fromJson(jsonObject, new TypeToken<SpeakerDevice>(){}.getType());
            case "dbrlsh7o5sn8ur4i":
                return gson.fromJson(jsonObject, new TypeToken<TapDevice>(){}.getType());
            case "eu0v2xgprrhhg41g":
                return gson.fromJson(jsonObject, new TypeToken<BlindDevice>(){}.getType());
            case "go46xmbqeomjrsjr":
                return gson.fromJson(jsonObject, new TypeToken<LightDevice>(){}.getType());
            case "im77xxyulpegfmv8":
                return gson.fromJson(jsonObject, new TypeToken<OvenDevice>(){}.getType());
            case "li6cbv5sdlatti0j":
                return gson.fromJson(jsonObject, new TypeToken<AcDevice>(){}.getType());
            case "lsf78ly0eqrjbz91":
                return gson.fromJson(jsonObject, new TypeToken<DoorDevice>(){}.getType());
            case "mxztsyjzsrq7iaqc":
                return gson.fromJson(jsonObject, new TypeToken<AlarmDevice>(){}.getType());
            case "ofglvd9gqx8yfl3l":
                return gson.fromJson(jsonObject, new TypeToken<VacuumDevice>(){}.getType());
            case "rnizejqr2di0okho":
                return gson.fromJson(jsonObject, new TypeToken<FridgeDevice>(){}.getType());
            default:
                return null;
        }
    }
}
