package gr.iti.mklab.verify;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import eu.socialsensor.framework.common.domain.MediaItem;
import gr.iti.mklab.extractfeatures.ItemFeatures;
import gr.iti.mklab.extractfeatures.ItemFeaturesAnnotation;
import gr.iti.mklab.extractfeatures.ItemFeaturesExtractor;
import gr.iti.mklab.utils.Vars;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.output.prediction.PlainText;
import weka.classifiers.lazy.KStar;
import weka.classifiers.misc.SerializedClassifier;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.Debug;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Class to organize the Item classification using Item features 
 * by declaring the attributes,creating the testing set 
 * and perform the classification.
 * @author boididou
 * boididou@iti.gr
 */
public class ItemClassifier {
	
	static ArrayList<Attribute> fvAttributes = new ArrayList<Attribute>();
	private static ClassifierAccuracy clAcc = new ClassifierAccuracy();
	
	public static ArrayList<Attribute> getFvAttributes(){
		return fvAttributes;
	}
	
	/**
	 * @return List of attributes needed for the classification
	 */
	public static List<Attribute> declareAttributes(){
		Attribute ItemLength = new Attribute("ItemLength");
		Attribute numWords = new Attribute("numWords");
		Attribute numQuestionMark = new Attribute("numQuestionMark");
		Attribute numExclamationMark = new Attribute("numExclamationMark");
		Attribute numUppercaseChars = new Attribute("numUppercaseChars");
		Attribute numPosSentiWords = new Attribute("numPosSentiWords");
		Attribute numNegSentiWords = new Attribute("numNegSentiWords");
		Attribute numMentions = new Attribute("numMentions");
		Attribute numHashtags = new Attribute("numHashtags");
		Attribute numURLs = new Attribute("numURLs");
		Attribute retweetCount = new Attribute("retweetCount");
		Attribute numSlangs = new Attribute("numSlangs");
		Attribute numNouns = new Attribute("numNouns");
		Attribute wotTrust = new Attribute("wotTrust");
		// Attribute wotSafe = new Attribute("wotSafe");
		Attribute readability = new Attribute("readability");

		// declare nominal attributes
		List<String> fvnominal1 = new ArrayList<String>(2);
		fvnominal1.add("true");
		fvnominal1.add("false");
		Attribute containsQuestionMark = new Attribute("containsQuestionMark",	fvnominal1);

		List<String> fvnominal2 = new ArrayList<String>(2);
		fvnominal2.add("true");
		fvnominal2.add("false");
		Attribute containsExclamationMark = new Attribute(
				"containsExclamationMark", fvnominal2);

		List<String> fvnominal3 = new ArrayList<String>(2);
		fvnominal3.add("true");
		fvnominal3.add("false");
		Attribute containsHappyEmo = new Attribute("containsHappyEmo",
				fvnominal3);

		List<String> fvnominal4 = new ArrayList<String>(2);
		fvnominal4.add("true");
		fvnominal4.add("false");
		Attribute containsSadEmo = new Attribute("containsSadEmo", fvnominal4);

		List<String> fvnominal5 = new ArrayList<String>(2);
		fvnominal5.add("true");
		fvnominal5.add("false");
		Attribute containsFirstOrderPron = new Attribute("containsFirstOrderPron", fvnominal5);

		List<String> fvnominal6 = new ArrayList<String>(2);
		fvnominal6.add("true");
		fvnominal6.add("false");
		Attribute containsSecondOrderPron = new Attribute("containsSecondOrderPron", fvnominal6);

		List<String> fvnominal7 = new ArrayList<String>(2);
		fvnominal7.add("true");
		fvnominal7.add("false");
		Attribute containsThirdOrderPron = new Attribute(
				"containsThirdOrderPron", fvnominal7);

		List<String> fvnominal8 = new ArrayList<String>(2);
		fvnominal8.add("true");
		fvnominal8.add("false");
		Attribute hasColon = new Attribute("hasColon", fvnominal8);

		List<String> fvnominal9 = new ArrayList<String>(2);
		fvnominal9.add("true");
		fvnominal9.add("false");
		Attribute hasPlease = new Attribute("hasPlease", fvnominal9);

		List<String> fvnominal10 = new ArrayList<String>(2);
		fvnominal10.add("true");
		fvnominal10.add("false");
		Attribute hasExternalLink = new Attribute("hasExternalLink",
				fvnominal10);

		List<String> fvnominal11 = null;
		Attribute id = new Attribute("id",fvnominal11);
		
		List<String> fvClass = new ArrayList<String>(2);
		fvClass.add("real");
		fvClass.add("fake");
		Attribute ClassAttribute = new Attribute("theClass", fvClass);

		// declare the feature vector
		fvAttributes.add(id);
		
		fvAttributes.add(ItemLength);
		fvAttributes.add(numWords);
		fvAttributes.add(containsQuestionMark);
		fvAttributes.add(containsExclamationMark);
		fvAttributes.add(hasExternalLink);
		fvAttributes.add(numNouns);
		fvAttributes.add(containsHappyEmo);
		fvAttributes.add(containsSadEmo);
		fvAttributes.add(containsFirstOrderPron);
		fvAttributes.add(containsSecondOrderPron);
		fvAttributes.add(containsThirdOrderPron);
		fvAttributes.add(numUppercaseChars);
		fvAttributes.add(numPosSentiWords);
		fvAttributes.add(numNegSentiWords);
		fvAttributes.add(numMentions);
		fvAttributes.add(numHashtags);
		fvAttributes.add(numURLs);
		fvAttributes.add(retweetCount);
		fvAttributes.add(numSlangs);
		fvAttributes.add(hasColon);
		fvAttributes.add(hasPlease);
		fvAttributes.add(wotTrust);
		fvAttributes.add(numQuestionMark);
		fvAttributes.add(numExclamationMark);
		fvAttributes.add(readability);
		
		/*fvAttributes.add(numPosSentiWords);
		fvAttributes.add(numNegSentiWords);
		fvAttributes.add(ItemLength);
		fvAttributes.add(wotTrust);
		fvAttributes.add(hasExternalLink);
		fvAttributes.add(readability);*/

		fvAttributes.add(ClassAttribute);

		return fvAttributes;
	}
	
	/**
	 * @param listItemFeatures the ItemFeatures of the Item
	 * @return the Instance created by the features
	 */
	public static Instance createInstance(ItemFeatures listItemFeatures){
		
		Instance inst = new DenseInstance(fvAttributes.size());
		String id = listItemFeatures.getId().replaceAll("[^\\d.]", "");
		inst.setValue((Attribute) fvAttributes.get(0), id);
		
		inst.setValue((Attribute) fvAttributes.get(1),
				listItemFeatures.getItemLength());
		inst.setValue((Attribute) fvAttributes.get(2),
				listItemFeatures.getNumWords());
		inst.setValue((Attribute) fvAttributes.get(3),
				String.valueOf(listItemFeatures.getContainsQuestionMark()));
		
		inst.setValue((Attribute) fvAttributes.get(4),
				String.valueOf(listItemFeatures.getContainsExclamationMark()));
		inst.setValue((Attribute) fvAttributes.get(5),
				String.valueOf(listItemFeatures.getHasExternalLink()));
		if (listItemFeatures.getNumNouns() != null) {
			inst.setValue((Attribute) fvAttributes.get(6),
					listItemFeatures.getNumNouns());
		}
		inst.setValue((Attribute) fvAttributes.get(7),
				String.valueOf(listItemFeatures.getContainsHappyEmo()));
		inst.setValue((Attribute) fvAttributes.get(8),
				String.valueOf(listItemFeatures.getContainsSadEmo()));

		if (listItemFeatures.getContainsFirstOrderPron() != null) {
			inst.setValue((Attribute) fvAttributes.get(9), String
					.valueOf(listItemFeatures.getContainsFirstOrderPron()));
		}
		if (listItemFeatures.getContainsSecondOrderPron() != null) {
			inst.setValue((Attribute) fvAttributes.get(10), String
					.valueOf(listItemFeatures.getContainsSecondOrderPron()));
		}
		if (listItemFeatures.getContainsThirdOrderPron() != null) {
			inst.setValue((Attribute) fvAttributes.get(11), String
					.valueOf(listItemFeatures.getContainsThirdOrderPron()));
		}

		inst.setValue((Attribute) fvAttributes.get(12),
				listItemFeatures.getNumUppercaseChars());

		if (listItemFeatures.getNumPosSentiWords() != null) {
			inst.setValue((Attribute) fvAttributes.get(13),
					listItemFeatures.getNumPosSentiWords());
		}
		if (listItemFeatures.getNumNegSentiWords() != null) {
			inst.setValue((Attribute) fvAttributes.get(14),
					listItemFeatures.getNumNegSentiWords());
		}
		inst.setValue((Attribute) fvAttributes.get(15),
				listItemFeatures.getNumMentions());
		inst.setValue((Attribute) fvAttributes.get(16),
				listItemFeatures.getNumHashtags());
		inst.setValue((Attribute) fvAttributes.get(17),
				listItemFeatures.getNumURLs());
		inst.setValue((Attribute) fvAttributes.get(18),
				listItemFeatures.getRetweetCount());

		if (listItemFeatures.getNumSlangs() != null) {
			inst.setValue((Attribute) fvAttributes.get(19),
					listItemFeatures.getNumSlangs());
		}
		inst.setValue((Attribute) fvAttributes.get(20),
				String.valueOf(listItemFeatures.getHasColon()));
		inst.setValue((Attribute) fvAttributes.get(21),
				String.valueOf(listItemFeatures.getHasPlease()));

		if (listItemFeatures.getWotTrust() != null) {
			inst.setValue((Attribute) fvAttributes.get(22),
					listItemFeatures.getWotTrust());
			
		}
		inst.setValue((Attribute) fvAttributes.get(23),
				listItemFeatures.getNumQuestionMark());
		inst.setValue((Attribute) fvAttributes.get(24),
				listItemFeatures.getNumExclamationMark());

		if (listItemFeatures.getReadability() != null) {
			inst.setValue((Attribute) fvAttributes.get(25),
					listItemFeatures.getReadability());
		}
		
		

		return inst;
	}
	
	/**
	 * @param listItemFeatures list of the ItemFeatures computed
	 * @param itemFeaturesAnnot list of the items' annotation details
	 * @return Instances that form the testing set
	 */
	public static Instances createTestingSet(List<ItemFeatures> listItemFeatures,List<ItemFeaturesAnnotation> listFeaturesAnnot){
		
		//auxiliary variable
		Integer index=0;
		
		// Create an empty training set
		Instances isTestSet = new Instances("Rel", fvAttributes, listItemFeatures.size());           
		
		// Set class index
		isTestSet.setClassIndex(fvAttributes.size()-1);
		
		for (int i=0;i<listItemFeatures.size();i++){
			Instance inst = createInstance(listItemFeatures.get(i));
						
			for (int j=0;j<listFeaturesAnnot.size();j++){
				if (listItemFeatures.get(i).getId().equals(listFeaturesAnnot.get(j).getId())){
					index = j;
				}
			}
			inst.setValue((Attribute)fvAttributes.get(fvAttributes.size()-1), listFeaturesAnnot.get(index).getReliability());
			
			isTestSet.add(inst);
		}
		return isTestSet;	
		
	}
	
	/**
	 * @param listItemFeatures the ItemFeatures computed for the MediaItem
	 * @param listFeaturesAnnot the MediaItem's annotation details
	 * @return Instances that form the testing set
	 */
	public static Instances createTestingSet(ItemFeatures listItemFeatures,ItemFeaturesAnnotation listFeaturesAnnot){
		
		// Create an empty training set
		Instances isTestSet = new Instances("ItemFeatureClassification", fvAttributes, 1);           
		// Set class index
		isTestSet.setClassIndex(ItemClassifier.getFvAttributes().size()-1);
	
		Instance inst = createInstance(listItemFeatures);
			
		inst.setValue((Attribute)fvAttributes.get(ItemClassifier.getFvAttributes().size()-1), listFeaturesAnnot.getReliability());
		
		isTestSet.add(inst);
		
		return isTestSet;	
		
	}
	
	/**
	 * @param isTestSet Instances of the test set
	 * @return Boolean table of reliability values of the test set instances 
	 * @throws Exception file
	 */
	public static boolean[] classifyItems(Instances isTestSet) throws Exception{
		
		//flags variable for the values of the verification
		boolean[] flags = new boolean[isTestSet.size()];
		int count = 0;
		//define the classifier and set the appropriate model file 
		SerializedClassifier classifier = new SerializedClassifier();
		classifier.setModelFile(new File(Vars.MODEL_PATH_ITEM_sample));

		for (int i = 0; i < isTestSet.numInstances(); i++) {
			
			double pred = classifier.classifyInstance(isTestSet.instance(i));
			
			String actual = isTestSet.classAttribute().value((int)isTestSet.instance(i).classValue());
			String predicted = isTestSet.classAttribute().value((int) pred);
			if (actual.equals(predicted)){
				count++;
			}
			
			//assign appropriate value to the flag
			if (predicted=="fake"){
				flags[i]=true;
			}
			else{
				flags[i]=false;
			}
			
		}
		
		//print info
		/*System.out.println();
		System.out.println("=== Results ===");
		System.out.println("Total items "+isTestSet.numInstances());
		System.out.println("Items classified correctly:"+count);
		System.out.println("Percentage "+((double)count/isTestSet.numInstances())*100);
		System.out.println();*/
		return flags;
	}
	
	/**
	 * Function that creates the training set given the features calculated before
	 * @param listItemFeatures the Item features of the MediaItem list
	 * @param itemFeaturesAnnot the MediaItem list's annotation details
	 * @return the Instances formed
	 */
	public static Instances createTrainingSet(List<ItemFeatures> listItemFeatures, List<ItemFeaturesAnnotation> itemFeaturesAnnot){
		
		//auxiliary variable
		Integer index=0;
		
		if (ItemClassifier.getFvAttributes().size()==0){
			fvAttributes = (ArrayList<Attribute>) declareAttributes();
		}
		
		// Create an empty training set
		Instances isTrainingSet = new Instances("TrainingContentFeatures",  ItemClassifier.getFvAttributes(), listItemFeatures.size());           
		
		// Set class index
		isTrainingSet.setClassIndex(fvAttributes.size()-1);
		
		for (int i=0;i<listItemFeatures.size();i++){
			
			Instance inst  = createInstance(listItemFeatures.get(i));
			
			for (int j=0;j<itemFeaturesAnnot.size();j++){
				if ((listItemFeatures.get(i).getId()).equals(itemFeaturesAnnot.get(j).getId())){
					index = j;
				}
			}
			
			inst.setValue((Attribute)fvAttributes.get(fvAttributes.size()-1), itemFeaturesAnnot.get(index).getReliability());
			isTrainingSet.add(inst);
		}
		
		//System.out.println("-----TRAINING SET-------");
		//System.out.println(isTrainingSet);
		
		return isTrainingSet;
	}
	
	/**
	 * Method that creates the classifier
	 * @param isTrainingSet the Instances from which the classifier is created
	 * @throws Exception
	 */
	public static void createClassifier(Instances isTrainingSet) throws Exception{

		//create the classifier
		J48 j48 = new J48();
		Classifier fc = (Classifier) j48 ;
		fc.buildClassifier(isTrainingSet);
		Debug.saveToFile(Vars.MODEL_PATH_ITEM_sample, fc);
		System.out.println("Model file saved to "+Vars.MODEL_PATH_ITEM_sample);
	}
	
	/**
	 * Function that organizes the classification given the training and testing sets
	 * @param itemsFakeTrain Fake Items for the training set
	 * @param itemsFakeTest Fake Items for the testing set
	 * @param itemsRealTrain Real Items for the training set
	 * @param itemsRealTest Real Items for the testing set
	 * @throws Exception
	 */
	public static void doClassification(List<MediaItem> itemsFakeTrain, List<MediaItem> itemsFakeTest, List<MediaItem> itemsRealTrain, List<MediaItem> itemsRealTest) throws Exception{
		
		System.out.println("=== Classification using Item(Content) features ===");
		
		//define the list of itemFeatures that are used for training and testing
		List<ItemFeatures> itemFeaturesTraining = new ArrayList<ItemFeatures>();
		List<ItemFeatures> itemFeaturesTesting  = new ArrayList<ItemFeatures>();
		//define the list of annotations of the items trained
		List<ItemFeaturesAnnotation> itemFeaturesAnnot  = new ArrayList<ItemFeaturesAnnotation>();
		List<ItemFeaturesAnnotation> itemFeaturesAnnot2 = new ArrayList<ItemFeaturesAnnotation>();
		
		//features
		System.out.println("Extracting features for training fake Items...");
		List<ItemFeatures> itemFeatsFakeTrain = ItemFeaturesExtractor.featureExtractionMedia(itemsFakeTrain);
		System.out.println("Extracting features for training real Items...");
		List<ItemFeatures> itemFeatsRealTrain = ItemFeaturesExtractor.featureExtractionMedia(itemsRealTrain);
		System.out.println("Extracting features for testing fake Items...");
		List<ItemFeatures> itemFeatsFakeTest  = ItemFeaturesExtractor.featureExtractionMedia(itemsFakeTest);
		System.out.println("Extracting features for testing real Items...");
		List<ItemFeatures> itemFeatsRealTest  = ItemFeaturesExtractor.featureExtractionMedia(itemsRealTest);
		
		System.out.println();
		
		/*--------FAKE ITEMS--------------*/
		//annotate and add to the itemFeaturesAnnot list
		//add item to the itemFeaturesTraining list
		for (int i = 0; i < itemFeatsFakeTrain.size(); i++) {
			ItemFeaturesAnnotation itemAnnot = new ItemFeaturesAnnotation();
			itemAnnot.setId(itemFeatsFakeTrain.get(i).getId());
			itemAnnot.setReliability("fake");	
			itemFeaturesAnnot.add(itemAnnot);
			itemFeaturesTraining.add(itemFeatsFakeTrain.get(i));
		}
		int trainfakesize = itemFeaturesTraining.size();
		System.out.println("Training size of fake items : " + trainfakesize);
		
		for (int i = 0; i < itemFeatsFakeTest.size(); i++) {
			ItemFeaturesAnnotation itemAnnot = new ItemFeaturesAnnotation();
			itemAnnot.setId(itemFeatsFakeTest.get(i).getId());
			itemAnnot.setReliability("fake");	
			itemFeaturesAnnot2.add(itemAnnot);
			itemFeaturesTesting.add(itemFeatsFakeTest.get(i));
		}
		int testfakesize = itemFeaturesTesting.size();
		System.out.println("Testing size of fake items : "+ testfakesize);
		
		
		
		/*--------REAL ITEMS--------------*/
		//annotate and add to the itemFeaturesAnnot list
		//add item to the itemFeaturesTraining list
		for (int i = 0; i < itemFeatsRealTrain.size(); i++) {
			ItemFeaturesAnnotation itemAnnot = new ItemFeaturesAnnotation();
			itemAnnot.setId(itemFeatsRealTrain.get(i).getId());
			itemAnnot.setReliability("real");	
			itemFeaturesAnnot.add(itemAnnot);
			itemFeaturesTraining.add(itemFeatsRealTrain.get(i));
		}
		
		System.out.println("Training size of real items : "+ (itemFeaturesTraining.size() - trainfakesize));
		
		for (int i = 0 ; i < itemFeatsRealTest.size() ; i++) {
			ItemFeaturesAnnotation itemAnnot = new ItemFeaturesAnnotation();
			itemAnnot.setId(itemFeatsRealTest.get(i).getId());
			itemAnnot.setReliability("real");	
			itemFeaturesAnnot2.add(itemAnnot);
			itemFeaturesTesting.add(itemFeatsRealTest.get(i));
		}
		System.out.println("Testing size of real items : "+ (itemFeaturesTesting.size() - testfakesize));
		
		
		Instances isTrainingSet = ItemClassifier.createTrainingSet(itemFeaturesTraining,itemFeaturesAnnot);
		Instances isTestingSet  = ItemClassifier.createTestingSet(itemFeaturesTesting,itemFeaturesAnnot2);
		
		System.out.println("Total size of training set : " +  isTrainingSet.size());
		System.out.println("Total size of testing set : "  +  isTestingSet.size());
		
		//create the classifier
		createClassifier(isTrainingSet);
		classifyItems(isTestingSet);
	}
	
	
	/**
	 * Method that implements cross validation to the items specified
	 * @param itemsFake	the list of Items annotated as fake
	 * @param itemsReal the list of Items annotated as real
	 * @throws Exception
	 */
	public static void crossValidate(List<MediaItem> itemsFake, List<MediaItem> itemsReal) throws Exception{
		
		List<ItemFeatures> itemFeatsFake = ItemFeaturesExtractor.featureExtractionMedia(itemsFake);
		List<ItemFeatures> itemFeatsReal = ItemFeaturesExtractor.featureExtractionMedia(itemsReal);
		
		
		//define the list of itemFeatures that are used for training
		List<ItemFeatures> itemFeaturesTraining = new ArrayList<ItemFeatures>();
		//define the list of annotations of the items trained
		List<ItemFeaturesAnnotation> itemFeaturesAnnot = new ArrayList<ItemFeaturesAnnotation>();
		
		
		/*--------REAL ITEMS--------------*/
		// a)annotate and add to the itemFeaturesAnnot list and 
		// b)add item to the itemFeaturesTraining list
		
		Collections.shuffle(itemFeatsReal);
		for (int i = 0; i < itemFeatsReal.size(); i++) {
			ItemFeaturesAnnotation itemAnnot = new ItemFeaturesAnnotation();
			itemAnnot.setId(itemFeatsReal.get(i).getId());
			itemAnnot.setReliability("real");	
			itemFeaturesAnnot.add(itemAnnot);
			itemFeaturesTraining.add(itemFeatsReal.get(i));
		}
		int trainreal = itemFeaturesTraining.size();
		System.out.println("Training size of real items : "+ trainreal);
		
		
		/*--------FAKE ITEMS--------------*/
		// a)annotate and add to the itemFeaturesAnnot list and 
		// b)add item to the itemFeaturesTraining list
		
		Collections.shuffle(itemFeatsFake);
		for (int i = 0 ; i < itemFeatsFake.size(); i++) {
			ItemFeaturesAnnotation itemAnnot = new ItemFeaturesAnnotation();
			itemAnnot.setId(itemFeatsFake.get(i).getId());
			itemAnnot.setReliability("fake");	
			itemFeaturesAnnot.add(itemAnnot);
			itemFeaturesTraining.add(itemFeatsFake.get(i));
		}
		
		System.out.println("Training size of fake items : " + (itemFeaturesTraining.size() - trainreal));
		System.out.println("Total size of training set  : " +  itemFeaturesTraining.size());
		
		Instances isTrainingSet = ItemClassifier.createTrainingSet(itemFeaturesTraining,itemFeaturesAnnot);
		
		/*--------- J48 tree --------------*/
		Evaluation eval = new Evaluation(isTrainingSet);
		J48 tree = new J48(); 

		StringBuffer forPredictionsPrinting = new StringBuffer();
		PlainText classifierOutput = new PlainText();
		
		classifierOutput.setBuffer(forPredictionsPrinting);
		weka.core.Range attsToOutput = null;
		Boolean outputDistribution = new Boolean(true);
		classifierOutput.setOutputDistribution(true);

		eval.crossValidateModel(tree, isTrainingSet, 10, new Random(1), classifierOutput, attsToOutput, outputDistribution);
			
		System.out.println("===== J48 classifier =====");
		System.out.println("Number of correct classified "+eval.correct());
		System.out.println("Percentage of correct classified "+eval.pctCorrect());
		System.out.println(eval.toClassDetailsString());
		System.out.println(eval.toMatrixString());
	
		System.out.println(eval.toSummaryString());
		
		/*--------- Random Forest tree --------------*/
		Evaluation evall = new Evaluation(isTrainingSet);
		RandomForest tr = new RandomForest();
		StringBuffer forPredictionsPrintingl = new StringBuffer();
		PlainText classifierOutputl = new PlainText();
		
		classifierOutputl.setBuffer(forPredictionsPrintingl);
		weka.core.Range attsToOutputl = null;
		Boolean outputDistributionl = new Boolean(true);
		classifierOutputl.setOutputDistribution(true);

		evall.crossValidateModel(tr, isTrainingSet, 10, new Random(1), classifierOutputl, attsToOutputl, outputDistributionl);
		
		System.out.println("===== Random Forest =====");
		System.out.println("Number of correct classified "+evall.correct());
		System.out.println("Percentage of correct classified "+evall.pctCorrect());
		System.out.println(evall.toClassDetailsString());
		System.out.println(evall.toMatrixString());
		System.out.println(evall.toSummaryString());
		
		/*--------- KStar --------------*/
		Evaluation eval4 = new Evaluation(isTrainingSet);	
		KStar tree4 = new KStar();
		eval4.crossValidateModel(tree4, isTrainingSet, 10, new Random(1));
		
		System.out.println("===== KStar classifier =====");
		System.out.println("Number of correct classified "+eval4.correct());
		System.out.println("Percentage of correct classified "+eval4.pctCorrect());
		System.out.println(eval4.toClassDetailsString());
		System.out.println(eval4.toMatrixString());
	}
	
	/**
	 * @param isTestSet the current Instances of the dataset
	 * @return double[] distribution probabilities
	 * @throws Exception file
	 */
	public static double[] findProbDistribution(Instances isTestSet) throws Exception{
		
		//probabilities variable
		double[] probabilities = new double[isTestSet.size()];
		SerializedClassifier classifier = new SerializedClassifier();
		classifier.setModelFile(new File(Vars.MODEL_PATH_ITEM_sample));
		
		for (int i = 0; i < isTestSet.numInstances(); i++) {
			double[] probabilityDistribution = classifier.distributionForInstance(isTestSet.instance(i));
			probabilities[i] = probabilityDistribution[1];
		}
		return probabilities;
	}
	
	public static Instances formTrainingSet(List<MediaItem> itemsFake, List<MediaItem> itemsReal) throws Exception {
		
		System.out.println("Training set: Item features extraction for fake items...");	
		List<ItemFeatures> itemFeatsFake = ItemFeaturesExtractor.featureExtractionMedia(itemsFake);
		System.out.println("Training set: Item features extraction for real items...");	
		List<ItemFeatures> itemFeatsReal = ItemFeaturesExtractor.featureExtractionMedia(itemsReal);
		
		Instances isTrainingSet = null;
		// define the list of itemFeatures that are used for training
		List<ItemFeatures> itemFeaturesTraining = new ArrayList<ItemFeatures>();
		// define the list of annotations of the items trained
		List<ItemFeaturesAnnotation> itemFeaturesAnnot = new ArrayList<ItemFeaturesAnnotation>();
					
		//fake
		for (int i = 0; i < itemFeatsFake.size(); i++) {
			ItemFeaturesAnnotation itemAnnot = new ItemFeaturesAnnotation();
			itemAnnot.setId(itemFeatsFake.get(i).getId());
			itemAnnot.setReliability("fake");
			itemFeaturesAnnot.add(itemAnnot);
			itemFeaturesTraining.add(itemFeatsFake.get(i));
		}
		
		int sizefake = itemFeaturesTraining.size();
		System.out.println("Size of fake training "+sizefake);
		
		//real
		
		for (int i = 0; i < itemFeatsReal.size(); i++) {
			ItemFeaturesAnnotation itemAnnot = new ItemFeaturesAnnotation();
			itemAnnot.setId(itemFeatsReal.get(i).getId());
			itemAnnot.setReliability("real");
			itemFeaturesAnnot.add(itemAnnot);
			itemFeaturesTraining.add(itemFeatsReal.get(i));
		}
				
		System.out.println("Size of real training "+(itemFeaturesTraining.size()-sizefake));
		isTrainingSet = ItemClassifier.createTrainingSet(itemFeaturesTraining,itemFeaturesAnnot);
		System.out.println("Total training size "+itemFeaturesTraining.size());
		
		return isTrainingSet;
	}
	
	public static Instances formTestingSet(List<MediaItem> itemsFake, List<MediaItem> itemsReal) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {

		System.out.println("Testing set: Item features extraction for fake items...");	
		List<ItemFeatures> itemFeatsFake = ItemFeaturesExtractor.featureExtractionMedia(itemsFake);
		System.out.println("Testing set: Item features extraction for real items...");	
		List<ItemFeatures> itemFeatsReal = ItemFeaturesExtractor.featureExtractionMedia(itemsReal);
		
		List<ItemFeatures> itemFeaturesTesting = new ArrayList<ItemFeatures>();
		List<ItemFeaturesAnnotation> itemFeaturesAnnot = new ArrayList<ItemFeaturesAnnotation>();


		for (int i = 0; i < itemFeatsFake.size(); i++) {
			ItemFeaturesAnnotation itemAnnot = new ItemFeaturesAnnotation();
			itemAnnot.setId(itemFeatsFake.get(i).getId());
			itemAnnot.setReliability("fake");
			itemFeaturesAnnot.add(itemAnnot);
			itemFeaturesTesting.add(itemFeatsFake.get(i));
		}
		int sizefake = itemFeaturesTesting.size();
		System.out.println("Testing set of fake items " + sizefake);
		
		
		for (int i = 0; i < itemFeatsReal.size(); i++) {
			ItemFeaturesAnnotation itemAnnot = new ItemFeaturesAnnotation();
			itemAnnot.setId(itemFeatsReal.get(i).getId());
			itemAnnot.setReliability("real");
			itemFeaturesAnnot.add(itemAnnot);
			itemFeaturesTesting.add(itemFeatsReal.get(i));
		}
		System.out.println("Testing size of real items "+ (itemFeaturesTesting.size() - sizefake));
		System.out.println("Testing size "+ itemFeaturesTesting.size());
		
		Instances isTestingSet = createTestingSet(itemFeaturesTesting, itemFeaturesAnnot);
		
		return isTestingSet;
	}
	
	public static ClassifierAccuracy getClsAccuracy() {
		return clAcc;
	}

}
