package cc.icld.model;

import java.util.LinkedList;
import java.util.Map;

/**
 * ���ڱ����1���ֵĿ�Ļ�����Ϣ
 * */
public class BlockInfo {
	private int level;// ���Ի��ֵĲ��
	private boolean flag;// ���ڼ�¼�Ŀ��Ƿ��ܼ������� true��ʾ���Ի���false��ʾ�����ٻ���
	private LinkedList<Map<String, Object>> Block;// һ�����ֿ�

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
