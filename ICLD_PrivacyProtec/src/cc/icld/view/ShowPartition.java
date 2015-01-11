package cc.icld.view;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cc.icld.core.Partition;
import cc.icld.model.BlockInfo;
import cc.icld.model.ColumnCount;
import cc.icld.util.Common;
import cc.icld.util.InitialDataset;
import cc.icld.util.WekaType;

public class ShowPartition extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private LinkedList<Map<String, Object>> DataSet = null;
	private String[] FinalNSAs = null;
	public List<BlockInfo> TopBlockSetList = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("method");
		if ("partition".equals(method)) {
			// Patition(request, response);
		} else if ("BlockListRandomize".equals(method)) {
			// BlockListRandomize(request, response);
		} else if ("ShowInitialDataSet".equals(method)) {// չʾ���ݼ���ʼ��
			ShowInitialDataSet(request, response);
		} else if ("doTopPartition".equals(method)) {
			doTopPartition(request, response);
		} else if ("doFinalPartition".equals(method)) {
			doFinalPartition(request, response);
		}
	}

	public void PatitionView(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("partition.jsp");
	}

	public void ShowInitialDataSet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String[] attributes = { "age", "sex", "education", "marital_status",
				"workclass", "relationship", "race" };
		List<ColumnCount> attriList = null;// ��������������������Ĳ�ֵͬ�����Ը���
		InitialDataset idset = new InitialDataset();
		try {
			attriList = idset.InitDataset(attributes);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("attriList", attriList);
		request.setAttribute("MyDataSet", idset.MyDataSet);// ������ʼ�������ݼ�
		this.DataSet = idset.MyDataSet;
		this.FinalNSAs = new String[attriList.size()];
		for (int i = 0; i < attriList.size(); i++) {
			this.FinalNSAs[i] = attriList.get(i).getColName();
		}
		// response.sendRedirect("index.jsp");
		RequestDispatcher dispatcher = request
				.getRequestDispatcher("index.jsp");
		dispatcher.forward(request, response);
	}

	/** �߲㻮�� */
	public void doTopPartition(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String chestr = (String) request.getParameter("chestr");// ��ȡѡ�е�����
		System.out.println(chestr);
		String TopNSAs[] = chestr.split(",");// ��Ҫ���и߲㻮�ֵ�����
		Partition toppartition = new Partition();
		String SA = "occupation";
		toppartition.TopPartition(this.DataSet, Common.L, Common.K, TopNSAs, 0,
				SA);
		if (toppartition.BlockSetList != null
				&& !toppartition.BlockSetList.isEmpty()) {
			request.setAttribute("BlockSetList", toppartition.BlockSetList);
			this.TopBlockSetList = toppartition.BlockSetList;
		}
		request.setAttribute("FinalNSAs", this.FinalNSAs);
		RequestDispatcher dispatcher = request
				.getRequestDispatcher("toppartition.jsp");
		dispatcher.forward(request, response);
	}

	/** ��2�㻮�� */
	public void doFinalPartition(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Partition finalpartition = new Partition();
		float GCPValue = 0.0f;// NSA�Ż���ʧ
		float GLPValue = 0.0f;// SA�������������ʧ
		String[] NSAs = { "age", "sex", "education", "marital_status",
				"workclass", "relationship", "race" };
		String SA1 = "occupation";
		// ���ڼ���GLPʱ�ĳ�ʼ������
		finalpartition.InitialNLP(DataSet, NSAs, SA1);
		// ���л��ֲ���
		if (this.TopBlockSetList != null && this.TopBlockSetList.size() > 0) {
			for (BlockInfo blockObj : this.TopBlockSetList) {
				finalpartition.FinalPatition(blockObj.getBlock(), Common.K,
						FinalNSAs, blockObj.getLevel());
			}
		}
		if (finalpartition.FinalBlockList != null
				&& finalpartition.FinalBlockList.size() > 0) {
			// ����2���SAֵ���Ŷ�
			List<Set<String>> SASetList = new ArrayList<Set<String>>();
			for (LinkedList<Map<String, Object>> Block : finalpartition.FinalBlockList) {
				String SA = "occupation";
				Set<String> SASet = finalpartition.g(Block, Common.K, Common.D,
						SA);
				SASetList.add(SASet);
				// ˳������GCP
				try {
					GCPValue = GCPValue
							+ finalpartition.NCP(Block, NSAs,
									InitialDataset.NSACount);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// ˳������GLP
				GLPValue = GLPValue + finalpartition.NLP(Block, SASet);
			}
			GCPValue = GCPValue
					/ (NSAs.length * InitialDataset.MyDataSet.size());
			GLPValue = GLPValue / InitialDataset.MyDataSet.size();
			request.setAttribute("SASetList", SASetList);
			request.setAttribute("FinalBlockList",
					finalpartition.FinalBlockList);
			// 2015.1.8 ��FinalBlockListת����weka��ʽ start
			WekaType wekatype = new WekaType();
			wekatype.DataToWeka("D:/WekaData/Third" + wekatype.TimeToStr(),
					finalpartition.FinalBlockList);
			// 2015.1.8 ��FinalBlockListת����weka��ʽ end

		}
		request.setAttribute("FinalNSAs", this.FinalNSAs);
		request.setAttribute("GCPValue", GCPValue);
		request.setAttribute("GLPValue", GLPValue);
		RequestDispatcher dispatcher = request
				.getRequestDispatcher("finalpartition.jsp");
		dispatcher.forward(request, response);
	}
}
