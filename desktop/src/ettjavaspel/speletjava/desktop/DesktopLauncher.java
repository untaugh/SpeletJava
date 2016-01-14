package ettjavaspel.speletjava.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ettjavaspel.speletjava.SpeletJava;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "SpeletJava";
		config.width = 500;
		config.height = 500;
		new LwjglApplication(new SpeletJava(), config);
	}
}
