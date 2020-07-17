import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class TestImagePredictionWithRandomImage {

    public static void main(String[] args)  throws IOException {

        int height = 28;
        int width = 28;
        int channels=1;
        int outputNum = 10; // number of output classes
        int batchSize = 128; // batch size for each epoch
        int rngSeed = 123 ;// random number seed for reproducibility
        int numEpochs = 15 ;// number of epochs to perform

        List<Integer> labelList = Arrays.asList(2,3,7,1,6,4,0,5,8,9);

        String fileChooser = fileChooser();

        // Load model
        MultiLayerNetwork newModel = ModelSerializer.restoreMultiLayerNetwork(new File("G:\\Deeplearning\\Trained Model\\trained_mnist_model.zip"));

        System.out.println("Loaded Model from file location "+"G:\\Deeplearning\\Trained Model\\trained_mnist_model.zip");

        // load file from the choosen location
        File file = new File(fileChooser);

        // Use NativeImageLoader to convert to Numeric matrix
        NativeImageLoader loader = new NativeImageLoader(height,width,channels);

        // Get image into INDArray
        INDArray image = loader.asMatrix(file);
        image = image.reshape(new int[]{1, 784});

        // Normalize the values from 0-1
        DataNormalization scaler = new ImagePreProcessingScaler(0,1);
        scaler.transform(image);
        System.out.println("carregar modelo matrixes  " + image);
        // Pass it to the neural net

        INDArray output = newModel.output(image,false);
        System.out.println("Image used for predictin is "+fileChooser);
        for(int i=0;i<labelList.size();i++){
            System.out.println("Prediction as "+labelList.get(i)+" is : "+ new BigDecimal(output.getDouble(i)).toPlainString());
        }
        System.out.println("Prediction is : "+output.toStringFull());
        System.out.println("Prediction is : "+labelList.toString());

    }

    public static String fileChooser(){
        JFileChooser fileChooser = new JFileChooser();
        int ret = fileChooser.showOpenDialog(null);
        if(ret == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();
            String fileName = file.getAbsolutePath();
            return fileName;
        }else{
            return null;
        }
    }
}
