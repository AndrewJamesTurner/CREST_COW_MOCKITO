import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.core.variable.RealVariable;
import org.moeaframework.core.variable.BinaryVariable;
import org.moeaframework.problem.AbstractProblem;
import java.io.*;


public class FitnessFunction extends AbstractProblem {

	private static final int numTests = 1487;
	private static final int maxTests = 500;

	private String[] tests = new String[numTests];

	public FitnessFunction() {

		super(numTests, 2);

		try {
			FileReader fileReader = new FileReader("./resources/test_classes_and_methods.csv");
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			int index = 0;
			String line = bufferedReader.readLine();

			while ( line  != null ) {
				String[] actionValuePair = line.split(",");
				tests[index++] = actionValuePair[0];
				line = bufferedReader.readLine();
			}
		}
		catch (IOException ex) {
			//
		}
	}

	@Override
	public Solution newSolution() {
		Solution solution = new Solution(getNumberOfVariables(),
										 getNumberOfObjectives());

		for (int i = 0; i < getNumberOfVariables(); i++) {
			solution.setVariable(i, new BinaryVariable(1));
		}

		return solution;
	}

	@Override
	public void evaluate(Solution solution) {

		boolean[] values = new boolean[numberOfVariables];

		for (int i = 0 ; i < numberOfVariables; i++ ) {
			values[i] = ((BinaryVariable)solution.getVariable(i)).get(0);
		}

		double fitness1 = 0;
		double fitness2 = 0;

		for (int i = 0 ; i < numberOfVariables; i++ ) {
			if (values[i]) {
				fitness1 += 1;
			}
			else {
				fitness2 += 1;
			}
		}

		double[] f = new double[numberOfObjectives];
		f[0] = fitness1;
		f[1] = fitness2;

		solution.setObjectives(f);
	}
}
