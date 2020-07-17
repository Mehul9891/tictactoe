import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;
import java.io.IOException;

public class DigitPredictionUsingML {

    public static void main(String[] args) throws IOException {

        int seed = 123;
        double learningRate = 0.01;
        int nEpochs = 30;


        int numRows = 28;
        int numColumns = 28;
        int outputNum = 10; // number of output classes
        int batchSize = 128; // batch size for each epoch
        int rngSeed = 123 ;// random number seed for reproducibility
        int numEpochs = 15 ;// number of epochs to perform

        DataSetIterator mnistTrain = new MnistDataSetIterator(batchSize, true, rngSeed);
        DataSetIterator mnistTest = new MnistDataSetIterator(batchSize, false, rngSeed);


        MultiLayerConfiguration config = new NeuralNetConfiguration.Builder()
                .seed(rngSeed)
                .weightInit(WeightInit.XAVIER)
                .updater(new Nesterovs(learningRate,0.9))
                .list()
                .layer(0,new DenseLayer.Builder().nIn(numRows * numColumns).nOut(1000)
                        .activation(Activation.RELU)
                        .build())
                .layer(1,new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .activation(Activation.SOFTMAX)
                        .nIn(1000).nOut(outputNum).build())
                .build();

        MultiLayerNetwork model = new MultiLayerNetwork(config);
        model.init();
        model.setListeners(new ScoreIterationListener(10));  //Print score every 10 parameter updates

        model.fit(mnistTrain,nEpochs);

        // Save Model

        File locationToSave = new File("G:\\Deeplearning\\Trained Model\\trained_mnist_model.zip");

        // Boolean to save updaters
        Boolean saveUpdaters = false;

        // ModelSerializer : save the model
        ModelSerializer.writeModel(model,locationToSave,saveUpdaters);

        // Load model
        MultiLayerNetwork newModel = ModelSerializer.restoreMultiLayerNetwork(new File("G:\\Deeplearning\\Trained Model\\trained_mnist_model.zip"));


        System.out.println("Evaluate model....");
        Evaluation eval = new Evaluation(outputNum);
        while (mnistTest.hasNext()) {
            DataSet t = mnistTest.next();
            INDArray features = t.getFeatures();
            INDArray labels = t.getLabels();
            INDArray predicted = newModel.output(features, false);
            eval.eval(labels, predicted);
        }
        System.out.println(eval.stats());

    }
}
