package com.example.matti.myapplication.sensor;

import com.example.matti.myapplication.R;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;

import de.greenrobot.event.EventBus;

public class SensorReceiverService {

    // Capability codes shared with the wear application
    private static final String ACCELEROMETER_SENSING_CAPABILITY = "acceleration_sensing";
    private static final String HEART_RATE_SENSING_CAPABILITY = "heart_rate_sensing";

    public enum EventType {
        FALL, HEART_ATTACK, FAINT
    }

    /**
     * Simple event class
     */
    public class Event {
        private EventType type;
        private String message;

        public EventType getType() {
            return type;
        }

        public void setType(EventType type) {
            this.type = type;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    /**
     * Interface used by the client to receive events from the sensors
     */
    public interface SensedEventListener {
        void onEventSensed(Event event);
    }

    private HashMap<EventType, SensedEventListener> listeners;

    public SensorReceiverService() {
        listeners = new HashMap<>();

        EventBus.getDefault().register(this);
    }

    public void setListener(EventType type, SensedEventListener listener) {
        listeners.put(type, listener);
    }

    public void onMessageReceived(byte[] payload) {
        int type = payload[0];

        byte[] data = new byte[payload.length-1];

        for(int i = 0; i < data.length; i++)
            data[i] = payload[i+1];

        if (type == 1) {
            byte[] dataX = {data[0], data[1], data[2]};
            byte[] dataY = {data[3], data[4], data[5]};
            byte[] dataZ = {data[6], data[7], data[8]};

            float x = ByteBuffer.wrap(dataX).order(ByteOrder.LITTLE_ENDIAN).getFloat();
            float y = ByteBuffer.wrap(dataY).order(ByteOrder.LITTLE_ENDIAN).getFloat();
            float z = ByteBuffer.wrap(dataZ).order(ByteOrder.LITTLE_ENDIAN).getFloat();

            // Handle accelerometers related events
            if (listeners.get(EventType.FALL) != null && SensedEventsUtils.hasFallen(x, y, z)) {
                Event e = new Event();
                e.setType(EventType.FALL);
                e.setMessage("User fallen");
                listeners.get(EventType.FALL).onEventSensed(e);
            }
        }
        else if (type == 2) {
            float heartRate = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getFloat();

            boolean heartAttack = SensedEventsUtils.hasHeartAttack(heartRate);
            boolean faint = SensedEventsUtils.hasFaint(heartRate);

            // Handle heart rate sensor related events
            if (listeners.get(EventType.HEART_ATTACK) != null && (heartAttack || faint)) {
                Event e = new Event();
                if (heartAttack) e.setType(EventType.HEART_ATTACK);
                if (faint) e.setType(EventType.FAINT);
                e.setMessage("Heart attack");
                listeners.get(EventType.HEART_ATTACK).onEventSensed(e);
            }
        }
    }

    public void onEvent(byte[] data) {
        onMessageReceived(data);
    }
}
