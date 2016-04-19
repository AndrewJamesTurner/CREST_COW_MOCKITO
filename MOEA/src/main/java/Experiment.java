import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.Executor;
import org.moeaframework.Analyzer;
import org.moeaframework.core.Solution;
import java.util.ArrayList;
import java.io.*;
import java.io.PrintWriter;
import org.moeaframework.util.progress.ProgressListener;
import org.moeaframework.util.progress.ProgressEvent;
import org.moeaframework.core.variable.BinaryVariable;
import org.moeaframework.core.variable.BinaryIntegerVariable;


public class Experiment {

	private String name;
	private int time; // mins
	private static String[] tests = new String[420];

	public Experiment(String name, int time) {
		this.name = name;
		this.time = time;

		loadTests();
	}

	public void run() {

		NondominatedPopulation result = new Executor()
		.withAlgorithm("NSGAII")
		.withProblemClass(FitnessFunction.class)
		//.withMaxEvaluations(100000)
		.withMaxTime(1000 * 60 * this.time)
		.withProgressListener(new MyListener())
		.withProperty("populationSize", 10)
		.run();

		displayResults(result);

		saveResults(result);
	}


	private void displayResults(NondominatedPopulation result) {

		System.out.println("Not covered,Time");

		int id = 0;

		for (Solution solution : result) {
			System.out.println(solution.getObjective(0) + ", " + solution.getObjective(1));
		}
	}

	private void saveResults(NondominatedPopulation result) {

		String dir = "./Experiments/" + name + "/";
		new File(dir).mkdirs();

		for (Solution solution : result) {

			try {

				PrintWriter writer = new PrintWriter(dir + "solution_" + solution.getObjective(0) + "_" + solution.getObjective(1) + ".txt", "UTF-8");

				for (int i = 0 ; i < solution.getNumberOfVariables(); i++ )

					if (solution.getVariable(i) instanceof BinaryVariable) {

						boolean test = ((BinaryVariable)solution.getVariable(i)).get(0);

						if (test) {
							writer.println(tests[i]);
						}
					}
					else {
						int test = ((BinaryIntegerVariable)solution.getVariable(i)).getValue();

						if (test >= 0) {
							writer.println(tests[test]);
						}
					}

			}

			writer.close();
		}
		catch (UnsupportedEncodingException e) {

		}
		catch (FileNotFoundException e) {

		}
	}
}



private void loadTests() {

	String fileName = "./resources/mockitousagePackageSubsetTests.csv";

	try {
		FileReader fileReader = new FileReader(fileName);
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		int index = 0;
		String line = bufferedReader.readLine();

		while ( line  != null ) {
			String[] test = line.split(",");
			tests[index++] = test[0];
			line = bufferedReader.readLine();
		}
	}
	catch (IOException ex) {
		System.out.println("Could not load " + fileName);
	}
}
}