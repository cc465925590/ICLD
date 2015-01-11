package cc.icld.model;

import java.util.LinkedList;
import java.util.Map;

/**
 * 用于保存φ1划分的块的划分信息
 * */
public class BlockInfo {
	private int level;// 属性划分的层次
	private boolean flag;// 用于记录改块是否还能继续划分 true表示可以划分false表示不能再划分
	private LinkedList<Map<String, Object>> Block;// 一个划分块

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public LinkedList<Map<String, Object>> getBlock() {
		return Block;
	}

	public void setBlock(LinkedList<Map<String, Object>> block) {
		Block = block;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}
