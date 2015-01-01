/*
 * By:
 * Shekoufeh Gorgi Zadeh
 * 
 * 10.12.2014
 * 
 */
package IndoorAirQuality;    // change this if you want 
 import java.io.Console;

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
component("MosquittoSubscriber2", MosquittoSubscribe.class);
component("JsonParser", JsonParser.class);
component("JsonParser2", JsonParser.class);
component("ActionSuggester", ActionSuggester.class);
component("Console1", WriteToConsole.class);
component("Console2", WriteToConsole.class);

initialize("9", component("MedianFilter"), port("SIZE"));
initialize(1500, component("ThresholdCheck"), port("THRESHOLD"));
initialize("client1", component("MosquittoSubscriber"), port("CLIENTID"));
initialize("/le/AirQuality/IAQ", component("MosquittoSubscriber"), port("TOPIC"));
initialize("/le/Bar/Outside_Temp", component("MosquittoSubscriber2"), port("TOPIC"));
initialize("v", component("JsonParser"), port("KEY[0]"));
initialize("v", component("JsonParser2"), port("KEY[0]"));
initialize("1500", component("ActionSuggester"), port("THRESHOLD"));

connect(component("MosquittoSubscriber"), port("MESSAGE"), component("JsonParser"), port("JSON"));
connect(component("MosquittoSubscriber2"), port("MESSAGE"), component("JsonParser2"), port("JSON"));
connect(component("JsonParser"), port("VALUE[0]"), component("MedianFilter"), port("IN"));
connect(component("MedianFilter"), port("OUT"), component("Duplicator"), port("IN"));
connect(component("Duplicator"), port("OUT[0]"), component("ThresholdCheck"), port("IN"));
connect(component("ThresholdCheck"), port("OUT"), component("SpeakerActuator"), port("IN"));
connect(component("Duplicator"), port("OUT[1]"), component("Mapper"), port("IN"));
connect(component("Mapper"), port("OUT"), component("LEDActuator"), port("IN"));
connect(component("JsonParser2"), port("VALUE[0]"), component("ActionSuggester"), port("TEMPERATURE"));
connect(component("Duplicator"), port("OUT[2]"), component("ActionSuggester"), port("VOC"));
connect(component("ActionSuggester"), port("DOORSTATE"), component("Console1"), port("IN"));
connect(component("ActionSuggester"), port("WINDOWSTATE"), component("Console2"), port("IN"));
 } 
public static void main(String[] argv) throws Exception  { 
 new IndoorAirQuality().go(); 
} 
 
}
