package by.creepid.docgeneration.dao.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Table(name="regions")
@NamedQueries({
	@NamedQuery(name="Regions.findById", query="select r from Regions r where r.id = :id"),
	@NamedQuery(name="Regions.findAll", query="select r from Regions r")
})
public class Regions implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="id")
	private Long id;
	@Column(name="value")
	private String value;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
