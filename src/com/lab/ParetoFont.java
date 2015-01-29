package com.lab;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.gep.*;




public class ParetoFont {

	
	public List<UnitFont> listUnit=new LinkedList<UnitFont>();
	public FunctionSet Fun=new FunctionSet();
	
	/**
	 * 运行gep 10次 得到最佳种群的一个集合
	 */
	public void GetSuperSet(){
		GEPRun gepRun=new GEPRun();
		
		for(int i=0;i<10;i++){   ///运行10 次gep 过程
			gepRun.RunGep();
			Expression Exp=new Expression();
			
			for(int j=0;j<gepRun.GepPro.Pop.PopulationSet.size();++j){
				Individual Indiv= gepRun.GepPro.Pop.PopulationSet.get(j);
			    UnitFont unit=new UnitFont();
			    unit.Indiv=(Individual) Indiv.clone();
			    unit.dFitness=Indiv.Fitness;
			    unit.nAttrNum=Exp.GetAttriNum(Indiv);
			    listUnit.add(unit);
			}
			
		}
	}
	
	/**
	 * pareto 前沿
	 */
	  public void GetParetoFont(){  
		  //按fitness 从小到大排序
		  int i,j,k;
		  UnitFont unit;
		  for(i=0;i<listUnit.size()-1;++i){
			  unit=listUnit.get(i);
			  k=i;
			  for(j=i+1;j<listUnit.size();++j){
				  if(listUnit.get(j).dFitness<unit.dFitness){
					  k=j;
					  unit=listUnit.get(j);
				  }
			  }
			  if(i!=k){
				 UnitFont temp=listUnit.get(i);
				 listUnit.set(i,listUnit.get(k));
				 listUnit.set(k,temp);
			  }
		  }
		  
		  //选择pareto font
		  i=1;
		  while(i<listUnit.size()){
			  if(listUnit.get(i).nAttrNum>listUnit.get(i-1).nAttrNum){
				  listUnit.remove(i);
			  }
			  else{
				  ++i;
			  }
		  }
		  
		  for(i=0;i<listUnit.size();++i){
			  System.out.println( listUnit.get(i).dFitness +"  "+listUnit.get(i).nAttrNum);
		  }
		  
		  
	  }
	  
	  public void GetFeature(){
		  int[] Feature=new int[GepProcess.FeatureNum];
		  int i,j,k;
		  for(i=0;i<Feature.length;++i){
			  Feature[i]=0;
		  }
		  
		  for(i=0;i<listUnit.size();++i){
			  Individual Indiv=listUnit.get(i).Indiv;
			  for(j=0;j<Indiv.Chrom.size();++j){
				  String str=Indiv.Get(j);
				  if( 0==Fun.GetParamCount(str)){
					  int no=Integer.parseInt(str);
					  Feature[no]++;
				  }
			  }
		  }
		  
//		  List<Integer> listInt=new ArrayList<Integer>();
		  for(i=0;i<Feature.length;++i){
			  if(Feature[i]>0){
				 // listInt.add(i);
				  System.out.println(i +"  "+Feature[i]);
			  }
		  }
//		  
//		  int[] FeatureNo=new int[listInt.size()];
//		for(i=0;i<listInt.size();++i){
//			FeatureNo[i]=listInt.get(i);
//		}
//		
//		//打印
//		for(i=0;i<FeatureNo.length;++i){
//			System.out.print("  "+FeatureNo[i]);
//		}
		    
	  }
	  
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ParetoFont pare=new ParetoFont();
		pare.GetSuperSet();
		pare.GetParetoFont();
		pare.GetFeature();
		
	}

}

 class UnitFont{
	 Individual Indiv;
	 double dFitness;
	 int nAttrNum;
	
}
