package cc.icld.util;

import java.util.List;
import java.util.Map;

public class Common {
	public static String TABLENAME = "adult_TDS";// Ҫ�����ı� adult_o adult
	public static String RECORDNUM = "";// Ҫ��TABLE�����ļ�¼��
	public static List<Map<String, Object>> INITIALDATASET = null;// ��ʼ���ݼ�
	public static int K = 3;// K-�����еĲ���K
	public static int L = 3;// L-�������еĲ���L
	public static int D = 0;// ��2�е�sa���ϸ����еĲ���d
	public static String[] NSAs = { "sex", "education",
			"marital_status", "workclass", "relationship", "race" };
	public static String SA = //"occupation";
								 "age";
}
