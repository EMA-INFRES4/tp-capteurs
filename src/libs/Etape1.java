package libs;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.EventListenerList;

import hidException.HidFeatureReportException;
import hidException.HidInterruptReportException;
import hidException.HidNotOpenDeviceException;
import oakRH.OakUsbRH;
import oakSensor.OakMemoryMode;
import oakSensor.OakReportMode;

public class Etape1 implements Runnable {


	private final EventListenerList listeners = new EventListenerList();
	
	private  int _Intervale = 2000;
	private double _oldTemp = 0;
	
	public Etape1(){
	}
	public Etape1(int intervale){
		this._Intervale = intervale;
	}

	@Override
	public void run() {
		OakUsbRH rhSensor = new OakUsbRH();
		try {
			rhSensor.openSensor();
			rhSensor.setReportMode(OakReportMode.REPORT_MODE_FIXED_RATE,
					OakMemoryMode.RAM);
			rhSensor.setReportRate(this._Intervale, OakMemoryMode.RAM);
			byte[] data;
			while (rhSensor.isOpened()) {
				data = rhSensor.readData();
				double humid = rhSensor.getHumidity(data) * 1e-4 * 100;
				double chaleur = Math.round(rhSensor.getTemperature(data) * 1e-2 - 273.15);
				
				fireTemperatureChanged(humid, chaleur);
			}
		} catch (HidNotOpenDeviceException | HidFeatureReportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HidInterruptReportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addCapteurListener(CapteurListener listener){
		listeners.add(CapteurListener.class, listener);
	}
	
	public void removeCapteurListener(CapteurListener listener){
		listeners.remove(CapteurListener.class, listener);
	}

	public CapteurListener[] getCapteurListeners(){
		return listeners.getListeners(CapteurListener.class);
	}

	protected void fireTemperatureChanged(double temperature, double humidite){
		if(temperature != _oldTemp){
			for(CapteurListener listener : getCapteurListeners()){
				listener.TemperatureChanged(temperature, humidite);
			}
			_oldTemp = temperature;
		}
	}
	
}
