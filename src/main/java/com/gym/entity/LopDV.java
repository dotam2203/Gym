package com.gym.entity;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="LOPDV")
public class LopDV {
	@Id
	@Column(name = "MaLop")
	private String maLop;
	
	@Column(name = "TenLop")
	private String tenLop;
	
	@Column(name = "TrangThai")
	private Integer trangThai;
	
	@OneToMany(mappedBy = "lopDV")
	private Collection<GoiTap> goiTap;

	public String getMaLop() {
		return maLop;
	}

	public void setMaLop(String maLop) {
		this.maLop = maLop;
	}

	public String getTenLop() {
		return tenLop;
	}

	public void setTenLop(String tenLop) {
		this.tenLop = tenLop;
	}
	

	public Integer getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(Integer trangThai) {
		this.trangThai = trangThai;
	}

	public Collection<GoiTap> getGoiTap() {
		return goiTap;
	}

	public void setGoiTap(Collection<GoiTap> goiTap) {
		this.goiTap = goiTap;
	}	
	
}
