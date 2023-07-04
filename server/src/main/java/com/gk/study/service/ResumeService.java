package com.gk.study.service;


import com.gk.study.entity.Resume;

import java.util.List;

public interface ResumeService {
    List<Resume> getResumeList();
    void createResume(Resume resume);
    void deleteResume(String id);

    void updateResume(Resume resume);

    Resume getDetail(String userId);
}
