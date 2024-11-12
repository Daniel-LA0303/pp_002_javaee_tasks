package org.mx.project.management.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Task {

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
	 * due date
	 */
	private LocalDate dueDate;

	/**
	 * status
	 */
	private Boolean status;

	/**
	 * priority
	 */
	private String priority;

	/**
	 * created at
	 */
	private LocalDateTime createdAt;

	/**
	 * updated at
	 */
	private LocalDateTime updatedAt;

	/**
	 * user assigned id
	 */
	private Long userAsignedId;

	/**
	 * project id
	 */
	private Long projectId;

	/**
	 * 
	 */
	public Task() {
	}

	/**
	 * @param id
	 * @param title
	 * @param description
	 * @param dueDate
	 * @param status
	 * @param priority
	 * @param createdAt
	 * @param updatedAt
	 * @param userAsignedId
	 * @param projectId
	 */
	public Task(Long id, String title, String description, LocalDate dueDate, Boolean status, String priority,
			LocalDateTime createdAt, LocalDateTime updatedAt, Long userAsignedId, Long projectId) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.dueDate = dueDate;
		this.status = status;
		this.priority = priority;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.userAsignedId = userAsignedId;
		this.projectId = projectId;
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
	 * return the value of the propertie dueDate
	 *
	 * @return the dueDate
	 */
	public LocalDate getDueDate() {
		return dueDate;
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
	 * return the value of the propertie priority
	 *
	 * @return the priority
	 */
	public String getPriority() {
		return priority;
	}

	/**
	 * return the value of the propertie projectId
	 *
	 * @return the projectId
	 */
	public Long getProjectId() {
		return projectId;
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
	 * return the value of the propertie userAsignedId
	 *
	 * @return the userAsignedId
	 */
	public Long getUserAsignedId() {
		return userAsignedId;
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
	 * set the value of the proppertie dueDate
	 *
	 * @param dueDate the dueDate to set
	 */
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
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
	 * set the value of the proppertie priority
	 *
	 * @param priority the priority to set
	 */
	public void setPriority(String priority) {
		this.priority = priority;
	}

	/**
	 * set the value of the proppertie projectId
	 *
	 * @param projectId the projectId to set
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
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
	 * set the value of the proppertie userAsignedId
	 *
	 * @param userAsignedId the userAsignedId to set
	 */
	public void setUserAsignedId(Long userAsignedId) {
		this.userAsignedId = userAsignedId;
	}

}
