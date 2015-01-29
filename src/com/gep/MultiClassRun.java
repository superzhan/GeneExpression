package com.gep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MultiClassRun {

	public GepProcess GepPro;
	public static int nCheck;

	public Individual[] BestIndivs;

	public int[] nAttri = null;

	/**
	 * 
	 * 
	 * @param sPath
	 * @return
	 * @throws IOException
	 */
	public double[][] ReadData(String sPath) {

		double[][] digital = new double[1000][1000];

		File file = new File(sPath);
		if (!file.exists() || file.isDirectory())
			try {
				throw new FileNotFoundException();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String temp = null;
		String[] tempArray = null;
		int i = 0;
		try {
			temp = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (temp != null) {

			tempArray = temp.split(",");

			GepPro.FeatureNum = tempArray.length - 1; // ==========================================================

			for (int j = 0; j < tempArray.length; j++) {
				digital[i][j] = Double.parseDouble(tempArray[j]);

			}
			try {
				temp = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}

		int nRow = i;
		int nCol = tempArray.length;

		double[][] Res = new double[nRow][nCol];
		for (i = 0; i < nRow; ++i) {
			for (int j = 0; j < nCol; ++j) {
				Res[i][j] = digital[i][j];
			}
		}
		return Res;

	}

	/**
	 * 
	 * 
	 * @param nAttri
	 */
	public double[][] SelectAttri(int[] nAttri, double[][] Data) {
		int nRow = Data.length;
		int nCol = Data[0].length;
		double[][] dRes = new double[nRow][nAttri.length + 1]; // 

		int i, j, k;
		int nIndex;
		for (i = 0; i < nAttri.length; ++i) {
			nIndex = nAttri[i];
			for (j = 0; j < nRow; ++j) {
				dRes[j][i] = Data[j][nIndex];
			}
		}

		for (j = 0; j < nRow; ++j) {
			dRes[j][nAttri.length] = Data[j][nCol - 1];
		}

		return dRes;

	}

	/**
	 * 
	 * +++++++++++++++
	 */
	public void SetValue() {

		GepPro.FitnessFunType = "SampleClassify"; //

		// SampleClassify:简单分类函数 ,
		// SenSepClassify:适应度*敏感度
		// ConciseClassify 简洁模型
		
		GepPro.MaxGeneration = 1000;
		GepPro.HeadLength = 30;
		GepPro.GeneCount = 4;
		GepPro.PopulationSize = 80;
		GepPro.FeatureNum = 84; //
		GepPro.nClassCount = 4; //

		GepPro.MutationRate = 0.04;
		GepPro.ISRate = 0.1;
		GepPro.RISRate = 0.1;
		int[] nLen = { 1, 2, 3 };
		GepPro.ISElemLength = nLen;
		GepPro.RISRate = 0.1;
		GepPro.RISElemLength = nLen;
		GepPro.GeneTransRate = 0.1;

		GepPro.OnePRecomRate = 0.3;
		GepPro.TwoPRecomRate = 0.3;
		GepPro.GeneRecomRate = 0.1;

		GepPro.TrainData = ReadData("data/ECGTrain.txt");//

		GepPro.TestData = ReadData("data/ECGTest.txt");// 

	}

	/**
	 * GEP 
	 * 
	 * @param GepPro
	 */
	public void GEPrunning(GepProcess GepPro) {

		int nGeneration = 0;

		GepPro.InitialPopulation();

		do {

			GepPro.EvalutePopulaton();
			Print();

			GepPro.Select();

			GepPro.Mutation();

			GepPro.TransPosIS();

			GepPro.TransPosRIS();

			GepPro.TransPosGene();

			GepPro.RecomOnePoint();

			GepPro.RecomTwoPoint();

			GepPro.ReComGene();

			++nGeneration;

		} while (((1 - GepPro.BestIndividual.Fitness) > 0.03)
				&& nGeneration < GepPro.MaxGeneration);
	}

	public void RunGep() {

		GepPro = new GepProcess();
		SetValue();
		BestIndivs = new Individual[GepPro.nClassCount];
		int i;
		// 
		for (i = 1; i <= GepPro.nClassCount; ++i) {
			GepPro.nCurrentClass = i;
			GEPrunning(GepPro);
			BestIndivs[i - 1] = GepPro.BestIndividual;
		}

	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	public static void main(String[] args) {

		for(int k=0;k<2;k++)
		{
		
			MultiClassRun gep = new MultiClassRun();
			gep.RunGep();
	
			for (int i = 0; i < gep.BestIndivs.length; ++i) {
				System.out.print(gep.BestIndivs[i].Fitness+" ");
			}
	
			System.out.println( gep.Test());
		}
	}

	public double Test() {

		int nRow = GepPro.TestData.length;
		int nCol = GepPro.TestData[0].length;
		double[] dValue = new double[GepPro.nClassCount];
		int i, j, k;
		Expression Exp = new Expression();
		int tp = 0, fp = 0;
		for (j = 0; j < nRow; ++j) {

			for (i = 0; i < GepPro.nClassCount; ++i) {
				dValue[i] = Exp.GetValue(BestIndivs[i], GepPro.TestData[j]); // 鎶婃暟鎹斁鍒版瘡涓�釜鍒嗙被鍣ㄤ腑
																				// 璁＄畻鍑虹粨鏋�
			}

			int DataClass = (int) GepPro.TestData[j][nCol - 1]; // 鏁版嵁鎵�睘鐨勫垎绫�

		  for(i=0;i<GepPro.nClassCount;++i)
		  {
			    if(i!=DataClass)
			    {
			    	  if( dValue[i]<=0)
			    	  {
			    		  ++tp;
			    	  }else
			    	  {
			    		  ++fp;
			    		  break;
			    	  }
			    }else
			    {
			    	  if( dValue[i]>0)
			    	  {
			    		  ++tp;
			    	  }else
			    	  {
			    		  ++fp;
			    		  break;
			    	  }
			    } 
		  }
			
//			
//			if (DataClass == 2) {
//
//				if (dValue[DataClass - 1] > 0) {
//					++tp;
//				} else {
//					++fp;
//				}
//			}

		}
		GepPro.TestAccuracy = (tp) / (double) (tp + fp);
		return GepPro.TestAccuracy;
	}

	public void Print() {
		for (int i = 0; i < GepPro.PopulationSize; ++i) {

			for (int j = 0; j < GepPro.GeneCount; ++j) {

				for (int k = 0; k < GepPro.GeneLength; ++k) {
					System.out.print(GepPro.Pop.Get(i).Get(
							j * GepPro.GeneLength + k)
							+ " ");
				}
				System.out.print("        ");
			}
			System.out.print("  :  " + GepPro.Fitness[i] + " value"
					+ GepPro.Pop.Get(i).Value);
			System.out.println();
		}
		System.out.println();

	}

}
