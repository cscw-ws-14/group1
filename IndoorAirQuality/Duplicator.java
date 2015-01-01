/*
 * By:
 * Shekoufeh Gorgi Zadeh
 * 
 * 10.12.2014
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

@ComponentDescription("Duplicates the input on each outport.")
@OutPort(value = "OUT", arrayPort = true)
@InPort("IN")
public class Duplicator extends Component {

  static final String copyright = " ";

  private InputPort inport;

  private OutputPort[] outportArray;

  @Override
  protected void execute() {

    int no = outportArray.length;
    Packet p;

    p = inport.receive();

    Packet out;
    System.out.println(Integer.parseInt((String)p.getContent()));
    for(int i = 0; i < no; ++i){
    	out = create(p.getContent()+"");
    	outportArray[i].send(out);
    }
    drop(p);
  }

  @Override
  protected void openPorts() {

    inport = openInput("IN");

    outportArray = openOutputArray("OUT");

  }
}
