package com.gk.study.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gk.study.entity.Company;
import com.gk.study.mapper.CompanyMapper;
import com.gk.study.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {
    @Autowired
    CompanyMapper mapper;

    @Override
    public List<Company> getCompanyList() {
        return mapper.selectList(new QueryWrapper<>());
    }

    @Override
    public void createCompany(Company company) {
        System.out.println(company);
        mapper.insert(company);
    }

    @Override
    public void deleteCompany(String id) {
        mapper.deleteById(id);
    }

    @Override
    public void updateCompany(Company company) {
        mapper.updateById(company);
    }

    @Override
    public List<Company> getUserCompanyList(String userId) {
        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return mapper.selectList(queryWrapper);
    }
}
