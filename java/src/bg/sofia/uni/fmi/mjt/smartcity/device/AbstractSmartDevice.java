package bg.sofia.uni.fmi.mjt.smartcity.device;

import bg.sofia.uni.fmi.mjt.smartcity.enums.DeviceType;

import java.time.LocalDateTime;

public abstract sealed class AbstractSmartDevice implements SmartDevice permits SmartTrafficLight, SmartLamp, SmartCamera {
    private String id;
    private final String name;
    private final double powerConsumption;
    private final LocalDateTime installationDateTime;
    protected DeviceType type;

    public AbstractSmartDevice(String name, double powerConsumption, LocalDateTime installationDateTime) {
        this.name = name;
        this.powerConsumption = powerConsumption;
        this.installationDateTime = installationDateTime;
        this.setDeviceType();
        this.setId();
    }

    protected abstract void setDeviceType();

    protected abstract int getQuantity();

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public double getPowerConsumption() {
        return this.powerConsumption;
    }

    public LocalDateTime getInstallationDateTime() {
        return this.installationDateTime;
    }

    public DeviceType getType() {
        return this.type;
    }

    private void setId() {
        this.id = this.getType().getShortName() + "-" + this.getName() + "-" + this.getQuantity();
    }
}
