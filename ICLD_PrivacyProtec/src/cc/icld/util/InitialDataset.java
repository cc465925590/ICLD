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
import java.util.Set;

import cc.icld.model.ColumnCount;

/** 2014.11.24 */
public class InitialDataset {
	public static LinkedList<Map<String, Object>> MyDataSet = null;
	public static Map<String, Integer>NSACount = null;//��¼ÿ�����ԵĻ���
	private AccessConnet ac = new AccessConnet();
	private List<ColumnCount> SortAttributes(String[] attributes)
			throws SQLException {

		PreparedStatement pstmt;
		List<ColumnCount> attriList = new LinkedList<ColumnCount>();
		List<ColumnCount> resultList = new ArrayList<ColumnCount>();// ����������
		String attr = "";
		ac.connect();
		for (String attrtemp : attributes) {// ��ÿ�����Խ��м���
			attr = attrtemp;
			String sql = "select count(*) from (SELECT DISTINCT " + attr
					+ " FROM " + Common.TABLENAME + ") ";
			pstmt = ac.con.prepareStatement(sql);
			System.out.println(sql);
			ResultSet NSArs = pstmt.executeQuery();
			Integer count = 0;
			if (NSArs.next()) {
				count = Integer.parseInt(NSArs.getString(1));
			}
			ColumnCount obj = new ColumnCount();
			obj.setColName(attr);
			obj.setColCount(count);
			attriList.add(obj);
		}
		ac.close();
		// ��ÿ�����԰��ռ���������С��������
		while (!attriList.isEmpty()) {
			int mincount = attriList.get(0).getColCount();
			int m = 0;
			for (int i = 0; i < attriList.size(); i++) {
				if (attriList.get(i).getColCount() < mincount) {
					mincount = attriList.get(i).getColCount();
					m = i;
				}
			}
			ColumnCount obj = new ColumnCount();
			obj.setColName(attriList.get(m).getColName());
			obj.setColCount(mincount);
			resultList.add(obj);
			attriList.remove(m);

		}
		return resultList;
	}

	public List<ColumnCount> InitDataset(String[] attributes)
			throws SQLException {
		String[] NSAs = { "age", "sex", "education", "marital_status",
				"workclass", "relationship", "race", "occupation" };
		List<ColumnCount> attributesList = SortAttributes(attributes);// ��ȡ���������Լ������ֵ
		Map<String, Set<String>> NsaSet = new HashMap<String, Set<String>>();
		Map<String, Integer> NsaCount = new HashMap<String, Integer>();// ����map���ڼ�¼ÿ��NSA�Ļ���
		for (String NSA : NSAs) {
			NsaSet.put(NSA, new HashSet<String>());
		}
		String squence = "";
		LinkedList<Map<String, Object>> DataSet = new LinkedList<Map<String, Object>>();
		int i = 0;
		for (ColumnCount obj : attributesList) {
			if (i < attributesList.size() - 1)
				squence = squence + obj.getColName() + ",";
			else
				squence = squence + obj.getColName();
			i++;
		}
		String sql = "select * from " + Common.TABLENAME + " order by "
				+ squence;// squnece�����NSA����
		System.out.println(sql);
		// ������˳���������ȡ���
		ac.connect();
		PreparedStatement pstmt = ac.con.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			Map<String, Object> adult = new HashMap<String, Object>();
//			adult.put("ID", rs.getString(1));
//			adult.put("age", rs.getString(2));
//			adult.put("sex", rs.getString(3));
//			adult.put("education", rs.getString(4));
//			adult.put("marital_status", rs.getString(5));
//			adult.put("workclass", rs.getString(6));
//			adult.put("relationship", rs.getString(7));
//			adult.put("race", rs.getString(8));
//			adult.put("occupation", rs.getString(9));
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
			DataSet.add(adult);
			NsaSet.get("age").add(adult.get("age").toString());
			NsaSet.get("sex").add((String)adult.get("sex"));
			NsaSet.get("education").add((String)adult.get("education"));
			NsaSet.get("marital_status").add((String)adult.get("marital_status"));
			NsaSet.get("workclass").add((String)adult.get("workclass"));
			NsaSet.get("relationship").add((String)adult.get("relationship"));
			NsaSet.get("race").add((String)adult.get("race"));
			NsaSet.get("occupation").add((String)adult.get("occupation"));
		}
		ac.close();
		MyDataSet = DataSet;
		// ����NSaSet ����ת��ΪNsacount
		for (String nsa : NSAs) {
			NsaCount.put(nsa, NsaSet.get(nsa).size());
		}
		InitialDataset.NSACount = NsaCount;
		return attributesList;// ������������������䲻ͬ��ֵ�ĸ���
	}
}
