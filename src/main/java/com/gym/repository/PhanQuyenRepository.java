package com.gym.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gym.entity.PhanQuyen;

public interface PhanQuyenRepository  extends CrudRepository<PhanQuyen,String> {
	@Query("SELECT c FROM PhanQuyen c WHERE c.maQuyen = :maQuyen")
	public List<PhanQuyen> findByMaQuyen(@Param("maQuyen") int maQuyen);
}
