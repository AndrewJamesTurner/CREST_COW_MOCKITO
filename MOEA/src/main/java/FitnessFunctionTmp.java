import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.core.variable.RealVariable;
import org.moeaframework.core.variable.BinaryVariable;
import org.moeaframework.problem.AbstractProblem;
import java.io.*;


public class FitnessFunctionTmp extends AbstractProblem {

	private static final int size = 1000;

	public FitnessFunctionTmp() {

		super(size, 2);
	}

	@Override
	public Solution newSolution() {

		Solution solution = new Solution(getNumberOfVariables(), getNumberOfObjectives(), getNumberOfConstraints());

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

		int numOnes = numTrues(values);

		double[] f = new double[numberOfObjectives];
		f[0] = numOnes;
		f[1] = size - numOnes;

		solution.setObjectives(f);
	}

	private int numTrues(boolean[] array) {

		int num = 0;

		for (int i = 0 ; i < array.length; i++ ) {
			if (array[i]) {
				num++;
			}
		}

		return num;
	}
}
