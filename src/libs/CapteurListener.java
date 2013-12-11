package libs;
import java.util.EventListener;


public interface CapteurListener extends EventListener {
	void TemperatureChanged(double temperature, double humidite);
}
