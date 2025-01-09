package cart.domain;

import product.domain.OptionVO;
import product.domain.ProductVO;

public class CartVO {
	private int cartno;       /* 장바구니 일련번호 */
	private int fk_optionno;  /* 옵션 일련번호 */
	private String fk_userid; /* 회원 아이디 */
	private int cnt;          /* 수량 */
	
	// select 용 field
	private ProductVO productVO; /* 상품 */
	private OptionVO  optionVO;  /* 옵션 */

	public int getCartno() {
		return cartno;
	}

	public void setCartno(int cartno) {
		this.cartno = cartno;
	}

	public int getFk_optionno() {
		return fk_optionno;
	}

	public void setFk_optionno(int fk_optionno) {
		this.fk_optionno = fk_optionno;
	}

	public String getFk_userid() {
		return fk_userid;
	}

	public void setFk_userid(String fk_userid) {
		this.fk_userid = fk_userid;
	}

	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

	public ProductVO getProductVO() {
		return productVO;
	}

	public void setProductVO(ProductVO productVO) {
		this.productVO = productVO;
	}

	public OptionVO getOptionVO() {
		return optionVO;
	}

	public void setOptionVO(OptionVO optionVO) {
		this.optionVO = optionVO;
	}
	
}
