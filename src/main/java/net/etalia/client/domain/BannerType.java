package net.etalia.client.domain;

public enum BannerType {

	Rectangle(180,150),	
	WideSkyscraper(160,600),
	Pushdown(970,90),
	BigBox(300,250),
	Filmstrip(300,600),
	Billboard(970,250);
	
	private int width;
	private int height;
	private int area;
	
	private BannerType(int w, int h) {
		this.width = w;
		this.height = h;
		this.area = w * h;
	}
	
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int getArea() {
		return area;
	}
}
