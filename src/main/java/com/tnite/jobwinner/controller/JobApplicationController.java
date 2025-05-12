package com.tnite.jobwinner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import com.tnite.jobwinner.model.JobApplicationInput;
import com.tnite.jobwinner.model.JobApplication;
import com.tnite.jobwinner.service.JobApplicationService;

import io.micrometer.common.lang.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class JobApplicationController {

    @Bean
    private WebFluxConfigurer corsConfigurer() {
        return new WebFluxConfigurer() {
            @Value("${ui.path}")
            private String UI_PATH;
            
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/graphql").allowedOrigins(UI_PATH);
            }
        };
    }
    
    @Autowired
    private JobApplicationService jobApplicationService;
    
    
    @MutationMapping
    public Mono<JobApplication> addJobApplication(@Argument JobApplicationInput jobApplicationInput) {
        return jobApplicationService.addJobApplication(jobApplicationInput);
    }
    
    @MutationMapping
    public Mono<JobApplication> updateJobApplication(@Argument JobApplication jobApplication) {
        return jobApplicationService.updateJobApplication(jobApplication);
    }

    @MutationMapping
    public Mono<JobApplication> deleteJobApplication(@Argument @NonNull Integer id) {
        return jobApplicationService.deleteJobApplication(id);
    }
        
    @QueryMapping
    public Flux<JobApplication> allJobApplication() {
        return jobApplicationService.allJobApplication();
    }

    @QueryMapping
    public Flux<JobApplication> searchJobApplications(@Argument String searchTerm) {
        return jobApplicationService.searchJobApplications(searchTerm);
    }

}