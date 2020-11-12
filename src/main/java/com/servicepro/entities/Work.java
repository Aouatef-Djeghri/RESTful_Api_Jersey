package com.servicepro.entities;
// Generated Nov 6, 2020, 2:05:45 AM by Hibernate Tools 4.3.5.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.*;

/**
 * Work generated by hbm2java
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "work", catalog = "servicebyprodb")
public class Work implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8649026710935488954L;
	private Integer idWork;
	private Address address;
	private Service service;
	private User user;
	private String title;
	private String description;
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss", timezone="CET")
	private Date dueDate;
	private String firstImage;
	private String seceondImage;
	private String thirdImage;
	private String status;
	private String type;
	private String paymentMethod;
	private Double maxPrice;
	private Double minPrice;
	@JsonManagedReference
	private Set<Application> applications = new HashSet<Application>(0);
	//@JsonManagedReference(value="review-work")
	//@JsonIgnoreProperties({"work", "user"})
	@JsonIgnore
	private Set<Review> reviews = new HashSet<Review>(0);

	public Work() {
	}

	public Work(Address address, Service service, User user, String title, String description, Date dueDate,
			String status, String paymentMethod) {
		this.address = address;
		this.service = service;
		this.user = user;
		this.title = title;
		this.description = description;
		this.dueDate = dueDate;
		this.status = status;
		this.paymentMethod = paymentMethod;
	}

	public Work(Address address, Service service, User user, String title, String description, Date dueDate,
			String firstImage, String seceondImage, String thirdImage, String status, String type, String paymentMethod,
			Double maxPrice, Double minPrice, Set<Application> applications, Set<Review> reviews) {
		this.address = address;
		this.service = service;
		this.user = user;
		this.title = title;
		this.description = description;
		this.dueDate = dueDate;
		this.firstImage = firstImage;
		this.seceondImage = seceondImage;
		this.thirdImage = thirdImage;
		this.status = status;
		this.type = type;
		this.paymentMethod = paymentMethod;
		this.maxPrice = maxPrice;
		this.minPrice = minPrice;
		this.applications = applications;
		this.reviews = reviews;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id_work", unique = true, nullable = false)
	public Integer getIdWork() {
		return this.idWork;
	}

	public void setIdWork(Integer idWork) {
		this.idWork = idWork;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "addresse", nullable = false)
	public Address getAddress() {
		return this.address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_service", nullable = false)
	public Service getService() {
		return this.service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_client", nullable = false)
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "title", nullable = false, length = 100)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "description", nullable = false)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "due_date", nullable = false, length = 19)
	public Date getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	@Column(name = "first_image")
	public String getFirstImage() {
		return this.firstImage;
	}

	public void setFirstImage(String firstImage) {
		this.firstImage = firstImage;
	}

	@Column(name = "seceond_image")
	public String getSeceondImage() {
		return this.seceondImage;
	}

	public void setSeceondImage(String seceondImage) {
		this.seceondImage = seceondImage;
	}

	@Column(name = "third_image")
	public String getThirdImage() {
		return this.thirdImage;
	}

	public void setThirdImage(String thirdImage) {
		this.thirdImage = thirdImage;
	}

	@Column(name = "status", nullable = false, length = 12)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "type", length = 9)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "payment_method", nullable = false, length = 11)
	public String getPaymentMethod() {
		return this.paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	@Column(name = "max_price", precision = 22, scale = 0)
	public Double getMaxPrice() {
		return this.maxPrice;
	}

	public void setMaxPrice(Double maxPrice) {
		this.maxPrice = maxPrice;
	}

	@Column(name = "min_price", precision = 22, scale = 0)
	public Double getMinPrice() {
		return this.minPrice;
	}

	public void setMinPrice(Double minPrice) {
		this.minPrice = minPrice;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "work")
	public Set<Application> getApplications() {
		return this.applications;
	}

	public void setApplications(Set<Application> applications) {
		this.applications = applications;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "work")
	public Set<Review> getReviews() {
		return this.reviews;
	}

	public void setReviews(Set<Review> reviews) {
		this.reviews = reviews;
	}

}
