package ar.edu.itba.hci.uzr.intellifox.api;

import com.google.gson.GsonBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import ar.edu.itba.hci.uzr.intellifox.api.Error;
import ar.edu.itba.hci.uzr.intellifox.api.ErrorResult;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceDeserializer;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.AcDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.BlindDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.DoorDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.LightDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.OvenDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.SpeakerDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.TapDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.VacuumDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.room.Room;
import ar.edu.itba.hci.uzr.intellifox.api.models.routine.Routine;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private Retrofit retrofit = null;
    private ApiService service = null;
    private static ApiClient instance = null;
    // Use IP 10.0.2.2 instead of 127.0.0.1 when running Android emulator in the
    // same computer that runs the API.
    private final String BaseURL = "http://10.0.2.2:8080/api/";

    private ApiClient() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();

        gsonBuilder.registerTypeAdapter(Device.class, new DeviceDeserializer());
        Gson gson = gsonBuilder.create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        this.service = retrofit.create(ApiService.class);
    }

    public static synchronized ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    public Error getError(ResponseBody response) {
        Converter<ResponseBody, ErrorResult> errorConverter =
                this.retrofit.responseBodyConverter(ErrorResult.class, new Annotation[0]);
        try {
            ErrorResult responseError = errorConverter.convert(response);
            return responseError.getError();
        } catch (IOException e) {
            return null;
        }
    }

    public Call<Result<Room>> addRoom(Room room, Callback<Result<Room>> callback) {
        Call<Result<Room>> call = this.service.addRoom(room);
        call.enqueue(callback);
        return call;
    }

    public Call<Result<Boolean>> modifyRoom(Room room, Callback<Result<Boolean>> callback) {
        Call<Result<Boolean>> call = this.service.modifyRoom(room.getId(), room);
        call.enqueue(callback);
        return call;
    }

    public Call<Result<Boolean>> deleteRoom(String roomId, Callback<Result<Boolean>> callback) {
        Call<Result<Boolean>> call = this.service.deleteRoom(roomId);
        call.enqueue(callback);
        return call;
    }

    public Call<Result<Room>> getRoom(String roomId, Callback<Result<Room>> callback) {
        Call<Result<Room>> call = this.service.getRoom(roomId);
        call.enqueue(callback);
        return call;
    }

    public Call<Result<List<Room>>> getRooms(Callback<Result<List<Room>>> callback) {
        Call<Result<List<Room>>> call = this.service.getRooms();
        call.enqueue(callback);
        return call;
    }

    public Call<Result<Device>> getDevice(String deviceId, Callback<Result<Device>> callback) {
        Call<Result<Device>> call = this.service.getDevice(deviceId);
        call.enqueue(callback);
        return call;
    }

    public Call<Result<List<Device>>> getDevices(Callback<Result<List<Device>>> callback) {
        Call<Result<List<Device>>> call = this.service.getDevices();
        call.enqueue(callback);
        return call;
    }

    public Call<Result<List<Routine>>> getRoutines(Callback<Result<List<Routine>>> callback) {
        Call<Result<List<Routine>>> call = this.service.getRoutines();
        call.enqueue(callback);
        return call;
    }

    public Call<Result<Boolean>> executeRoutine(String routineId, Callback<Result<Boolean>> callback) {
        Call<Result<Boolean>> call = this.service.executeRoutine(routineId);
        call.enqueue(callback);
        return call;
    }

    public Call<Result<Object>> executeDeviceAction(String deviceId, String actionName, String[] params, Callback<Result<Object>> callback) {
        Call<Result<Object>> call = this.service.executeDeviceAction(deviceId, actionName, params);
        call.enqueue(callback);
        return call;
    }

    public Call<Result<DoorDeviceState>> getDoorDeviceState(String deviceId, Callback<Result<DoorDeviceState>> callback) {
        Call<Result<DoorDeviceState>> call = this.service.getDoorDeviceState(deviceId);
        call.enqueue(callback);
        return call;
    }
//
//    public Call<Result<OvenDeviceState>> getOvenDeviceState(String deviceId, Callback<Result<OvenDeviceState>> callback) {
//        Call<Result<OvenDeviceState>> call = this.service.getOvenDeviceState(deviceId);
//        call.enqueue(callback);
//        return call;
//    }
//
//    public Call<Result<AcDeviceState>> getAcDeviceState(String deviceId, Callback<Result<AcDeviceState>> callback) {
//        Call<Result<AcDeviceState>> call = this.service.getAcDeviceState(deviceId);
//        call.enqueue(callback);
//        return call;
//    }
//
//
// public Call<Result<BlindDeviceState>> getBlindsDeviceState(String deviceId, Callback<Result<BlindDeviceState>> callback) {
//        Call<Result<BlindDeviceState>> call = this.service.getBlindDeviceState(deviceId);
//        call.enqueue(callback);
//        return call;
//    }
//
//public Call<Result<LightDeviceState>> getLightDeviceState(String deviceId, Callback<Result<LightDeviceState>> callback) {
//        Call<Result<LightDeviceState>> call = this.service.getLightDeviceState(deviceId);
//        call.enqueue(callback);
//        return call;
//    }
//
//public Call<Result<SpeakerDeviceState>> getSpeakerDeviceState(String deviceId, Callback<Result<SpeakerDeviceState>> callback) {
//        Call<Result<SpeakerDeviceState>> call = this.service.getSpeakerDeviceState(deviceId);
//        call.enqueue(callback);
//        return call;
//    }
//
//public Call<Result<TapDeviceState>> getTapDeviceState(String deviceId, Callback<Result<TapDeviceState>> callback) {
//        Call<Result<TapDeviceState>> call = this.service.getTapDeviceState(deviceId);
//        call.enqueue(callback);
//        return call;
//    }
//
//public Call<Result<VacuumDeviceState>> getVacuumDeviceState(String deviceId, Callback<Result<VacuumDeviceState>> callback) {
//        Call<Result<VacuumDeviceState>> call = this.service.getVacuumDeviceState(deviceId);
//        call.enqueue(callback);
//        return call;
//    }
//
}
