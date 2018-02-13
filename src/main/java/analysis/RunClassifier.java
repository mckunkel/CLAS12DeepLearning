/*  +__^_________,_________,_____,________^-.-------------------,
 *  | |||||||||   `--------'     |          |                   O
 *  `+-------------USMC----------^----------|___________________|
 *    `\_,---------,---------,--------------'
 *      / X MK X /'|       /'
 *     / X MK X /  `\    /'
 *    / X MK X /`-------'
 *   / X MK X /
 *  / X MK X /
 * (________(                @author m.c.kunkel
 *  `------'
*/
package analysis;

import org.apache.spark.SparkContext;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.StringIndexerModel;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.feature.VectorIndexer;
import org.apache.spark.ml.feature.VectorIndexerModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import spark.utils.SparkManager;

public class RunClassifier {
	public static void main(String[] args) {

		SparkSession spark = SparkManager.getSession();
		SparkContext sparkContext = spark.sparkContext();
		// StructType schema = createStructType(new StructField[] {
		// createStructField("ID", IntegerType, false),
		// createStructField("hour", IntegerType, false),
		// createStructField("mobile", DoubleType, false),
		// createStructField("userFeatures", new VectorUDT(), false),
		// createStructField("clicked", DoubleType, false) });
		// $example on$
		// Load and parse the data file, converting it to a DataFrame.
		Dataset<Row> data = spark.read().format("csv").option("header", "true").option("inferSchema", true)
				.load("/Users/michaelkunkel/WORK/GIT_HUB/CLASEtaPrimeAnalysis/MachineLearningPID/TrainingSample.csv");
		data.printSchema();
		// Dataset<Row> dataII = spark.read().format("csv").option("header",
		// true)
		// .csv("/Users/michaelkunkel/WORK/GIT_HUB/CLASEtaPrimeAnalysis/MachineLearningPID/TrainingSample.csv");
		//

		// ID,PID,EpEmAngle,InvariantMassEpEmGam,InvariantMassEpEm,EmPx,EmPy,EmPz,EmE,EmTheta,EmPhi,GamPx,GamPy,GamPz,GamE,GamTheta,GamPhi
		StringIndexerModel labelIndexer = new StringIndexer().setInputCol("ID").setOutputCol("indexedLabel").fit(data);
		String[] feat = { "EpEmAngle", "InvariantMassEpEm" };
		VectorAssembler assem = new VectorAssembler();
		assem.setInputCols(new String[] { "ID", "PID", "EpEmAngle", "InvariantMassEpEmGam", "InvariantMassEpEm", "EmPx",
				"EmPy", "EmPz", "EmE", "EmTheta", "EmPhi", "GamPx", "GamPy", "GamPz", "GamE", "GamTheta", "GamPhi" });
		assem.setOutputCol("features");
		// setDataSet(assem.transform(input_file));
		VectorIndexerModel featureIndexer = new VectorIndexer().setInputCol("EpEmAngle").setOutputCol("indexedFeatures")
				.setMaxCategories(4).fit(data);

		// Split the data into training and test sets (30% held out for testing)
		Dataset<Row>[] splits = data.randomSplit(new double[] { 0.7, 0.3 });
		Dataset<Row> trainingData = splits[0];
		Dataset<Row> testData = splits[1];
		spark.stop();
	}
}
