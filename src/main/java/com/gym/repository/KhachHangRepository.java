package com.gym.repository;



import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gym.entity.KhachHang;

import java.util.List;

@Repository
public interface KhachHangRepository  extends CrudRepository<KhachHang, String> {
	@Query("SELECT c FROM KhachHang c WHERE c.maKH = :makh")
	public List<KhachHang> findByMaKH(@Param("makh") String maKH);
	@Query("SELECT c FROM KhachHang c WHERE c.email = :email")
	public List<KhachHang> findByEmail(@Param("email") String email);
	@Query("FROM KhachHang c ORDER BY c.maKH DESC")
	public List<KhachHang> findAllSortMaKH();
	
}
