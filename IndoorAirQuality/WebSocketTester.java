/*
 * By:
 * Shekoufeh Gorgi Zadeh
 * 
 * 11.01.2015
 * 
 */
package IndoorAirQuality;


import com.jpmorrsn.fbp.engine.Component;
import com.jpmorrsn.fbp.engine.ComponentDescription;
import com.jpmorrsn.fbp.engine.InPort;
import com.jpmorrsn.fbp.engine.InputPort;
import com.jpmorrsn.fbp.engine.OutPort;
import com.jpmorrsn.fbp.engine.OutputPort;
import com.jpmorrsn.fbp.engine.Packet;

@ComponentDescription("Pass a stream of packets to an output stream")
@OutPort("OUT")
@InPort("IN")
public class WebSocketTester extends Component {

  static final String copyright = ".";

  private InputPort inport;

  private OutputPort outport;

  @Override
  protected void execute() {

    // make it a non-looper - for testing

    Packet p = inport.receive();
    Packet out;
    
    for(int i = 0; i<200;++i){
    	try {
			sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	out = create(p.getContent());
    	outport.send(out);
    }
    drop(p);
  }

  @Override
  protected void openPorts() {

    inport = openInput("IN");

    outport = openOutput("OUT");

  }
}
