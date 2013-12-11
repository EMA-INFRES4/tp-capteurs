package libs;
import java.util.EventListener;

public interface WebcamListener extends EventListener {
	void SomebodyAppear(int numberOfPeople);
	void SomebodyDisappear();
}
