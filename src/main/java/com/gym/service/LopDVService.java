package com.gym.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gym.entity.LopDV;
import com.gym.repository.LopDVRepository;

@Service
public class LopDVService {
	@Autowired
	private LopDVRepository repo;
	public List<LopDV> listAll() {
		return (List<LopDV>) repo.findAll();
	}
	
	public void delete(String id) {
		repo.deleteById(id);
	}

	public Optional<LopDV> get(String id) {
		return repo.findById(id);
	}

	public void save(LopDV lopDV) {
		repo.save(lopDV);
	}
	public List<LopDV> selectByTenLop(String tenLop){
		return repo.findByTenLop(tenLop);
	}
	public List<LopDV> selectByMaLop(String maLop){
		return repo.findByMaLop(maLop);
	}
	
	public int updateByMaLop(int trangThai, String maLop){
		return repo.updateByMaLop(trangThai, maLop);
	}
}
