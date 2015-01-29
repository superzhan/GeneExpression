package com.lab;

import java.util.LinkedList;
import java.util.List;

import com.gep.*;

public class FeatureWeight {
	GEPRun GepRun=null;
	
	List< List<FeatureStru> > FeatSet=new LinkedList<  List<FeatureStru> >();
	
	
	public void GetFeatureData(){
		int nTime=10;
		for(int i=0;i<nTime;++i){
			
			GepRun=new GEPRun();
			GepRun.RunGep();
			GepProcess GepPro=GepRun.GepPro;
			
			List<FeatureStru> listFeatStru =GepPro.GetFeatureOrder();
			
			FeatSet.add(listFeatStru);
		}
		
		Print();
		
	}
	
	public void Print(){
		 int  i,j,k;
		 for(i=0;i<FeatSet.size();++i){
			 List<FeatureStru> listFeatStru=FeatSet.get(i);
			 for(j=0;j<listFeatStru.size();++j){
				     FeatureStru feat=  listFeatStru.get(j);
				     System.out.print(feat.nFeatureNO +"  ");
			 }
			 System.out.println();
		 }
	}
	
	
	public static void main(String[] args) {
		FeatureWeight F=new FeatureWeight();
		F.GetFeatureData();
		
	}

}
