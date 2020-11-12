package bg.sofia.uni.fmi.mjt.smartcity.hub;

import bg.sofia.uni.fmi.mjt.smartcity.device.SmartDevice;
import bg.sofia.uni.fmi.mjt.smartcity.enums.DeviceType;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.Map;
import java.util.LinkedHashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Queue;
import java.util.Objects;
import java.util.Collection;
import java.util.PriorityQueue;

public class SmartCityHub {
    private final Set<SmartDevice> devices;
    private final Map<DeviceType, Integer> occurrences;

    public SmartCityHub() {
        this.devices = new LinkedHashSet<>();
        this.occurrences = new HashMap<>();
        occurrences.put(DeviceType.CAMERA, 0);
        occurrences.put(DeviceType.LAMP, 0);
        occurrences.put(DeviceType.TRAFFIC_LIGHT, 0);
    }

    /**
     * Adds a @device to the SmartCityHub.
     *
     * @throws IllegalArgumentException         in case @device is null.
     * @throws DeviceAlreadyRegisteredException in case the @device is already registered.
     */
    public void register(SmartDevice device) throws DeviceAlreadyRegisteredException {
        if (device == null) {
            throw new IllegalArgumentException();
        }

        if (this.devices.contains(device)) {
            throw new DeviceAlreadyRegisteredException();
        }

        this.devices.add(device);
        this.occurrences.replace(device.getType(), this.occurrences.get(device.getType()) + 1);
    }

    /**
     * Removes the @device from the SmartCityHub.
     *
     * @throws IllegalArgumentException in case null is passed.
     * @throws DeviceNotFoundException  in case the @device is not found.
     */
    public void unregister(SmartDevice device) throws DeviceNotFoundException {
        if (device == null) {
            throw new IllegalArgumentException();
        }

        final Iterator<SmartDevice> it = this.devices.iterator();
        while (it.hasNext()) {
            SmartDevice currentDevice = it.next();
            if (currentDevice == device) {
                it.remove();
                return;
            }
        }

        throw new DeviceNotFoundException();
    }

    /**
     * Returns a SmartDevice with an ID @id.
     *
     * @throws IllegalArgumentException in case @id is null.
     * @throws DeviceNotFoundException  in case device with ID @id is not found.
     */
    public SmartDevice getDeviceById(String id) throws DeviceNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        for (SmartDevice currentDevice : this.devices) {
            if (currentDevice.getId().equals(id)) {
                return currentDevice;
            }
        }

        throw new DeviceNotFoundException();
    }

    /**
     * Returns the total number of devices with type @type registered in SmartCityHub.
     *
     * @throws IllegalArgumentException in case @type is null.
     */
    public int getDeviceQuantityPerType(DeviceType type) {
        if (type == null) {
            throw new IllegalArgumentException();
        }

        return this.occurrences.get(type);
    }

    /**
     * Returns a collection of IDs of the top @n devices which consumed
     * the most power from the time of their installation until now.
     * <p>
     * The total power consumption of a device is calculated by the hours elapsed
     * between the two LocalDateTime-s: the installation time and the current time (now)
     * multiplied by the stated nominal hourly power consumption of the device.
     * <p>
     * If @n exceeds the total number of devices, return all devices available sorted by the given criterion.
     *
     * @throws IllegalArgumentException in case @n is a negative number.
     */
    public Collection<String> getTopNDevicesByPowerConsumption(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }

        return n > this.devices.size() ?
                this.topNBasedOnConsumption(this.devices.size()) : this.topNBasedOnConsumption(n);
    }

    private Collection<String> topNBasedOnConsumption(int n) {
        Queue<SmartDevice> queue = new PriorityQueue<>(this::calculatePriority);
        queue.addAll(this.devices);

        Set<String> topN = new LinkedHashSet<>();
        int i = 0;
        while (i++ < n) {
            topN.add(Objects.requireNonNull(queue.poll()).getId());
        }

        return topN;
    }

    private int calculatePriority(SmartDevice firstDevice, SmartDevice secondDevice) {
        double dev1powerCons = firstDevice.getPowerConsumption();
        double dev2powerCons = secondDevice.getPowerConsumption();

        long periodDev1 = ChronoUnit.HOURS.between(firstDevice.getInstallationDateTime(), LocalDateTime.now());
        long periodDev2 = ChronoUnit.HOURS.between(secondDevice.getInstallationDateTime(), LocalDateTime.now());

        double consumption1 = dev1powerCons * periodDev1;
        double consumption2 = dev2powerCons * periodDev2;

        return Double.compare(consumption2, consumption1);
    }

    /**
     * Returns a collection of the first @n registered devices, i.e the first @n that were added
     * in the SmartCityHub (registration != installation).
     * <p>
     * If @n exceeds the total number of devices, return all devices available sorted by the given criterion.
     *
     * @throws IllegalArgumentException in case @n is a negative number.
     */
    public Collection<SmartDevice> getFirstNDevicesByRegistration(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }

        if (n > this.devices.size()) {
            return this.devices;
        }

        int i = 0;
        Set<SmartDevice> firstN = new LinkedHashSet<>();
        final Iterator<SmartDevice> it = this.devices.iterator();
        while (it.hasNext() && i++ < n) {
            firstN.add(it.next());
        }

        return firstN;
    }
}
