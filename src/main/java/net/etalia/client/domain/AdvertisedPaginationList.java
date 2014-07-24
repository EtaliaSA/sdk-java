package net.etalia.client.domain;

import java.util.LinkedList;
import java.util.List;

public class AdvertisedPaginationList<T> extends PaginationList<T> {

	protected List<Banner> advertising = new LinkedList<Banner>();
	
	public AdvertisedPaginationList() {
		super(0, 0);
	}

	public AdvertisedPaginationList(int offset, int count) {
		super(offset, count);
	}

	public void setAdvertising(List<Banner> advertising) {
		this.advertising = advertising;
	}
	public List<Banner> getAdvertising() {
		return advertising;
	}

}
