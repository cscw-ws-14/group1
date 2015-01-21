import com.jpmorrsn.fbp.engine.Network;

public class MessageBoxComponentExampleUse extends Network {
    protected void define() {
        component("CW", MessageBoxComponent.class);
        initialize("test bro", component("CW"), port("MESSAGE"));
    }

    public static void main(String[] argv) throws Exception  {
        new MessageBoxComponentExampleUse().go();
    }
}
