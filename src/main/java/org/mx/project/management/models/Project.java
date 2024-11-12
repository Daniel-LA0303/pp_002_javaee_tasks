package org.mx.project.management.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Project {

	/**
	 * id
	 */
	private Long id;

	/**
	 * title
	 */
	private String title;

	/**
	 * description
	 */
	private String description;

	/**
	 * start date
	 */
	private LocalDate startDate;

	/**
	 * end date
	 */
	private LocalDate endDate;

	/**
	 * status
	 */
	private Boolean status;

	/**
	 * created at
	 */
	private LocalDateTime createdAt;

	/**
	 * updated at
	 */
	private LocalDateTime updatedAt;

	/**
	 * user id
	 */
	private Long userId;

	/**
	 * 
	 */
	public Project() {
	}

	/**
	 * @param id
	 * @param title
	 * @param description
	 * @param startDate
	 * @param endDate
	 * @param status
	 * @param createdAt
	 * @param updatedAt
	 * @param userId
	 */
	public Project(Long id, String title, String description, LocalDate startDate, LocalDate endDate, Boolean status,
			LocalDateTime createdAt, LocalDateTime updatedAt, Long userId) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.userId = userId;
	}

	/**
	 * return the value of the propertie createdAt
	 *
	 * @return the createdAt
	 */
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	/**
	 * return the value of the propertie description
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * return the value of the propertie endDate
	 *
	 * @return the endDate
	 */
	public LocalDate getEndDate() {
		return endDate;
	}

	/**
	 * return the value of the propertie id
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * return the value of the propertie startDate
	 *
	 * @return the startDate
	 */
	public LocalDate getStartDate() {
		return startDate;
	}

	/**
	 * return the value of the propertie status
	 *
	 * @return the status
	 */
	public Boolean getStatus() {
		return status;
	}

	/**
	 * return the value of the propertie title
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * return the value of the propertie updatedAt
	 *
	 * @return the updatedAt
	 */
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * return the value of the propertie userId
	 *
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * set the value of the proppertie createdAt
	 *
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * set the value of the proppertie description
	 *
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * set the value of the proppertie endDate
	 *
	 * @param endDate the endDate to set
	 */
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	/**
	 * set the value of the proppertie id
	 *
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * set the value of the proppertie startDate
	 *
	 * @param startDate the startDate to set
	 */
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	/**
	 * set the value of the proppertie status
	 *
	 * @param status the status to set
	 */
	public void setStatus(Boolean status) {
		this.status = status;
	}

	/**
	 * set the value of the proppertie title
	 *
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * set the value of the proppertie updatedAt
	 *
	 * @param updatedAt the updatedAt to set
	 */
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	/**
	 * set the value of the proppertie userId
	 *
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
