/**
 * 
 */
package com.gep;

/**
 * @author shenzhan
 *
 */
public class ConciseClassify extends FitnessFunction {

	/**
	 * 简单模型分类法
	 * 在SenSep的基础上加入惩罚 
	 * 是的个体更加简洁
	 */
	public ConciseClassify() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void GetFitness(Population Pop, double[][] Data, double[] Fitness) {
		int nRow = Data.length;
		int nCol = Data[0].length;
	
		int i, j, k;

		for (i = 0; i < Pop.Size; ++i) {
			int tp = 0, fp = 0, tn = 0, fn = 0;
			for (j = 0; j < nRow; ++j) {
				double dValue = super.Exp.GetValue(Pop.Get(i), Data[j]);
				Pop.Get(i).Value=dValue;
				// 二分类 0 类
				if (Data[j][nCol-1] == 1) {
					if (dValue < 0) {
						tp++;
					} else {
						fp++;
					}
				} else if (Data[j][nCol-1] == 2) {
					if (dValue >= 0) {
						tn++;
					} else {
						fn++;
					}
				}

			}
			
			double se,sp,ss;
			
			if((tp+fn)<0.1){
				se = 0;
			}
			else
			    se = tp/(double)(tp+fn);//敏感性
			
			if((tn+fp)<0.1){
				sp = 0;
			} else {
				sp = tn/(double)(tn+fp);//特效性
			}
			ss = se*sp;//敏感/特效性
			
			Expression Exp=new Expression();
			double dValidLen=Exp.GetIndivValidLen(Pop.Get(i));
			double dIndivLen=Pop.Get(i).Chrom.size();
			
			ss=ss*1000-(dValidLen/dIndivLen)*10 ;  //加入惩罚因子
			
			Pop.Get(i).Fitness = ss;
			Fitness[i] = ss;
		}
		
	}

}
