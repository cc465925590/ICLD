package cc.icld.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;


/** count查询 衡量NSA与SA的相对错误率 */
public class CountTest {

	/**
	 * 对采用泛化技术的匿名数据进行count查询的相对正确率计算
	 * 
	 * @param OrgGenBlockList
	 *            原始的泛化数据块
	 * @param FinGenBlockList
	 *            匿名后的数据块
	 * @param record
	 *            查询条件
	 * @serialData 2015.4.12
	 */
	public float CountForGen(List<LinkedList<Map<String, Object>>> OrgGenBlockList,
			List<LinkedList<Map<String, Object>>> FinGenBlockList, List<Map<String, Object>> recordList) {
		float avgResult = 0.0f;
		float queryNum = recordList.size();
		// 进行查询
		int i=0;
		for (Map<String, Object> CountQuery : recordList) {
			float act = 0.0f;
			float est = 0.0f;
			// 对原始数据进行统计
			for (LinkedList<Map<String, Object>> OrggenBlock : OrgGenBlockList) {
				act += getCountInBlock(OrggenBlock, CountQuery);
			}
			// 对匿名数据进行统计
			for (LinkedList<Map<String, Object>> FingenBlock : FinGenBlockList) {
				est += getCountInBlock(FingenBlock, CountQuery);
			}
			if (act >= est)
				avgResult += (act - est) / act;
			else
				avgResult += (est - act) / act;
			System.out.println("比较第"+i);
			i++;
		}
		avgResult = avgResult / queryNum;
		return avgResult;
	}

	/** 生成查询条件数据集 */
	public List<Map<String, Object>> CreateCountQuery(int n, String tablename) {
		List<Map<String, Object>> queryList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> recordList = new ArrayList<Map<String, Object>>();
		AccessConnet ac = new AccessConnet();
		ac.connect();
		try {
			String orderstr = "select * from " + tablename;
			System.out.println(orderstr);
			PreparedStatement pstmt = ac.con.prepareStatement(orderstr);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Map<String, Object> adult = new HashMap<String, Object>();
				/*
				 * adult.put("ID", rs.getString(1).trim()); adult.put("age",
				 * rs.getString(2).trim()); adult.put("sex",
				 * rs.getString(3).trim()); adult.put("education",
				 * rs.getString(4).trim()); adult.put("marital_status",
				 * rs.getString(5).trim()); adult.put("workclass",
				 * rs.getString(6).trim()); adult.put("relationship",
				 * rs.getString(7).trim()); adult.put("race",
				 * rs.getString(8).trim()); adult.put("occupation",
				 * rs.getString(9).trim());
				 */
				adult.put("ID", rs.getInt(1));
				adult.put("age", rs.getInt(2));
				adult.put("workclass", rs.getString(3));
				adult.put("education", rs.getString(5));
				adult.put("marital_status", rs.getString(7));
				adult.put("occupation", rs.getString(8));
				adult.put("relationship", rs.getString(9));
				adult.put("race", rs.getString(10));
				adult.put("sex", rs.getString(11));
				adult.put("class", rs.getString(16));
				/*
				 * adult.put("ID", rs.getInt(1)); adult.put("sex",
				 * rs.getString(2)); adult.put("education", rs.getString(3));
				 * adult.put("marital_status", rs.getString(4));
				 * adult.put("workclass", rs.getString(5));
				 * adult.put("relationship", rs.getString(6));
				 * adult.put("occupation", rs.getString(7)); adult.put("class",
				 * rs.getString(8));
				 */
				recordList.add(adult);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ac.close();

		for (int i = 0; i < recordList.size(); i = i + n) {
			int index = new Random().nextInt(n);
			if((i + index)<recordList.size()){
				Map<String, Object> record = recordList.get(i + index);
				queryList.add(record);
			}
		}

		return queryList;
	}

	/** 从泛化块中根据查询条件计算count */
	private float getCountInBlock(LinkedList<Map<String, Object>> genBlock, Map<String, Object> record) {
		float count = 0.0f;
		// 1 从泛化块中找到SA值等于record的SA值的个数
		float SAnum = GetSANum(genBlock, record, Common.SA);
		if (SAnum != 0) {
			// 2 计算出record的NSA在泛化块中的概率
			float probability = GetNSAProbability(genBlock, record, Common.NSAs);
			count = SAnum * probability;
		}
		return count;
	}

	private float GetSANum(LinkedList<Map<String, Object>> genBlock, Map<String, Object> record, String SA) {
		float SAnum = 0.0f;
		String SAValue = record.get(SA).toString();
		for (Map<String, Object> obj : genBlock) {
			if (obj.get(SA).toString().equals(SAValue))
				SAnum++;
		}
		return SAnum;
	}

	/** 查询条件在泛化块中为真的概率 */
	private float GetNSAProbability(LinkedList<Map<String, Object>> genBlock, Map<String, Object> record,
			String[] NSAs) {
		float probability = 1.0f;
		Map<String, Set<Object>> NSANumMap = new HashMap<String, Set<Object>>();
		for (String NSA : NSAs) {
			NSANumMap.put(NSA, new HashSet<Object>());
		}
		for (Map<String, Object> obj : genBlock) {
			for (String NSA : NSAs) {
				NSANumMap.get(NSA).add(obj.get(NSA));
			}
		}
		for (String NSA : NSAs) {
			if (NSANumMap.get(NSA).contains(record.get(NSA))) {
				float NSANum = NSANumMap.get(NSA).size();
				probability = probability * (1 / NSANum);
			} else {
				probability = 0;
				break;
			}
		}
		return probability;
	}

}
