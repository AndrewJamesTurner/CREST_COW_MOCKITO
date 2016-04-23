
import org.moeaframework.util.progress.ProgressListener;
import org.moeaframework.util.progress.ProgressEvent;


class MyListener implements ProgressListener {

	public void progressUpdate(ProgressEvent event) {

		int elapsedEvals = event.getCurrentNFE();
		int maxEvals = event.getMaxNFE();
		System.out.println("Eval: " + elapsedEvals);

	}

}