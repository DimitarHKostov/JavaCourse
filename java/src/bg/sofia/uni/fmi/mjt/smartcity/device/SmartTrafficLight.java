package bg.sofia.uni.fmi.mjt.smartcity.device;

import bg.sofia.uni.fmi.mjt.smartcity.enums.DeviceType;

import java.time.LocalDateTime;

public non-sealed class SmartTrafficLight extends AbstractSmartDevice {
    private static int trafficLightQuantity = 0;

    public SmartTrafficLight(String name, double powerConsumption, LocalDateTime installationDateTime) {
        super(name, powerConsumption, installationDateTime);
    }

    protected int getQuantity() {
        trafficLightQuantity++;
        return SmartTrafficLight.trafficLightQuantity - 1;
    }

    protected void setDeviceType() {
        this.type = DeviceType.TRAFFIC_LIGHT;
    }
}
