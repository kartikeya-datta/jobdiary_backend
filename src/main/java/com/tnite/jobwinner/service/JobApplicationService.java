package com.tnite.jobwinner.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tnite.jobwinner.model.JobApplicationInput;
import com.tnite.jobwinner.model.JobApplication;
import com.tnite.jobwinner.repo.JobApplicationRepository;

import graphql.com.google.common.base.Function;
import io.micrometer.common.lang.NonNull;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class JobApplicationService {
    
    @Autowired
    private JobApplicationRepository jobApplicationRepository;
    
    Function<JobApplicationInput, JobApplication> mapping = aji -> {
        var jobApplication = new JobApplication();
        jobApplication.setCompanyName(aji.getCompanyName());
        jobApplication.setJobTitle(aji.getJobTitle());
        jobApplication.setSalaryRange(aji.getSalaryRange());
        jobApplication.setJobUrl(aji.getJobUrl());
        jobApplication.setAppliedDate(aji.getAppliedDate());
        jobApplication.setDescription(aji.getDescription());
        jobApplication.setNote(aji.getNote());
        jobApplication.setStatus(aji.getStatus());
        return jobApplication;
    };
    

    public Mono<JobApplication> addJobApplication(JobApplicationInput jobApplicationInput) {
        Mono<JobApplication> jobApplication = jobApplicationRepository.save(mapping.apply(jobApplicationInput));
        log.info("Added new job application: {}", jobApplicationInput);
        return jobApplication;
    }

    public Mono<JobApplication> updateJobApplication(JobApplication jobApplication) {
        log.info("Updating job application id {}", jobApplication.getId());
        return this.jobApplicationRepository.findById(jobApplication.getId())
                .flatMap(j -> {
                    j.setCompanyName(jobApplication.getCompanyName());
                    j.setJobTitle(jobApplication.getJobTitle());
                    j.setSalaryRange(jobApplication.getSalaryRange());
                    j.setJobUrl(jobApplication.getJobUrl());
                    j.setAppliedDate(jobApplication.getAppliedDate());
                    j.setDescription(jobApplication.getDescription());
                    j.setNote(jobApplication.getNote());
                    j.setStatus(jobApplication.getStatus());
                    return jobApplicationRepository.save(jobApplication).log();
                });
    }

    public Mono<JobApplication> deleteJobApplication(@NonNull Integer id) {
        log.info("Deleting job application id {}", id);
        return this.jobApplicationRepository.findById(id).switchIfEmpty(Mono.empty()).filter(Objects::nonNull)
                .flatMap(jobApplicationToBeDeleted -> jobApplicationRepository
                        .delete(jobApplicationToBeDeleted)
                        .then(Mono.just(jobApplicationToBeDeleted))).log();
    }

    public Flux<JobApplication> allJobApplication() {
        return this.jobApplicationRepository.findAll().log();
    }

    public Mono<JobApplication> getJobApplicationById(Integer id) {
        return jobApplicationRepository.findById(id);
    }

    public Flux<JobApplication> searchJobApplications(String searchTerm) {
        return jobApplicationRepository.searchJobApplications(searchTerm);
    }
}
