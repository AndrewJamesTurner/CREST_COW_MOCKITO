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
import org.moeaframework.core.Algorithm;
import org.moeaframework.core.TerminationCondition;

public class Experiment {

	private String name;
	private int time; // mins
	private static String[] tests = new String[420];
	private static String fileName = "./resources/mockitousagePackageSubsetTests.csv";
	private String dir;

	public Experiment(String name, int time) {

		this.name = name;
		this.time = time;

		loadTests();

		dir = "./Experiments/" + name + "/";
		new File(dir).mkdirs();
	}

	public void run() {

		NondominatedPopulation result = new Executor()
		.withAlgorithm("NSGAII")
		.withProblemClass(FitnessFunction.class)
		.withMaxTime(1000 * 60 * this.time)
		.withProgressListener(new MyListener())
		.withProperty("populationSize", 10)
		.withTerminationCondition(new MyTerminationCondition())
		.run();

		displayResults(result);

		saveResults(result);

		System.out.println("defaultPerfomance: " + defaultPerfomance());
	}

	// performance form using all of the tests
	String defaultPerfomance() {

		String appliedTests = "";

		for (int i = 0 ; i < tests.length; i++ ) {
			appliedTests += (tests[i] + " ");
		}

		String command = "./ff.sh " + appliedTests;

		try {

			Runtime r = Runtime.getRuntime();
			Process p = r.exec(command);

			p.waitFor();
			BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = b.readLine();
			String lastLine = line;

			while (line != null) {
				lastLine = line;
				line = b.readLine();
			}

			b.close();

			String[] qwerty = lastLine.split(", ");
			double notCovered = 1 - Double.parseDouble(qwerty[0]);
			double time = Double.parseDouble(qwerty[1]);

			return notCovered + "," + time;
		}
		catch (IOException e) {
			e.printStackTrace(System.out);
		}
		catch (InterruptedException e) {
			System.out.println("InterruptedException");
		}

		return "";
	}


	private void displayResults(NondominatedPopulation result) {

		System.out.println("Not covered,Time");

		int id = 0;

		for (Solution solution : result) {
			System.out.println(solution.getObjective(0) + ", " + solution.getObjective(1));
		}

		System.out.println();
	}

	private void saveResults(NondominatedPopulation result) {

		try {

			PrintWriter writerDefault = new PrintWriter(dir + "defaultBehavior.txt");
			PrintWriter writerSummary = new PrintWriter(dir + "summary.txt", "UTF-8");

			writerDefault.println("Not Covered,Time");
			writerDefault.println(defaultPerfomance());
			writerDefault.close();

			writerSummary.println("Not Covered,Time");

			for (Solution solution : result) {

				PrintWriter writer = new PrintWriter(dir + "solution_" + solution.getObjective(0) + "_" + solution.getObjective(1) + ".txt", "UTF-8");
				writerSummary.println(solution.getObjective(0) + "," + solution.getObjective(1));

				for (int i = 0 ; i < solution.getNumberOfVariables(); i++ ) {

					boolean test = ((BinaryVariable)solution.getVariable(i)).get(0);

					if (test) {
						writer.println(tests[i]);
					}
				}

				writer.close();
			}

			writerSummary.println("\ndefault behavior\n" + defaultPerfomance());

			writerSummary.close();

			// save all the tests
			PrintWriter writerAllTests = new PrintWriter(dir + "allTests.txt");

			for (int i = 0 ; i < tests.length; i++ ) {
				writerAllTests.println(tests[i]);
			}

			writerAllTests.close();
		}

		catch (UnsupportedEncodingException e) {

		}
		catch (FileNotFoundException e) {

		}
	}


	private void loadTests() {

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


	public class MyTerminationCondition implements TerminationCondition {

		private NondominatedPopulation previousResults = null;

		public void initialize(Algorithm algorithm) {

		}

		public boolean shouldTerminate(Algorithm algorithm) {

			if (previousResults == null) {
				previousResults = algorithm.getResult();
				return false;
			}

			NondominatedPopulation currentResults = algorithm.getResult();

			// population has changed
			if (currentResults.size() != previousResults.size()) {
				logProgress(algorithm);
				previousResults = algorithm.getResult();
				return false;
			}
			else {
				for (int i = 0; i < currentResults.size(); i++) {
					for (int j = 0; j < currentResults.get(i).getNumberOfObjectives(); j++) {

						if (currentResults.get(i).getObjective(j) != previousResults.get(i).getObjective(j)) {
							logProgress(algorithm);
							previousResults = algorithm.getResult();
							return false;
						}
					}
				}
			}

			previousResults = algorithm.getResult();
			return false;
		}


		private void logProgress(Algorithm algorithm) {

			try {
				String filename = dir + "evaluationsBetweenImproovmentsToTheFitness.txt";
				FileWriter fw = new FileWriter(filename, true); //the true will append the new data
				fw.write(algorithm.getNumberOfEvaluations() + "\n");
				fw.close();
			}
			catch (IOException ioe) {
				System.err.println("IOException: " + ioe.getMessage());
			}
		}
	}
}