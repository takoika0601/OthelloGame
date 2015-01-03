package jp.co.shinkai;

public class Stone {
	// ‹ó”’ƒ}ƒX
	public static final int BLANK = 0;
	// •Î
	public static final int BLACK_STONE = 1;
	// ”’Î
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
