package collection.domain;

public class CollectionImgVO {

	private int collection_imgno; /* 컬렉션 이미지 일련번호 */
	private int fk_collectionno;  /* 컬렉션 일련번호 */
	private String img;           /* 컬렉션 이미지 */
	
	public int getCollection_imgno() {
		return collection_imgno;
	}

	public void setCollection_imgno(int collection_imgno) {
		this.collection_imgno = collection_imgno;
	}

	public int getFk_collectionno() {
		return fk_collectionno;
	}

	public void setFk_collectionno(int fk_collectionno) {
		this.fk_collectionno = fk_collectionno;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
}
