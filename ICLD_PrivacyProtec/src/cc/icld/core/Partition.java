package cc.icld.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import cc.icld.model.BlockInfo;

public class Partition {
	public List<BlockInfo> BlockSetList = new ArrayList<BlockInfo>();// 用来保存φ1划分后生成的块
	public List<LinkedList<Map<String, Object>>> FinalBlockList = new ArrayList<LinkedList<Map<String, Object>>>();// 用于保存φ2划分后生成的块

	/**
	 * φ1划分(算法3.3)
	 * 
	 * 2014.11.25
	 */
	public void TopPartition(LinkedList<Map<String, Object>> BlockSet, int L,
			int K, String[] NSAs, int i, String SA) {// L代表L-多样性的参数L,K代表K-匿名中的参数K
		if (i == NSAs.length) {
			// 对不能再划分的块进行算法3.1的扰动处理
			this.R(BlockSet, SA, L);
			BlockInfo bi = new BlockInfo();
			bi.setLevel(i);
			bi.setBlock(BlockSet);
			BlockSetList.add(bi);// 完成算法
			return;
		}
		// 先按照第i个NSA属性进行划分
		String NSA = NSAs[i];
		LinkedList<LinkedList<Map<String, Object>>> TempBlockList = new LinkedList<LinkedList<Map<String, Object>>>();// 用于暂时保存划分后的块
		int x = 0;
		while (x < BlockSet.size()) {// 划分子块
			LinkedList<Map<String, Object>> SubBlock = new LinkedList<Map<String, Object>>();
			int y = x;
			while (y < BlockSet.size()) {// 找到NSA相同的元组放到SubBlock中
				if (BlockSet.get(x).get(NSA).toString()
						.equals(BlockSet.get(y).get(NSA).toString())) {
					SubBlock.add(BlockSet.get(y));
					y++;
				} else
					break;
			}
			if (!SubBlock.isEmpty()) {
				TempBlockList.add(SubBlock);
			}
			x = y;
		}
		// 对划分的子块判断是否满足继续划分的条件
		if (TempBlockList.isEmpty() || !checkBlock1(TempBlockList, K, L, SA)) {// 如果不满足划分的条件则生成叶子节点
			// 对不能再划分的块进行算法3.1的扰动处理
			this.R(BlockSet, SA, L);
			BlockInfo bi = new BlockInfo();
			bi.setLevel(i);
			bi.setBlock(BlockSet);
			BlockSetList.add(bi);
		} else {// 每个块如果都满足划分条件，则进行递归操作
			for (LinkedList<Map<String, Object>> SubBlock : TempBlockList) {
				TopPartition(SubBlock, L, K, NSAs, i + 1, SA);
			}
		}
	}

	/** 判断 φ1划分的条件（元组个数大于等于2k而且每个块中的SA的不同取值要大于等于L） (满足条件为true) */
	private boolean checkBlock1(
			LinkedList<LinkedList<Map<String, Object>>> BlockList, int K,
			int L, String SA) {
		boolean flag = true;
		for (List<Map<String, Object>> SubBlock : BlockList) {
			if (SubBlock.size() < 2 * K || !IsLDiversity(SubBlock, L, SA)) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	/** 判断一个划分的块中是否包含至少L个不同的SA值 */
	private boolean IsLDiversity(List<Map<String, Object>> Block, int L,
			String SA) {
		boolean flag = false;
		Set<String> SASet = new HashSet<String>();
		for (Map<String, Object> obj : Block)
			SASet.add((String) obj.get(SA));
		if (SASet.size() >= L)
			flag = true;
		return flag;
	}

	/**
	 * φ2划分(算法3.4)
	 * 
	 * 2014.11.25
	 */
	public void FinalPatition(LinkedList<Map<String, Object>> BlockSet, int K,
			String[] NSAs, int level) {
		if (level == NSAs.length) {
			FinalBlockList.add(BlockSet);// 完成算法
			return;
		}
		// 先按照第i个NSA属性进行划分
		String NSA = NSAs[level];
		LinkedList<BlockInfo> TempBlockList = new LinkedList<BlockInfo>();// 用于暂时保存划分后的块
		int tempx = 0;
		while (tempx < BlockSet.size()) {// 划分子块
			LinkedList<Map<String, Object>> SubBlock = new LinkedList<Map<String, Object>>();
			int y = tempx;
			while (y < BlockSet.size()) {// 找到NSA相同的元组放到SubBlock中
				if (BlockSet.get(tempx).get(NSA).toString()
						.equals(BlockSet.get(y).get(NSA).toString())) {
					SubBlock.add(BlockSet.get(y));
					y++;
				} else
					break;
			}
			if (!SubBlock.isEmpty()) {
				BlockInfo bi = new BlockInfo();
				bi.setBlock(SubBlock);
				bi.setFlag(true);
				bi.setLevel(level);
				TempBlockList.add(bi);
			}
			tempx = y;
		}
		// 处理划分后的子块
		for (int i = 0; i < TempBlockList.size(); i++) {
			if (TempBlockList.get(i).getBlock().size() < K
					&& i < TempBlockList.size() - 1) {// 如果不是最后一个块 用后面的邻居进行处理
				if (TempBlockList.get(i).getBlock().size()
						+ TempBlockList.get(i + 1).getBlock().size() >= 2 * K) {
					int x = K - TempBlockList.get(i).getBlock().size();
					for (int j = 0; j < x; j++) {
						Map<String, Object> e = TempBlockList.get(i + 1)
								.getBlock().removeFirst();// 从前往后删除
						TempBlockList.get(i).getBlock().addLast(e);// 从尾部开始添加
					}
					TempBlockList.get(i).setFlag(false);
				} else {
					int pronum = TempBlockList.get(i).getBlock().size();
					for (int j = 0; j < pronum; j++) {
						Map<String, Object> e = TempBlockList.get(i).getBlock()
								.removeLast();// 从后往前删除
						TempBlockList.get(i + 1).getBlock().addFirst(e);// 从头部开始添加
						TempBlockList.get(i + 1).setFlag(false);
					}
				}
			}
		}
		// 处理如果最后一个块不满足条件的情况
		while (TempBlockList.getLast().getBlock().size() < K) {
			for (int i = TempBlockList.size() - 2; i >= 0; i--) {
				if (TempBlockList.get(i).getBlock().isEmpty())
					continue;// 去除块有可能会有空的情况
				else if (TempBlockList.get(i).getBlock().size()
						+ TempBlockList.getLast().getBlock().size() >= 2 * K) {
					int x = K - TempBlockList.getLast().getBlock().size();
					for (int j = 0; j < x; j++) {
						Map<String, Object> e = TempBlockList.get(i).getBlock()
								.removeLast();// 从后往前删除
						TempBlockList.getLast().getBlock().addFirst(e);// 从头部开始添加
					}
					TempBlockList.getLast().setFlag(false);
				} else {
					int pronum = TempBlockList.get(i).getBlock().size();
					for (int j = 0; j < pronum; j++) {
						Map<String, Object> e = TempBlockList.get(i).getBlock()
								.removeLast();// 从后往前删除
						TempBlockList.getLast().getBlock().addFirst(e);// 从头部开始添加
						TempBlockList.getLast().setFlag(false);
					}
				}
			}
		}
		// 遍历已经处理过的块
		for (BlockInfo bi : TempBlockList) {
			if (!bi.getBlock().isEmpty() && bi.isFlag()) { // 如果还能再分，则进行递归
				FinalPatition(bi.getBlock(), K, NSAs, level + 1);
			} else if (!bi.getBlock().isEmpty() && !bi.isFlag()) { // 如果不能再分
				FinalBlockList.add(bi.getBlock());
			}
		}

	}

	/** 算法3.1,生成随机的L个SA值集 */
	private void R(LinkedList<Map<String, Object>> BlockSet, String SA, int L) {
		for (int i = 0; i < BlockSet.size(); i++) {
			Set<String> SASet = new HashSet<String>();
			SASet.add(BlockSet.get(i).get(SA).toString());
			while (SASet.size() < L) {
				int index = new Random().nextInt(BlockSet.size());
				SASet.add(BlockSet.get(index).get(SA).toString());
			}
			BlockSet.get(i).put("SASet", SASet);
		}
	}

	/**
	 * 算法3.2 Gj ∈φ2, ∀t∈Gj, |t.SA∪{t[SA]}|= ℓ d∈[0, |Gj|(ℓ-1)-1]
	 */
	public Set<String> g(LinkedList<Map<String, Object>> BlockSet, int K,
			int d, String SA) {
		Set<String> SASet = new HashSet<String>();// 保存所有t.[SA] 每个元组真实的SA值
		Set<String> SASetAll = new HashSet<String>();// 保存所有t.SA 每个元组的SA值集
		for (Map<String, Object> t : BlockSet) {
			SASet.add(t.get(SA).toString());
			@SuppressWarnings("unchecked")
			Set<String> TSASet = (Set<String>) t.get("SASet");// 当前元组的SA集合
			if (TSASet != null && !TSASet.isEmpty())
				for (String SAstr : TSASet) {
					SASetAll.add(SAstr);
				}
		}
		// 要保证SASetA.size<=(BlockSet.size+d) SASetA为要发布的SA集合
		LinkedList<String> surplus = new LinkedList<String>();// 找出所有的SASetAll-SASet
		if (SASetAll.size() > (BlockSet.size() + d)) {
			int num = SASetAll.size() - BlockSet.size() - d;
			// 将SASet中不存在的，但是在SASetAll中存在的多余的SA值删除

			for (String SAValue : SASetAll) {
				if (!SASet.contains(SAValue)) {
					surplus.add(SAValue);
				}
			}
			for (int i = 0; i < num; i++) {
				int index = new Random().nextInt(surplus.size());
				surplus.remove(index);
			}
		}
		if (surplus.isEmpty())
			return SASetAll;
		else {
			for (String SAValue : surplus)
				SASet.add(SAValue);
			return SASet;
		}
	}

	/**
	 * 一个块的标准必然惩罚计算 NCP
	 * 
	 * @param 输入一个块BlockSet
	 *            ,NSA属性名NSAs,各个属性的整表的基数
	 */
	public float NCP(LinkedList<Map<String, Object>> BlockSet, String[] NSAs,
			Map<String, Integer> GlobleNSACount) throws Exception {
		float result = 0;
		Map<String, Set<String>> BlockNSACount = new HashMap<String, Set<String>>();
		for (String Nsa : NSAs)
			BlockNSACount.put(Nsa, new HashSet<String>());
		for (Map<String, Object> obj : BlockSet) {
			for (String Nsa : NSAs) {
				BlockNSACount.get(Nsa).add((String) obj.get(Nsa));
			}
		}
		// 计算一个元组的所有属性的NCP
		for (String Nsa : NSAs) {
			if (GlobleNSACount.get(Nsa) > 1)
				result = result + (float) (BlockNSACount.get(Nsa).size() - 1)
						/ (float) (GlobleNSACount.get(Nsa) - 1);
			else
				throw new Exception();
		}
		result = result * BlockSet.size();
		return result;
	}

	/**
	 * GLP的预处理-生成每个元组的具有所有NSAs相同值的SA值的集合
	 * 
	 * @serialData 2014.12.2
	 */
	public void InitialNLP(LinkedList<Map<String, Object>> DataSet,
			String[] NSAs, String SA) {
		for (int i = 0; i < DataSet.size(); i++) {
			if (DataSet.get(i).get("NLPSASet") == null) {
				DataSet.get(i).put("NLPSASet", new HashSet<String>());
			}
			@SuppressWarnings("unchecked")
			Set<String> GLPSASet1 = (Set<String>) DataSet.get(i)
					.get("NLPSASet");
			GLPSASet1.add((String) DataSet.get(i).get(SA));
			// 寻找NSAs都相等的元组
			for (int j = i + 1; j < DataSet.size(); j++) {
				boolean flag = true;
				for (String nsa : NSAs) {
					if (!DataSet.get(i).get(nsa)
							.equals(DataSet.get(j).get(nsa))) {
						flag = false;
						break;
					}
				}
				// 如果所有的NSAs的值都相等
				if (flag) {
					GLPSASet1.add((String) DataSet.get(j).get(SA));
					if (DataSet.get(j).get("NLPSASet") == null) {
						DataSet.get(j).put("NLPSASet", new HashSet<String>());
					} else {
						@SuppressWarnings("unchecked")
						Set<String> GLPSASet = (Set<String>) DataSet.get(j)
								.get("NLPSASet");
						GLPSASet.add((String) DataSet.get(i).get(SA));
					}
				}
			}
		}
	}

	/**
	 * 计算一个块的NLP
	 * 
	 * @param SGSet为最终发布时该块中的SA的值的集合
	 */
	@SuppressWarnings("unchecked")
	public float NLP(LinkedList<Map<String, Object>> BlockSet, Set<String> SGSet) {
		float probility = 0.0f;// 值不在真是SA值集合中的概率
		for (Map<String, Object> obj : BlockSet) {
			int sum = 0;
			Set<String> NLPSet = (Set<String>) obj.get("NLPSASet");
			for (String sa : SGSet) {
				if (!NLPSet.contains(sa))
					sum++;
			}
			probility = probility + (float) sum / (float) SGSet.size();
		}
		return probility;
	}
}
