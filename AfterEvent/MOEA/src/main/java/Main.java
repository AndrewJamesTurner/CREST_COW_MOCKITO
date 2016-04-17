import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.Executor;
import org.moeaframework.Analyzer;
import org.moeaframework.core.Solution;
import java.util.ArrayList;
import java.io.*;


public class Main {


	public static void main(String[] args) {



		try {

			Runtime r = Runtime.getRuntime();
			String tests = "org.mockitousage.verification.OnlyVerificationTest";
			String command = "./ff.sh " + tests;
			Process p = r.exec(command);

			p.waitFor();
			BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";

			while ((line = b.readLine()) != null) {
				System.out.println(line);
			}

			b.close();
		}
		catch (IOException e) {
			e.printStackTrace(System.out);
		}
		catch (InterruptedException e) {
			System.out.println("InterruptedException");
		}
	}

	/*public static void main(String[] args) {


		NondominatedPopulation result = new Executor()
		.withProblemClass(FitnessFunction.class)
		.withAlgorithm("NSGAII")
		.withMaxEvaluations(100000)
		.distributeOnAllCores()
		.run();

		ArrayList<Solution> solutions = new ArrayList<Solution>();

		for (int i = 0 ; i < result.size(); i++ ) {

			Solution sol = result.get(i);

			System.out.print("(" + i + "): ");

			for (int j = 0 ; j < sol.getNumberOfVariables(); j++ ) {
				System.out.print(sol.getVariable(j));
			}

			System.out.print("\n");

			solutions.add(sol);

		}

		System.out.println("#solutions: " + result.size());


	}*/
}
