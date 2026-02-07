package homework.chapter_6;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;
import java.util.Objects;

@Entity
@IdClass(MemberProductId.class)
public class MemberProduct {
	@Id
	@ManyToOne
	@JoinColumn(name = "MEMBER_ID")
	private Member member;

	@Id
	@ManyToOne
	@JoinColumn(name = "PRODUCT_ID")
	private Product product;

	private int orderAmount;

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(int orderAmount) {
		this.orderAmount = orderAmount;
	}
}

class MemberProductId implements Serializable {
	private static final long serialVersionUID = 1L;

	private String member;
	private String product;

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		MemberProductId that = (MemberProductId) o;
		return Objects.equals(member, that.member) && Objects.equals(product, that.product);
	}

	@Override
	public int hashCode() {
		return Objects.hash(member, product);
	}
}
