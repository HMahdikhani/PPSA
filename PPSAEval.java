import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.naming.PartialResultException;

/*
try {				
	
} catch (Exception e) {e.printStackTrace();}
*/
public class PPSAEval {

	  public static void main(String args[]) {	 
		  
		    int kappa = Integer.parseInt(args[0]);//Paillier keygen param
			Paillier paillier = new Paillier();
			// KeyGeneration
			paillier.keyGeneration(kappa);
			Paillier.PublicKey pubkey = paillier.getPubkey();
			Paillier.PrivateKey prikey = paillier.getPrikey();

		    int N = Integer.parseInt(args[1]); //Number of IoT devices, |V| = N
	  
		    int vSize = Integer.parseInt(args[2]); //|Vi in V| = |U| = vSize = 5,10.15,20,25,30
		    int dRange = 100; //Vij and Ui values in [0,dRange)
		    int iRange = 100; //Di - IoT device value in [0,iRange)
		    Vector<Integer> U = null;

		    
		    int t = 0;//465*51;//325*51;//210*51;//120*51;//55*51;//15*51; //Threshold
		    //	465(vSize=30), 325(25), 210(20), 120(15), 55(10), 15(5)
		    //sum(U)*min(Vi)=55*1 and sum(U)*max(Vi)=55*100
		    //t=(55*1+55*100)/2=55*51

		    switch(vSize) {
		    case 5:
		    	U = new Vector<Integer>(Arrays.asList(1,2,3,4,5));//|U|=|Vi|=5
		    	t = 15*51;
		    	break;
		    case 10:
		    	U = new Vector<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9,10));//|U|=|Vi|=10
		    	t = 55*51;
		    	break;
		    case 15:
		    	U = new Vector<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15));//|U|=|Vi|=15
		    	t = 120*51;
		    	break;		    	
		    case 20:
		    	U = new Vector<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20));//|U|=|Vi|=20
		    	t = 210*51;
		    	break;
		    case 25:
		    	U = new Vector<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25));//|U|=|Vi|=25
		    	t = 325*51;
		    	break;
		    case 30:
		    	U = new Vector<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30));//|U|=|Vi|=30
		    	t = 465*51;
		    	break;
		    }

		    int a = 500; //alfa: Anonymizing the final result value.
		    ////System.out.println("kappa: " + kappa);
		    ////System.out.println("N:|IoT| " + N);
  		    ////System.out.println("k=|U|=|Vi|: " + vSize);
		    ////System.out.println("U: " + U);
		    ////System.out.println("t: " + t);
		    ////System.out.println("a: " + a);
		    ////System.out.println("=========================================================================");

			

		    

		    long startTime;
		    long endTime;
		    long elapsedTime;
	   	    long t1,t3,t4,t5,t6;
			

			
			//==================== USER: 1. E(U), E(t), E(a) ====================
		    startTime = System.currentTimeMillis();
			Vector<BigInteger> EU = new Vector<BigInteger>();
			for (Integer i : U) {
				try {				
					EU.add(Paillier.encrypt(BigInteger.valueOf(i),pubkey));
				} catch (Exception e) {e.printStackTrace();}
			}			

			/* Decrypt the data to validate the encryption phase.
			for (BigInteger i : EU) {
				try {				
					System.out.println(Paillier.decrypt(i, pubkey, prikey));  

				} catch (Exception e) {e.printStackTrace();}
			}*/

			BigInteger Et = BigInteger.ZERO;
			try {				
				Et = Paillier.encrypt(BigInteger.valueOf(t), pubkey);				
			} catch (Exception e) {e.printStackTrace();}
			
			
			BigInteger Ea = BigInteger.ZERO;
			try {				
				Ea = Paillier.encrypt(BigInteger.valueOf(a), pubkey);				
			} catch (Exception e) {e.printStackTrace();}
	    	endTime=System.currentTimeMillis();
	    	elapsedTime = endTime-startTime;
	    	t1 = elapsedTime;
	    	////System.out.println("Preparing E(U), E(T) and E(a) (ns): "+t1);

	    	////System.out.println("=========================================================================");
	    	//System.exit(0);
	    	
			//==================== FN2: 2. E(U) ====================
			//Forwarding EU to IoT devices.
	    	
			//==================== IoT Device: 3. E(U.Vi) || E(D) ====================
		    startTime = System.currentTimeMillis();
		    
			Random rnd = new Random();
			Vector<Integer> D = new Vector<Integer>();//|D|=N
			Vector<BigInteger> ED = new Vector<BigInteger>();
			for(int i=0; i< N; i++) {
				D.add(i, rnd.nextInt(iRange)+1);
				//System.out.println("D["+i+"]: "+D.get(i));
				try {				
					ED.add(i, Paillier.encrypt(BigInteger.valueOf(D.get(i)), pubkey));
				} catch (Exception e) {e.printStackTrace();}
			}
			
			Vector<Vector<Integer>> V = new Vector<Vector<Integer>>(); //|V|=N, |Vi in V|=5, e.g. V[1]={1,2,3,4,5}
			Vector<Integer> tmpV;
			for(int i=0; i<N; i++) {
				tmpV = new Vector<Integer>();
				for(int j=0; j<vSize; j++) {
					tmpV.add(j, rnd.nextInt(dRange)+1);
				}
				V.add(i, tmpV);
				tmpV = null;
			}
			
			Vector<BigInteger> EUVi = new Vector<BigInteger>();		
			//Vector<BigInteger> tmpEUVi;
			BigInteger tmpZero = BigInteger.ZERO;			
			BigInteger tmpEUVi = tmpZero;

			BigInteger tmpRes;
			
			for(int i=0; i<N; i++) {
				tmpEUVi = Paillier.mul(EU.get(0), BigInteger.valueOf(V.get(i).get(0)), pubkey);//Initilize the tmpRes with U0^Vi0						
				for(int j=1; j<vSize; j++) {
					try {
						tmpRes = Paillier.mul(EU.get(j), BigInteger.valueOf(V.get(i).get(j)), pubkey);
						tmpEUVi = Paillier.add(tmpEUVi, tmpRes, pubkey);						
					} catch (Exception e) {e.printStackTrace();}					
				}
				EUVi.add(i, Paillier.selfBlind(tmpEUVi, new BigInteger(paillier.getPubkey().getN().bitLength()-10, rnd), pubkey));
			}

	    	endTime=System.currentTimeMillis();
	    	elapsedTime = endTime-startTime;
	    	t3 = elapsedTime;
	    	////System.out.println("IoT Response (ns) :"+t3+"/"+N+": "+t3/N);
	    	
		

		
	    	/*
	    	for(int i=0; i< N; i++) {
				System.out.println("D["+i+"]: "+D.get(i));
			}*/

			////System.out.println("U: "+ U);
			////System.out.println("V: "+ V);

			/*
			for(int i=0; i<N ; i++) {
				try {
					System.out.println("U.V["+i+"]: " + Paillier.decrypt(EUVi.get(i), pubkey, prikey));
					} catch (Exception e) {e.printStackTrace();}
			}*/
	
			////System.out.println("=========================================================================");
	    	//System.exit(0);
			
			//==================== FN2: 4. ww^-1 = 1 (mod n), E(U.Vi-t), E(D')=ED^wi=ED mul wi ====================
			startTime = System.currentTimeMillis();
			Vector<BigInteger> w = new Vector<BigInteger>();
			for(int i=0; i<N; i++) {
				//EUVi --> EUVi-t
				w.add(i, Paillier.randomZStarN(pubkey.getN()));
				ED.set(i, Paillier.mul(ED.get(i), w.get(i), pubkey));
				EUVi.set(i, Paillier.add(EUVi.get(i), Paillier.mul(Et, BigInteger.valueOf(-1), pubkey), pubkey)); 
			}
			endTime = System.currentTimeMillis();
	    	elapsedTime = endTime-startTime;
 		    t4 = elapsedTime;
 		    ////System.out.println("FN2: Step4 (ns)  :"+t4);

	    	
			/*
			for(int i=0; i<N ; i++) {
			try {
				//System.out.println("w["+i+"]: " + w.get(i));
				System.out.println("U.V["+i+"]-t: "+ Paillier.decrypt(EUVi.get(i), pubkey, prikey));
				} catch (Exception e) {e.printStackTrace();}
			}*/
			
			
 		    ////System.out.println("=========================================================================");
			//System.exit(0);
			
			
			//==================== FN1: 5. ci = E(U.Vi-t), Ci = E(D)^ci.r^n ====================
			startTime = System.currentTimeMillis();
			Vector<BigInteger> C = new Vector<BigInteger>();
			int tmpc = -1;
			for(int i=0; i<N; i++) {
				//EUVi-t >= 0 --> tmpc=1 else tmpc=0
				try {
					////System.out.print(paillier.decrypt(EUVi.get(i), pubkey, prikey).bitLength());
					////System.out.print("<");
					///System.out.println(paillier.getPubkey().getN().bitLength());
					tmpc = Math.abs(paillier.decrypt(EUVi.get(i), pubkey, prikey).bitLength() - paillier.getPubkey().getN().bitLength()) > kappa ? 1 : 0;
					////System.out.println(tmpc);
					C.add(i, Paillier.selfBlind(Paillier.mul(ED.get(i), BigInteger.valueOf(tmpc), pubkey), Paillier.randomZStarN(pubkey.getN()), pubkey));
				} catch (Exception e) {e.printStackTrace();}
			}
			endTime = System.currentTimeMillis();
	    	elapsedTime = endTime-startTime;
		    t5 = elapsedTime;
		    ////System.out.println("FN1: Step5 (ns) :"+t5);
			
		    /*
			for(int i=0; i<N ; i++) {
			try {
				System.out.println(Paillier.decrypt(C.get(i), pubkey, prikey));
				} catch (Exception e) {e.printStackTrace();}
			}*/
			
			////System.out.println("=========================================================================");
			//System.exit(0);			
			
			//==================== FN2: 6. mul (Ci^(wi^-1)).E(a) ====================
			startTime = System.currentTimeMillis();
			BigInteger result;
			BigInteger tmpWInv;
			BigInteger tmpCW;
			tmpWInv = w.get(0).modInverse(paillier.getPubkey().getN());
			tmpCW = Paillier.mul(C.get(0), tmpWInv, pubkey);
			result = tmpCW;
			for(int i=1; i<N; i++) {				
				tmpWInv = w.get(i).modInverse(paillier.getPubkey().getN());
				tmpCW = Paillier.mul(C.get(i), tmpWInv, pubkey);
				result = Paillier.add(result, tmpCW, pubkey);
			}
			result = Paillier.add(result, Ea, pubkey);
			endTime = System.currentTimeMillis();
	    	elapsedTime = endTime-startTime;
		    t6 = elapsedTime;
		    ////System.out.println("FN2: Step6 (ns)  :"+t6);
	    	
	    	////System.out.println("=========================================================================");
	    	//System.exit(0);
	    	
	    	
			//==================== User: 7. result-a --> finalresult ====================
			try {
				////System.out.println(Paillier.decrypt(result, pubkey, prikey).subtract(BigInteger.valueOf(a)));
				} catch (Exception e) {e.printStackTrace();}
			
			////System.out.println("=========================================================================");
		
		   
			System.out.println(kappa+"\t"+N+"\t"+vSize+"\t"+t1+"\t"+t3+"\t"+t3/N+"\t"+t4+"\t"+t5+"\t"+t6);

			
	  }
}
