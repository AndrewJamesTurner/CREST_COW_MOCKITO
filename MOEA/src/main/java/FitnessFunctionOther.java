import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.core.variable.RealVariable;
import org.moeaframework.core.variable.BinaryIntegerVariable;
import org.moeaframework.problem.AbstractProblem;
import java.io.*;


public class FitnessFunctionOther extends AbstractProblem {

	private static final int numTests = 420;
	private static final int maxTests = 420;

	private String[] tests = new String[numTests];

	public FitnessFunctionOther() {

		super(maxTests, 2);

		try {
			FileReader fileReader = new FileReader("./resources/mockitousagePackageSubsetTests.csv");
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			int index = 0;
			String line = bufferedReader.readLine();

			while ( line  != null ) {
				String[] test = line.split(",");
				tests[index++] = test[0];
				//System.out.print(test[0] + " ");
				line = bufferedReader.readLine();
			}
		}
		catch (IOException ex) {
			//
		}
	}

	@Override
	public Solution newSolution() {

		Solution solution = new Solution(getNumberOfVariables(), getNumberOfObjectives(), getNumberOfConstraints());

		for (int i = 0; i < getNumberOfVariables(); i++) {
			solution.setVariable(i, new BinaryIntegerVariable(-numTests, numTests));
		}

		return solution;
	}

	@Override
	public void evaluate(Solution solution) {


		int[] values = new int[numberOfVariables];

		for (int i = 0 ; i < numberOfVariables; i++ ) {
			values[i] = ((BinaryIntegerVariable)solution.getVariable(i)).getValue();
		}

		String appliedTests = "";

		for (int i = 0 ; i < values.length; i++ ) {

			if (values[i] >= 0) {
				appliedTests += (tests[i] + " ");
			}
		}

		String command = "./ff.sh " + appliedTests;

		// System.out.println();
		// System.out.println(command);
		// System.out.println();

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

			System.out.println("\t" + notCovered + " : " + time + "  (" + numTests(values) + ")");


			double[] f = new double[numberOfObjectives];
			f[0] = notCovered;
			f[1] = time;

			solution.setObjectives(f);
		}
		catch (IOException e) {
			e.printStackTrace(System.out);
		}
		catch (InterruptedException e) {
			System.out.println("InterruptedException");
		}
	}

	private int numTests(int[] values) {

		int sum = 0;

		for (int i = 0 ; i < values.length; i++ ) {
			if (values[i] >= 0) {
				sum++;
			}
		}

		return sum;
	}

}
