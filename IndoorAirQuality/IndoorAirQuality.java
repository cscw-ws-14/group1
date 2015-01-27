/*
 * By:
 * Shekoufeh Gorgi Zadeh
 * 
 * 10.12.2014
 * 
 */
// change this if you want
package IndoorAirQuality;    // change this if you want

import com.jpmorrsn.fbp.engine.Connection;
import com.jpmorrsn.fbp.engine.Network;

public class IndoorAirQuality extends Network {
    String description = " ";

    protected void define() {

        component("MedianFilter", MedianFilter.class);
        component("ThresholdCheck", ThresholdCheck.class);
        component("Mapper", Mapper.class);
        component("MosquittoSubscriber", MosquittoSubscribe.class);
        component("MosquittoSubscriber2", MosquittoSubscribe.class);
        component("JsonParser", JsonParser.class);
        component("JsonParser2", JsonParser.class);
        component("ActionSuggester", ActionSuggester.class);
        component("WS", WebSocketComponents.WebsocketComponent.class);  

        initialize("5", component("MedianFilter"), port("SIZE"));
        initialize(1500, component("ThresholdCheck"), port("THRESHOLD"));
        initialize("client1", component("MosquittoSubscriber"), port("CLIENTID"));
        initialize("/cscw-bplus-04/AirSensor/VOC", component("MosquittoSubscriber"), port("TOPIC"));
        initialize("/cscw-bplus-04/Bar/Outside_Temp", component("MosquittoSubscriber2"), port("TOPIC"));
        initialize("v", component("JsonParser"), port("KEY[0]"));
        initialize("v", component("JsonParser2"), port("KEY[0]"));
        initialize("1500", component("ActionSuggester"), port("THRESHOLD"));

        connect(component("MosquittoSubscriber"), port("MESSAGE"), component("JsonParser"), port("JSON"));
        connect(component("MosquittoSubscriber2"), port("MESSAGE"), component("JsonParser2"), port("JSON"));
        connect(component("JsonParser"), port("VALUE[0]"), component("MedianFilter"), port("IN"));
        connect(component("MedianFilter"), port("OUT"), component("ThresholdCheck"), port("IN"));
        connect(component("ThresholdCheck"), port("OUT"), component("Mapper"), port("IN"));
        Connection c1 = connect(component("JsonParser2"), port("VALUE[0]"), component("ActionSuggester"), port("TEMPERATURE"));
        Connection c2 = connect(component("Mapper"), port("OUT"), component("ActionSuggester"), port("IN"));
        connect(component("ActionSuggester"), port("OUT"), component("WS"), port("MESSAGE"));

        c1.setDropOldest();
        c2.setDropOldest();
    }

    public static void main(String[] argv) throws Exception {
        new IndoorAirQuality().go();
    }

}
