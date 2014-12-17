/*
 * By:
 * Shekoufeh Gorgi Zadeh
 * 
 * 10.12.2014
 * 
 */
package IndoorAirQuality;    // change this if you want 
 import com.jpmorrsn.fbp.components.Concatenate;
import com.jpmorrsn.fbp.components.Passthru;
import com.jpmorrsn.fbp.components.ReadFile;
import com.jpmorrsn.fbp.components.Splitter1;
import com.jpmorrsn.fbp.components.WriteToConsole;
import com.jpmorrsn.fbp.engine.*; 
import com.jpmorrsn.fbp.text.LineToWords;
public class IndoorAirQuality extends Network {
String description = " ";
 protected void define() { 
 
component("MedianFilter", MedianFilter.class);
component("Duplicator", Duplicator.class);
component("ThresholdCheck", ThresholdCheck.class);
component("SpeakerActuator", SpeakerActuator.class);
component("Mapper", Mapper.class);
component("LEDActuator", LEDActuator.class);
component("MosquittoSubscriber", MosquittoSubscribe.class);
component("JsonParser", JsonParser.class);

initialize("9", component("MedianFilter"), port("SIZE"));
initialize(1500, component("ThresholdCheck"), port("THRESHOLD"));
initialize("client1", component("MosquittoSubscriber"), port("CLIENTID"));
initialize("/le/AirQuality/IAQ", component("MosquittoSubscriber"), port("TOPIC"));
initialize("v", component("JsonParser"), port("KEY[0]"));

connect(component("MosquittoSubscriber"), port("MESSAGE"), component("JsonParser"), port("JSON"));
connect(component("JsonParser"), port("VALUE[0]"), component("MedianFilter"), port("IN"));
connect(component("MedianFilter"), port("OUT"), component("Duplicator"), port("IN"));
connect(component("Duplicator"), port("OUT[0]"), component("ThresholdCheck"), port("IN"));
connect(component("ThresholdCheck"), port("OUT"), component("SpeakerActuator"), port("IN"));
connect(component("Duplicator"), port("OUT[1]"), component("Mapper"), port("IN"));
connect(component("Mapper"), port("OUT"), component("LEDActuator"), port("IN"));

 } 
public static void main(String[] argv) throws Exception  { 
 new IndoorAirQuality().go(); 
} 
 
}
