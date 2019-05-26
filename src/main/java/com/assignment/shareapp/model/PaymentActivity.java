package com.assignment.shareapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="payment_activity")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, 
						allowGetters = true)

public class PaymentActivity implements Serializable {
	
	private static final long serialVersionUID = -6790743123426778212L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int pid;
	
	@Column(name="act_code", nullable=false)
	@Enumerated(EnumType.STRING)
    private ActivityCode activityCode;
	
	@Column(name="act_desc", nullable=false)
	private String activityDescription;
	

	@Column(name="act_amount", nullable=false)
	private float amount = 0;
	
	@OneToOne
    @JoinColumn(name = "member_id",nullable=false)
	private Member member; 
	
	@ManyToMany
	@JoinTable(name = "transwith_member", joinColumns = @JoinColumn(name = "pid"), inverseJoinColumns = @JoinColumn(name = "member_id"))
	private List<Member> transWithMembers = new ArrayList<Member>(); //This can be used in future enhancement where 1 member did the external expenses only for few members.
	
	@OneToOne
    @JoinColumn(name = "group_id",nullable=false)
	private Group group;
	
	@Column(name="payment_date", nullable=false)
	private  Date paymentDate;
	
	@Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private  Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;
	

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public ActivityCode getActivityCode() {
		return activityCode;
	}
	
	public void setActivityCode(ActivityCode activityCode) {
		this.activityCode = activityCode;
	}

	public String getActivityDescription() {
		return activityDescription;
	}

	public void setActivityDescription(String activityDescription) {
		this.activityDescription = activityDescription;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public List<Member> getTransWithMembers() {
		return transWithMembers;
	}

	public void setTransWithMembers(List<Member> transWithMembers) {
		this.transWithMembers = transWithMembers;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public static enum ActivityCode {
		EXTERNAL,
		INTERNAL
	}
}
