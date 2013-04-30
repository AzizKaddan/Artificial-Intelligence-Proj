package ga;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class Engine {
  ////////////////////ORIGINAL MEMBERS///////////////////////////////////////////////////////////////////////////////
	
	public final static int GA_POPSIZE = 2048;
	public final static int GA_MAXITER = 5000;
	public  static float GA_ELITRATE = 0.1f;
	public  static float GA_MUTATIONRATE = 0.25f;
	public final static int MAX_COOR= 500;	//max coordinate value
	public static Random rdmGen  = new Random();
	public static int rand_max = 32767;			// ??
	public final static float GA_MUTATION = GA_MUTATIONRATE * rand_max   ; 	///TODO 
	public static String GA_TARGET = "Hello world!";
	private ArrayList<Member> ga_ArrayList = new ArrayList<Member>();
	private ArrayList<Integer> RWSArray;
	///////////////////NEWLY ADDED MEMBERS////////////////////////////////////.////////////////////////////////////////
	///////////Question 1 HW1 //////////////
	public static double average;					//for Question 1
	public static double standardDeviation;		// for Question 1
	public static int roundCounter = 0;
	///////////Question 2  HW1 /////////////////////////////////////////////////////////////////////////////////////////////
	public int hitBullBonus = 0;
	public static int bullBonus = 3;
	public static int hitBonus = 1;
	public int initValue = 0;
	///////////Question 3 HW1 ADDED MEMBERS /////////////////////////////////////////////////////////////////////////////////////////////
	public final static int NUM_OF_PARAMETERS = 3;
	public static int initRange = 50;				//initial range to start from in finding the function minimum , then grows --> Question 3 .
	public static int rangeInc = 40;
	public static double minValue = 32767 ; //this stores the minimum value for each round - question 3
	public static ArrayList<Integer> MIN_COORDINATES =new ArrayList<Integer>(); 	//this stores the coordinates that return the min value
	public static int sameMinimumCounter=0;
	public static final int MAX_SAME_MIN_TIMES = 100 ;
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////// Util members //////////////////////////////////////////////////////////////////////
	/////////////////////////////HW2 //////////////////////////////////
	public static double  currentFitness = 0;
	public static double  previousFitness = 0;
	public static final int LOCAL_MAXIMA_ITER = 100 ; 
	public static int maximaCounter = 0;
	public static long time ;
	public static int weightLCS = 0;
	public static int weightEdit = 0;
	public static int weightHemming = 0;
	public static boolean useStringHeuristic = false;
	public static int num = 213;
	public static int REPEAT = 1000;
	public static float goodPrecentage = 0.25f;
	public static float badPrecentage = 0.25f;
	public static final int BALDWIN_POP_SIZE = 1000;
	public static boolean useBaldwin = false;
	public static final long maxAge = 20;//50; //added 
	private boolean ageFlag = false;//added
			;//added
	public static double rankingParameter = 1.7; 	// must be between 1 and 2
	public static boolean scaleGenes = false;
	public static boolean rankGenes = false;
//////////////////////////////////////////////////////	
	public static enum Type{STRINGS , POINTS , NQUEENS , HITBULL , BALDWIN}	//select the type of the action to do
	public static enum SelectionType{NAIIVE , RWS , TOURNAMENT}	//select the type of selection
	public static enum ChessMateType {REGULAR , PMX}
	public static enum ChessMutateType{REGULAR , INSERTION}
	public static enum StringCrossover{REGULAR , TWOPOINT}
	//public static enum NormalizationType{WINDOWING , LINEAR_RANKING}
	public static ChessMateType cMType;
	public static ChessMutateType cMutType;
	public static StringCrossover sMType;
	public static  Type type;
	public static SelectionType selectionType;
	//private NormalizationType normalizationType ;
	
	////////////////////////HW3////////////////////////////////
	
	public static int reachedLocalMax;
	
	public Engine(){							
	
	}
	
	public void setType(Type type){				//set the type of the action to in the program
		this.type=type;	
	}
	
	public void initialize(ArrayList<Member> population ,ArrayList<Member> buffer){	//was previously called init_population
		if(useBaldwin){
			initPopulationBaldwin(population, buffer);
			return;
		}
		initValue = bullBonus * GA_TARGET.length();
		
		if(type == Type.POINTS){
			for(int i = 0 ; i < NUM_OF_PARAMETERS-1 ; i++){

			MIN_COORDINATES.add(0);		// initialize min coordinates ArrayList
			}
			return;
		}
		
		for(int i = 0; i < GA_POPSIZE;i++){
			population.add(new Member());	//initialize population
			buffer.add(new Member());		//initialize buffer
		}
	
	}

	public double maxFitness(ArrayList<Member> population){
		
		return population.get(population.size() - 1 ).getFitness();
	
	}
	
	public boolean checkReachingLocalMaxima(ArrayList<Member> population){	
		// checks if we have reached a local maxima
		previousFitness = currentFitness ; 				//previous generation
		currentFitness = population.get(0).getFitness();	//current generation
		
		if(currentFitness == previousFitness ){			// if still the same maxima then increment counter
			maximaCounter ++ ; 
		}
		if(maximaCounter == LOCAL_MAXIMA_ITER) {		// signal of a local maxima
			System.out.println("^^^^^^^^^^^^^LOCAL MAXIMA^^^^^^^^^^^^" +
					"			\n ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^" );
			maximaCounter = 0;
			return true;
		}
		return false;
	}
	
	public void dealWithLocalMaxima(ArrayList<Member> population){
		if(checkReachingLocalMaxima(population)){
			reachedLocalMax ++ ;	 // increase the counter denoting the number reached local maxima(Optima)
		}
		switch(reachedLocalMax){
		case 1:
			
			if(GA_MUTATIONRATE < 0.8 ){
				GA_MUTATIONRATE += 0.2;
			
			}
		case 2: // change mutate and selection parameters
			
			rankingParameter = rdmGen.nextDouble() + 1.5;		// select ranking parameter between 1.5 and 2.5
		
			weightEdit = rdmGen.nextInt()*4;			// change parameters of polynomial algorithm if used
			weightHemming = rdmGen.nextInt()*4;
			weightLCS = rdmGen.nextInt()*4;
		case 3:
			
			
		}
	}
	
	public void learning(ArrayList<Member> population){
		// intended for the Hinton and Nolan expirement
		for(int i = 0 ; i < BALDWIN_POP_SIZE ; i++ ){
			
			int fitness = 0;
			String tmp = population.get(i).getStr();
			
			for(int j=0 ; j < REPEAT ; j++ ){
				
				int randomNum= rdmGen.nextInt(GA_TARGET.length());
				if(tmp.charAt(randomNum) == '1'){
					String coString = tmp.substring(0, randomNum) + "0" + tmp.substring(randomNum+1);
					tmp = coString;	// replace character at 'randNum' index with 0
				}
				else{
					String coString = tmp.substring(0, randomNum) + "1" + tmp.substring(randomNum+1);
					tmp = coString;// replace character at 'randNum' index with 1
				}
			
			// calculating fitness for tmp
			for(int k = 0 ; j < GA_TARGET.length() ; j++){
				fitness += Math.abs(tmp.charAt(j) - GA_TARGET.charAt(j));
			}
			
			if(REPEAT - i != 0){
				fitness = 1 / (1 + (19 * (REPEAT - i)/REPEAT));		// FORMULA IN THE EXPIRMENT
				if(fitness == 0){
					break;
				}
			}
			
		}
					population.get(i).setFitness(fitness);
		}
		}
			
	
	public void initPopulationBaldwin(ArrayList<Member> population , ArrayList<Member> buffer){
		// this initialized the population according to the Hinton and Nolan expirement 
		for(int i = 0 ; i < GA_POPSIZE ; i ++ ){
			Member citizen = new Member();
			citizen.baldwinStr();
			population.add(citizen);
			buffer.add(citizen);
		}
	}
	
	public void rankGenes(ArrayList<Member> population){
		// ranks the genes in order to be used in the tournament selection
		for(int i = 0 ; i < GA_POPSIZE ; i++){
			population.get(i).setFitness(i + 1) ;
		}
		
	}
	
	public void scaleGenes(ArrayList<Member> population){
		// sclaes the genes in order to be used in RWS selection
		for(int i = 0 ; i < GA_POPSIZE ; i++){
			
			double initialFitness = population.get(i).getFitness();
//			
//			double scale = 1 ; 
//			if(standardDeviation != 0 ){	// as in the formula => if std is not 0 then 
//											// expected value is : 1 + (fitness - average) / 2 * std 
//				scale = 1 + ((initialFitness - average) / 2 * standardDeviation) ; 
//				scale *= 1000;
//			}
//			population.get(i).setFitness(scale);	// set the new fitness
		population.get(i).setFitness(Math.sqrt(initialFitness));
		}
	}


	public void calc_fitness(ArrayList<Member> population){
		double fitness;
		this.average = 0;
		double previousMin=minValue;
		if(type == Type.POINTS){
			
			FunctionMinima.minimumPerRound(population);
			
			if(previousMin==minValue){
				sameMinimumCounter++;	//check if its still the same minimum value
			}
		}
		
		
		for(int i=0;i<GA_POPSIZE;i++){
			fitness=0; 
			switch(type){
			case STRINGS:	//if we wish to calculate string fitness	
				if(useStringHeuristic){		// if user has chosen to use polynomial algorithm
					population.get(i).setFitness(StringMatching.polynomialAlgorithm(population.get(i).getStr(), GA_TARGET));
					break;
				}
				
				fitness=StringMatching.asciiDistanceHeur(population.get(i));//this is the original fitness calculating method(by ascii difference)
				population.get(i).setFitness(fitness);
			break;
			
			case HITBULL:
				
				fitness = initValue + HitBool.hitBullHeuristic(population.get(i));
				population.get(i).setFitness(fitness);
			break;
			
			case POINTS:	
				
				if(sameMinimumCounter <MAX_SAME_MIN_TIMES){
					fitness = 10 + FunctionMinima.DistanceToMin(population.get(i).getCoordinates());//apply the coordinates heuristic fitness calculation
				}
				
				else{
				}
				
				population.get(i).setFitness(fitness);
				
			break;
			
			case NQUEENS:
				
				fitness=NQueens.chessHerustic(population.get(i).getChessBoard());// apply the chess heuristic - calculating fitness
				population.get(i).setFitness(fitness);
				break;
			}
			average += fitness;	// increase the sum
			
			
			}
		
			average = average/GA_POPSIZE;	//calculate average
			standardDeviation = 0; // Question 1 
			for(int i = 0; i < GA_POPSIZE; i++){ // calculating the Standard Deviation according to the formula 
				standardDeviation += java.lang.Math.pow(population.get(i).getFitness() - average, 2);
			}
			standardDeviation = standardDeviation/GA_POPSIZE;
			standardDeviation = java.lang.Math.pow(this.standardDeviation, 0.5);
			updateRWSArray(population); 
	}
	
	public void updateRWSArray(ArrayList<Member> population){ //added
		double fit;
		if(selectionType != selectionType.RWS) return;
		if(RWSArray == null) {
			RWSArray = new ArrayList<Integer>();
		}
		RWSArray.clear();
		for(int i = 0;i < population.size();i++){
			fit = population.get(i).getFitness();
			for(int j =0;j < fit;j++){
				RWSArray.add(i);
			}
		}
	}
	
	
	public void sort_by_fitness(ArrayList<Member> population){
		Collections.sort(population,new Member());
		// do the ranking process 
		for( int i = 0 ; i < GA_POPSIZE ; i++){
			population.get(i).setRank(i);
		}
	}
	
	public void elitism(ArrayList<Member> population , ArrayList<Member> buffer ,int esize){
		int elitPassed = 0,i;
		
		for(i = 0; elitPassed < esize;i++){	
			if(ageFlag && population.get(i).getAge() < maxAge){
				// if ageFlag is turned on we let pass only young genes ,if not we let them pass as usual
			buffer.set(elitPassed, population.get(i));		//set in elitPassed index
			buffer.get(elitPassed).incAge();  // we increment the age of the gene
			elitPassed++; // we passed a gene so we increase the number of passed genes
			}
		}
		

		System.out.println(" i =  " + i);
	}
	
	
	
	public void mutate(Member member){				
		if(useBaldwin) {	// if user chose to use Hinton Nolan expirement , then mutate as demanded
			StringMatching.mutateBaldwin(member);
			return;
		}
		
		switch(type){
		case STRINGS:							// if type is of STRING
			
			StringMatching.mutateStr(member);
			break;
			
		case HITBULL:
			
			StringMatching.mutateStr(member);
			break;
			
		case POINTS:							// if type is of POINT
			
			FunctionMinima.mutatePoint(member);
			break;
			
		case NQUEENS:
			
			if(cMutType == ChessMutateType.REGULAR){	// if we choose to do regular mutation
				NQueens.mutateChessBoard(member);
			}
			else{
				NQueens.insertionMutateChessBoard(member);		// if we choose insertion mutation
			}
		}
	}
	
	
	public void mate(ArrayList<Member> population , ArrayList<Member> buffer) throws Exception{
		
		int esize=(int) (GA_POPSIZE * GA_ELITRATE);
		int spos,cpos, i1 = 0 , i2 = 0 , i3 = 0 , i4 = 0;
		elitism(population ,buffer,esize);
		ArrayList<Integer> randomNumbers = new ArrayList<Integer>();
		
		//mate the rest
		for(int i=esize;i<GA_POPSIZE;i++){
		//	if(ageFlag && minAge > maxAge) throw new Exception(); //added

			switch(selectionType){
			case NAIIVE: 							//If naiive then do the original selection
			i1 = rdmGen.nextInt(GA_POPSIZE / 2);
			i2 = rdmGen.nextInt(GA_POPSIZE / 2);
             
			break;
			case TOURNAMENT : 						//if tournament selection is chosen
				
				if(rankGenes){
					rankGenes(population);
				}
				
				//for(int j = 0;j < 4;j++) randomNumbers.add(rdmGen.nextInt(GA_POPSIZE));
				i1 = rdmGen.nextInt(GA_POPSIZE);
				i2 = rdmGen.nextInt(GA_POPSIZE);
				i3 = rdmGen.nextInt(GA_POPSIZE);
				i4 = rdmGen.nextInt(GA_POPSIZE);
				
				if(population.get(i1).getFitness() > population.get(i3).getFitness()){
					i1 = i3;
				}
				if(population.get(i2).getFitness() > population.get(i4).getFitness()){
					i2 = i4;
				}

			break;
			
			case RWS : 
				if(scaleGenes){
					scaleGenes(population);
				}
				int sum1 = 0,sum2 = 0,randomFitness1,randomFitness2,sumFitness;
				boolean breakFirst =false ,breakSecond = false;
				sumFitness = (int) (average * GA_POPSIZE);
			    randomFitness1 = rdmGen.nextInt(RWSArray.size());
				randomFitness2 = rdmGen.nextInt(RWSArray.size());
				
				i1 = RWSArray.get(randomFitness1);
				i2 = RWSArray.get(randomFitness2);
				break;
			}
			
			
		switch(type){
		
		case HITBULL:											// if type is STRING , then mate strings
			switch(sMType){		// selection type
			
			case REGULAR:
				spos = rdmGen.nextInt(GA_TARGET.length());		//string position randomly selected
				buffer.get(i).setStr(population.get(i1).getStr().substring(0, spos) + 
				population.get(i2).getStr().substring(spos,GA_TARGET.length()));	//string added to buffer is split from two strings
			break;
			
			case TWOPOINT:
				HitBool.twoPointMate(population.get(i1), population.get(i2));
			}
			
		case STRINGS:
			spos = rdmGen.nextInt(GA_TARGET.length());		//string position randomly selected
			buffer.get(i).setStr(population.get(i1).getStr().substring(0, spos) + 
				population.get(i2).getStr().substring(spos,GA_TARGET.length()));	//string added to buffer is split from two strings
		break;
			
		case POINTS:											//if type is POINTS, then mate points
				cpos = rdmGen.nextInt(MIN_COORDINATES.size()); 
				ArrayList<Integer> mergedCoor=FunctionMinima.mergeSubCoord(cpos,population.get(i1),population.get(i2));	//merge coordinates
				buffer.get(i).setCoordinates(mergedCoor);		//add the mixed points to the buffer
				break;
		case NQUEENS:
			switch(cMType){
			case REGULAR:
				buffer.set(i,NQueens.mateChessBoard(population.get(i1),population.get(i2)));
				break;
			case PMX:
				buffer.set(i,NQueens.mateChessBoardPMX(population.get(i1), population.get(1)));
				break;
			}
		}
			buffer.get(i).resetAge();
			if(rdmGen.nextInt(rand_max) < GA_MUTATION) {
				mutate(buffer.get(i));
			}
		}
	}
	
	public void swap(ArrayList<Member> population, ArrayList<Member> buffer){
		ArrayList<Member> tmp = new ArrayList<Member>();
		for(int i=0;i<population.size();i++) tmp.add(new Member());	//make temp of the same size as population
		Collections.copy(tmp, population);
		Collections.copy(population, buffer);
		Collections.copy(buffer, tmp);
	}
	/**
	* does two point crossover on two string and returns the result 
	* @return
	*/
	
	
	
	public static SelectionType getSelectionType() {
		return Engine.selectionType;
	}
	
	public static void setSelectionType(SelectionType selectionType) {
		selectionType = selectionType;
	}
	

public static void main(String[] args){
	
			Engine g=new Engine();
			g.setType(SimpleUI.actionTypeGetter());	// get the action type from the user
			SimpleUI.uiView();							//show the selection options to the user to choose from
			String resTest ="";
			ArrayList<Member> population = new ArrayList<Member>();
			ArrayList<Member> buffer = new ArrayList<Member>();
			g.initialize(population, buffer);	// initialize population
			g.time = System.currentTimeMillis();
			for(int i=0 ; i< GA_MAXITER ; i++){	
				g.calc_fitness(population);
				g.sort_by_fitness(population);
				String output = "Round : " + g.roundCounter+" " 
						+  SimpleUI.printBest(population) +  SimpleUI.printRoundAverage() 
						+" " + SimpleUI.printRoundStDeviation() + SimpleUI.printElapsedTime()
						+"\n"+ "============" +"\n";
				
				System.out.println(output);
				resTest += output;
				g.checkReachingLocalMaxima(population);
				if(population.get(0).getFitness() <= 0) break;
				if(useBaldwin)g.learning(population);
				try{
					g.mate(population, buffer); //added
					}catch(Exception e){
						break;
					}
				g.swap(population, buffer);
				g.roundCounter++;
			}
		
			SimpleUI.printToScreen(resTest);								//show results

	}
}
