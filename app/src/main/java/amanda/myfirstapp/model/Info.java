package amanda.myfirstapp.model;

import java.sql.Date;

public class Info {

        public final Sensors sensors;
        public final float airTemperature;
        public final float airHumidity;
        public final Date date;

        public Info(Sensors sensors, float airTemperature, float airHumidity, Date date) {
            this.sensors = sensors;
            this.airTemperature = airTemperature;
            this.airHumidity = airHumidity;
            this.date = date;
        }

        public Info(Sensors sensors, float airTemperature, float airHumidity){
            this(sensors, airTemperature, airHumidity, null);
        }

        @Override
        public String toString() {
            return "Info{" + sensors.toString() +
                    ", airTemperature=" + airTemperature +
                    ", airHumidity=" + airHumidity +
                    ", date=" + date +
                    '}';
        }
}
