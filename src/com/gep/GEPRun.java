package com.gep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GEPRun {

	public GepProcess GepPro;
	public static int nCheck;
	
	public int[] nAttri=null;

	public double[][] ReadData(String sPath) throws IOException {

		double[][] digital = new double[1000][1000];

		File file = new File(sPath);
		if (!file.exists() || file.isDirectory())
			throw new FileNotFoundException();
		BufferedReader br = new BufferedReader(new FileReader(file));
		String temp = null;
		String[] tempArray = null;
		int i = 0;
		temp = br.readLine();
		while (temp != null) {

			tempArray = temp.split(",");
			
			GepPro.FeatureNum=tempArray.length-1;  //==========================================================
			
			for (int j = 0; j < tempArray.length; j++) {
				digital[i][j] = Double.parseDouble(tempArray[j]);

			}
			temp = br.readLine();
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
	 * 用部分特征进行数据测试 部分特征由nAttri[] 给出
	 * 
	 * @param nAttri
	 */
	public double[][] SelectAttri(int[] nAttri, double[][] Data) {
		int nRow = Data.length;
		int nCol = Data[0].length;
		double[][] dRes = new double[nRow][nAttri.length + 1]; // 输出的数组

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
	 * 设置参数
	 */
	public void SetValue() {

		GepPro.FitnessFunType = "SenSepClassify"; // 设置适应值函数 可选
													// SampleClassify:简单分类函数 ,
													// SenSepClassify:适应度*敏感度
													// ConciseClassify 简洁模型
		GepPro.MaxGeneration = 500;
		GepPro.HeadLength = 10;
		GepPro.GeneCount = 3;
		GepPro.PopulationSize = 60;
		GepPro.FeatureNum = 4; // 特征个数------------------------------------------------

		// ---------------------------------------------------------
		// int[] nAttri={7  ,  26   , 20   , 17  ,   5  ,   6    ,25   , 28   , 14  ,  24  ,  11    ,27   ,  1};

		try {
			GepPro.TrainData = ReadData("data/sample.txt");// ----设置训练集数据的路径----------------------------------

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			GepPro.TestData = ReadData("data/sample.txt");// ----设置测试集数据的路径---------------------------------------------

		} catch (IOException e) {
			e.printStackTrace();
		}

		//设置特征子集----------------------------------------------------------------------------------------------------------------------------------------
//		if(nAttri!=null){
//		
//			GepPro.FeatureNum = nAttri.length;
//			GepPro.TrainData = this.SelectAttri(nAttri, GepPro.TrainData);
//			GepPro.TestData = this.SelectAttri(nAttri, GepPro.TestData);
//		}
//      
		//---------------------------------------------------------------------------------------------------------------------------------------------------
		
		GepPro.MutationRate = 0.04;
		GepPro.ISRate = 0.15;
		GepPro.RISRate = 0.1;
		int[] nLen = { 1, 2, 3 };
		GepPro.ISElemLength = nLen;
		GepPro.RISRate = 0.1;
		GepPro.RISElemLength = nLen;
		GepPro.GeneTransRate = 0.1;

		GepPro.OnePRecomRate = 0.3;
		GepPro.TwoPRecomRate = 0.31;
		GepPro.GeneRecomRate = 0.1;

	}

	public void RunGep() {

		GepPro = new GepProcess();
		SetValue();
		int nGeneration = 0;
		GepPro.InitialPopulation();

		do {

			GepPro.EvalutePopulaton();

			 //Print();
			// System.out.println("before average Fitness "+GepPro.AverageFitness());

			GepPro.Select();

			GepPro.Statictis();// 统计
			Print();

			GepPro.Mutation();

			GepPro.TransPosIS();

			GepPro.TransPosRIS();

			GepPro.TransPosGene();

			GepPro.RecomOnePoint();

			GepPro.RecomTwoPoint();

			GepPro.ReComGene();

			++nGeneration;
			// System.out.println(nGeneration
			// +":  "+GepPro.BestIndividual.Fitness +"\n");

		} while (((1 - GepPro.BestIndividual.Fitness) > 0.03)
				&& nGeneration < GepPro.MaxGeneration);
		
		// GepPro.Test();
		 
		System.out.println(GepPro.BestIndividual.Fitness);
		System.out.println(GepPro.BestIndividual.Chrom.toString());
		
      
		
		System.out.println("测试  " + GepPro.Test());

		//GepPro.GetFeatureOrder();

	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	public static void main(String[] args) {
		
//		double d=0;
//		for(int i=0;i<5;++i)
//		{
		   GEPRun gep = new GEPRun();
		  gep.RunGep();
//		  d+=gep.GepPro.TestAccuracy;
//		}
//		d=d/5;
//		System.out.println("平均准确度为  "+d);
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
