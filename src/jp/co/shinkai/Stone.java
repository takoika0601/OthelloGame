package jp.co.shinkai;

public class Stone {
	// �󔒃}�X
	public static final int BLANK = 0;
	// ����
	public static final int BLACK_STONE = 1;
	// ����
	public static final int WHITE_STONE = -1;

	private int stone;

	public Stone() {
		stone = 0;
	}

	public void setStone(int putstone) {
		stone = putstone;
	}

	public int getStone() {
		return stone;
	}
}
