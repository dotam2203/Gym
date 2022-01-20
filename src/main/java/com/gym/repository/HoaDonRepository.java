package com.gym.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gym.entity.HoaDon;
public interface HoaDonRepository extends CrudRepository<HoaDon, String> {
	@Query("SELECT c FROM HoaDon c ORDER BY c.maSoHD desc")
	public List<HoaDon> findAllSortMaSoHD();
	@Query("SELECT c FROM HoaDon c WHERE c.ngayHD LIKE CONCAT(:nam,'%')")
	public List<HoaDon> findByYear(@Param("nam") String nam);
	@Query("SELECT c FROM  HoaDon c WHERE  c.ngayHD LIKE CONCAT(:nam,'%') ORDER BY c.thehd.goiTap.gia desc ")//
	public List<HoaDon> findBetweenNamSortGiaTien(@Param("nam") String nam);
}
