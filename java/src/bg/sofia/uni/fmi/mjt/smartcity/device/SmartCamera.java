package bg.sofia.uni.fmi.mjt.smartcity.device;

import bg.sofia.uni.fmi.mjt.smartcity.enums.DeviceType;

import java.time.LocalDateTime;

public non-sealed class SmartCamera extends AbstractSmartDevice {
    private static int cameraQuantity = 0;

    public SmartCamera(String name, double powerConsumption, LocalDateTime installationDateTime) {
        super(name, powerConsumption, installationDateTime);
        this.type = DeviceType.CAMERA;
    }

    protected int getQuantity() {
        cameraQuantity++;
        return SmartCamera.cameraQuantity - 1;
    }

    protected void setDeviceType() {
        this.type = DeviceType.CAMERA;
    }
}
