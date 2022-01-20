package com.gym.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gym.entity.TaiKhoan;


public interface TaiKhoanRespository extends CrudRepository<TaiKhoan, Integer> {
	@Query("SELECT c FROM TaiKhoan c WHERE c.userName = :userName")
	public List<TaiKhoan> findByUserName(@Param("userName") String userName);
	
	@Query("SELECT c FROM TaiKhoan c WHERE c.phanQuyen.maQuyen = :maQuyen")
	public List<TaiKhoan> findByMaQuyen(@Param("maQuyen") int maQuyen);
	
	@Transactional
	@Modifying
	@Query("UPDATE TaiKhoan c SET c.trangThai= :trangThai WHERE c.userName= :userName")
	public int updateByUserName(@Param("trangThai") int trangThai, @Param("userName") String userName) ;
	
}
