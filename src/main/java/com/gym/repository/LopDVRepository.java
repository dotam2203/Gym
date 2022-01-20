package com.gym.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gym.entity.LopDV;

public interface LopDVRepository extends CrudRepository<LopDV, String> {

	@Query("SELECT c FROM LopDV c WHERE c.tenLop = :tenLop")
	public List<LopDV> findByTenLop(@Param("tenLop") String tenLop);
	@Query("SELECT c FROM LopDV c WHERE c.maLop = :maLop")
	public List<LopDV> findByMaLop(@Param("maLop") String maLop);
	
	@Transactional
	@Modifying
	@Query("UPDATE LopDV c SET c.trangThai= :trangThai WHERE c.maLop= :maLop")
	public int updateByMaLop(@Param("trangThai") int trangThai, @Param("maLop") String maLop) ;
}