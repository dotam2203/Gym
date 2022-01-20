package com.gym.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gym.entity.NhanVien;


public interface NhanVienRepository extends CrudRepository<NhanVien, String>{
	@Query("SELECT c FROM NhanVien c WHERE c.maNV = :maNV")
	public List<NhanVien> findByMaNV(@Param("maNV") String maNV);
	@Query("SELECT c FROM NhanVien c WHERE c.email = :email")
	public List<NhanVien> findByEmail(@Param("email") String email);
	@Query("SELECT c FROM NhanVien c WHERE c.taiKhoan.userName = :userName")
	public List<NhanVien> findByUserName(@Param("userName") String userName);
}
