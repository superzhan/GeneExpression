# GeneExpression
基因表达式编程，java代码实现。主要实现二分类器，和多分类器。
    基因表达式编程 (Gene Expression Programming，GEP)是C.Ferreira在遗传算法和遗传程序设计的基础上提出的一种新的遗传算法，它结合了遗传算法和遗传程序设计的优点，建立了一种新的基因编码方式和结果变形形式。

  基本使用：
     
  GEPRun类是代码的主要入口（测试使用）。
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


  public void RunGep() 是GEP的主要过程调用。SetValue()函数设置GEP运行参数。GepPro.TrainData 和GepPro.TestData设置GEP分类器的测试参数。 目前所有的测试数据位于data目录下。更多数据可以到UCI数据库上下载。
