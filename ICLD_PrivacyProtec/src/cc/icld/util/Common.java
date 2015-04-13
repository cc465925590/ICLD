package cc.icld.util;

import java.util.List;
import java.util.Map;

public class Common {
	public static String TABLENAME = "adult_TDS";// 要操作的表 adult_o adult
	public static String RECORDNUM = "";// 要对TABLE操作的记录数
	public static List<Map<String, Object>> INITIALDATASET = null;// 初始数据集
	public static int K = 3;// K-匿名中的参数K
	public static int L = 3;// L-多样性中的参数L
	public static int D = 0;// φ2中的sa集合个数中的参数d
	public static String[] NSAs = { "sex", "education",
			"marital_status", "workclass", "relationship", "race" };
	public static String SA = //"occupation";
								 "age";
}
