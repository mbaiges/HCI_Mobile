package ar.edu.itba.hci.uzr.intellifox.api;

import java.util.List;

import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.DoorDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.room.Room;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.routine.Routine;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @POST("rooms")
    @Headers("Content-Type: application/json")
    Call<Result<Room>> addRoom(@Body Room room);

    @PUT("rooms/{roomId}")
    @Headers("Content-Type: application/json")
    Call<Result<Boolean>> modifyRoom(@Path("roomId") String roomId, @Body Room room);

    @DELETE("rooms/{roomId}")
    Call<Result<Boolean>> deleteRoom(@Path("roomId") String roomId);

    @GET("rooms/{roomId}")
    Call<Result<Room>> getRoom(@Path("roomId") String roomId);

    @GET("rooms")
    Call<Result<List<Room>>> getRooms();

    @GET("devices/{deviceId}")
    Call<Result<Device>> getDevice(@Path("deviceId") String deviceId);

    @GET("devices")
    Call<Result<List<Device>>> getDevices();

    @PUT("devices/{deviceId}/{actionName}")
    Call<Result<Object>> executeDeviceAction(@Path("deviceId") String deviceId, @Path("actionName") String actionName, @Body String[] params);

    @GET("routines")
    Call<Result<List<Routine>>> getRoutines();

    @PUT("routines/{routineId}/execute")
    Call<Result<Boolean>> executeRoutine(@Path("routineId") String routineId);

    @GET("devices/{deviceId}/state")
    Call<Result<DoorDeviceState>> getDoorDeviceState(@Path("deviceId") String deviceId);

//    @GET("devices/{deviceId}/state")
//    Call<Result<OvenDeviceState>> getOvenDeviceState(@Path("deviceId") String deviceId);

//    @GET("devices/{deviceId}/state")
//    Call<Result<AcDeviceState>> getAcDeviceState(@Path("deviceId") String deviceId);

//    @GET("devices/{deviceId}/state")
//    Call<Result<BlindDeviceState>> getBlindDeviceState(@Path("deviceId") String deviceId);

//    @GET("devices/{deviceId}/state")
//    Call<Result<LightDeviceState>> getLightDeviceState(@Path("deviceId") String deviceId);

//    @GET("devices/{deviceId}/state")
//    Call<Result<SpeakerDeviceState>> getSpeakerDeviceState(@Path("deviceId") String deviceId);

//    @GET("devices/{deviceId}/state")
//    Call<Result<TapDeviceState>> getTapDeviceState(@Path("deviceId") String deviceId);

//    @GET("devices/{deviceId}/state")
//    Call<Result<VacuumDeviceState>> getVacuumDeviceState(@Path("deviceId") String deviceId);


}
