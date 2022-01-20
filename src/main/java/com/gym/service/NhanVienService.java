package com.gym.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gym.entity.NhanVien;
import com.gym.repository.NhanVienRepository;

@Service
public class NhanVienService {
	@Autowired
	private NhanVienRepository repo;
	public List<NhanVien> listAll() {
		return (List<NhanVien>) repo.findAll();
	}
	
	public void delete(String id) {
		repo.deleteById(id);
	}

	public Optional<NhanVien> get(String id) {
		return repo.findById(id);
	}

	public void save(NhanVien nhanVien) {
		repo.save(nhanVien);
	}
	public List<NhanVien> selectByMaNV(String maNV){
		return repo.findByMaNV(maNV);
	}
	public List<NhanVien> selectByEmail(String email){
		return repo.findByEmail(email);
	}
	public List<NhanVien> selectByUserName(String userName){
		return repo.findByUserName(userName);
	}
}
