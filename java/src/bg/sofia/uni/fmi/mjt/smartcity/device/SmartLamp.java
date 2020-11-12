package bg.sofia.uni.fmi.mjt.smartcity.device;

import bg.sofia.uni.fmi.mjt.smartcity.enums.DeviceType;

import java.time.LocalDateTime;

public non-sealed class SmartLamp extends AbstractSmartDevice {
    private static int lampQuantity = 0;

    public SmartLamp(String name, double powerConsumption, LocalDateTime installationDateTime) {
        super(name, powerConsumption, installationDateTime);
        this.type = DeviceType.LAMP;
    }

    protected int getQuantity() {
        lampQuantity++;
        return SmartLamp.lampQuantity-1;
    }

    protected void setDeviceType() {
        this.type = DeviceType.LAMP;
    }
}
